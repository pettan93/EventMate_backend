package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.SubmissionDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.SubmissionFileDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.FileType;
import gr.tei.erasmus.pp.eventmate.backend.models.Submission;
import gr.tei.erasmus.pp.eventmate.backend.models.SubmissionFile;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.SubmissionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private EventService eventService;
    @Autowired
    private SubmissionRepository submissionRepository;


    public Task submit(User user, Task task, Blob data) {

        var submission = getUserSubmissionForTask(task, user);

        if (submission == null) {
            submission = new Submission();
            submission.setSubmitter(user);
            submission.setContent(new ArrayList<>());
        } else {
            var i = task.getSubmissions().indexOf(submission);
            task.getSubmissions().remove(i);
        }

        var submissionFile = new SubmissionFile();
        submissionFile.setCreated(new Date());
        submissionFile.setType(FileType.PHOTO); // TODO
        submissionFile.setContent(data);

        submission.getContent().add(submissionFile);

        task.getSubmissions().add(submission);

        return taskService.saveTask(task);
    }

    public Boolean isSubmissionValid(Task task, Submission submission) {

        //todo implement
        return true;
    }

    public Boolean hasPermissionForSubmissionFile(User user, SubmissionFile submissionFile) {

        var submissions = submissionRepository.findAll()
                .stream()
                .filter(submission -> submission.getContent() != null && submission.getContent().contains(submissionFile))
                .collect(Collectors.toList());

        var submission = submissions.get(0);

        var task = getParentTask(submission);

        var event = eventService.getParentEvent(task);

        return eventService.isOwner(user, event) || taskService.isOwner(user, task) || submission.getSubmitter().equals(user);
    }

    public Boolean hasPermissionForSubmittion(User user, Task task) {
        return task.getAssignees().contains(user);
    }

    public Submission getUserSubmissionForTask(Task task, User user) {

        List<Submission> userSubmissions = task.getSubmissions()
                .stream()
                .filter(submission -> submission.getSubmitter().equals(user))
                .collect(Collectors.toList());

        return userSubmissions.size() == 0 ? null : userSubmissions.get(0);
    }

    public Task addTaskSubmission(Task task, Submission submission) {


        Submission userSubmission = getUserSubmissionForTask(task, submission.getSubmitter());
        if (userSubmission != null) {
            task.getSubmissions().remove(userSubmission);
        }

        task.getSubmissions().add(submission);

//        submissionRepository.save(submission);

        taskService.saveTask(task);

        return task;
    }

    public Task assignPoints(Task task, Submission submission, Long points) {

        submission.setEarnedPoints(Long.valueOf(points).intValue());


        submissionRepository.save(submission);

        return task;
    }

    public Task getParentTask(Submission s) {

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


//        if (submittionFile.getContent() != null) {
//            submittionFileDto.setContent(FileUtils.getEncodedStringFromBlob(submittionFile.getContent()));
//        }


        return submittionFileDto;
    }


    public SubmissionFile convertFileToEntity(SubmissionFileDTO submissionFileDto) {

        SubmissionFile submissionFile = modelMapper.map(submissionFileDto, SubmissionFile.class);

//        if (submissionFileDto.getContent() != null) {
//            submissionFile.setContent(FileUtils.getBlobFromEncodedString(submissionFileDto.getContent()));
//        }


        return submissionFile;
    }


}
