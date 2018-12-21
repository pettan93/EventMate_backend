package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.SubmissionDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.SubmissionFileDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.Submission;
import gr.tei.erasmus.pp.eventmate.backend.models.SubmissionFile;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ModelMapper modelMapper;


    public Boolean isSubmissionValid(Task task, Submission submission) {

        //todo implement
        return true;
    }


    public Submission getUserSubmissionForTask(Task task, User user) {

        List<Submission> userSubmissions = task.getSubmissions()
                .stream()
                .filter(submission -> submission.getSubmitter().equals(user))
                .collect(Collectors.toList());

        return userSubmissions.size() == 0 ? null : userSubmissions.get(0);
    }

    public Submission addTaskSubmission(Task task, Submission submission) {


        Submission userSubmission = getUserSubmissionForTask(task, submission.getSubmitter());
        if (userSubmission != null) {
            task.getSubmissions().remove(userSubmission);
        }

        task.getSubmissions().add(submission);

        taskService.saveTask(task);

        return submission;
    }

    private Task getParentTask(Submission s) {

        List<Task> tasks = taskService.getAllTasks()
                .stream()
                .filter(task -> task.getSubmissions().contains(s))
                .collect(Collectors.toList());

        return tasks.size() == 0 ? null : tasks.get(0);
    }

    public SubmissionDTO convertToDto(Submission submission) {

        SubmissionDTO submissionDto = modelMapper.map(submission, SubmissionDTO.class);

        Task parentTask = getParentTask(submission);

        submissionDto.setMaxPoints(parentTask.getPoints().intValue());
        submissionDto.setTaskId(parentTask.getId());

        if (submission.getContent() != null && submission.getContent().size() > 0) {


            List<SubmissionFileDTO> filesDtos = submission.getContent()
                    .stream()
                    .map(this::convertFileToDto)
                    .collect(Collectors.toList());
            submissionDto.setContent(new ArrayList<>(filesDtos));
        }


        submissionDto.setSubmitter((userService.convertToDto(submission.getSubmitter())));


        return submissionDto;
    }


    public Submission convertToEntity(SubmissionDTO submissionDto) {

        Submission submission = modelMapper.map(submissionDto, Submission.class);

//        if (submissionDto.getId() != null) {
//            Optional<Task> oldTask = taskRepository.findById(submissionDto.getId());
//            if (oldTask.isPresent()) {
//                // if exists in db
//                Task existingTask = oldTask.get();
//
//                task.setTaskOwner(existingTask.getTaskOwner());
//                task.setSubmissions(existingTask.getSubmissions());
//
//
//            }
//        }

        if (submissionDto.getContent() != null && submissionDto.getContent().size() > 1) {
            List<SubmissionFile> files = submissionDto.getContent()
                    .stream()
                    .map(this::convertFileToEntity)
                    .collect(Collectors.toList());
            submission.setContent(new ArrayList<>(files));
        }

        return submission;
    }


    public SubmissionFileDTO convertFileToDto(SubmissionFile submittionFile) {

        SubmissionFileDTO submittionFileDto = modelMapper.map(submittionFile, SubmissionFileDTO.class);


        submittionFileDto.setContent(FileUtils.getEncodedStringFromBlob(submittionFile.getContent()));


        return submittionFileDto;
    }


    public SubmissionFile convertFileToEntity(SubmissionFileDTO submissionFileDto) {

        SubmissionFile submissionFile = modelMapper.map(submissionFileDto, SubmissionFile.class);

        submissionFile.setContent(FileUtils.getBlobFromEncodedString(submissionFileDto.getContent()));


        return submissionFile;
    }


}
