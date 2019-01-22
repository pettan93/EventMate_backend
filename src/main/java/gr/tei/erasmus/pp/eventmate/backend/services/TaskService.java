package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.PlainTaskDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.TaskDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.TaskLightDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.TaskState;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private SubmissionService submissionService;

    public Boolean hasPermission(User user, Task task) {
        return task.getAssignees().contains(user) || task.getTaskOwner().equals(user);
    }

    public Boolean isOwner(User user, Task task) {
        return task.getTaskOwner().equals(user);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task createTask(Task task) {

        return taskRepository.save(task);
    }

    public Optional<Task> getById(long id) {
        return taskRepository.findById(id);
    }


    public Boolean isTaskInState(Task task, TaskState state) {
        return task.getTaskState().equals(state);
    }


    public Boolean isUserAssignee(Task task, User user) {
        return task.getAssignees().contains(user);
    }

    public Task createTask(User user, Task task) {

        task.setTaskOwner(user);

        task.setTaskState(TaskState.EDITABLE);

        return taskRepository.save(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public List<Task> getUserTasks(User user) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getAssignees().contains(user) || task.getTaskOwner().equals(user))
                .collect(Collectors.toList());
    }

    public Task updateTask(Task original, Task newTask) {
        newTask.setId(original.getId());
        newTask.setTaskState(original.getTaskState());


        HashSet hs = new HashSet(newTask.getAssignees());  // willl not add the duplicate values
        newTask.getAssignees().clear();
        newTask.getAssignees().addAll(hs);

        newTask.setSubmissions(new ArrayList<>());

        return taskRepository.save(newTask);
    }

    public Task pushTaskState(Task task) {

        task.setTaskState(TaskState.next(task.getTaskState()));

        taskRepository.save(task);

        return task;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public PlainTaskDTO convertToPlainTaskDto(Task task) {
        PlainTaskDTO taskDto = modelMapper.map(task, PlainTaskDTO.class);

        if (task.getPhoto() != null) {
            try {
                taskDto.setPhoto(FileUtils.getEncodedStringFromBlob(task.getPhoto()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return taskDto;
    }


    public TaskDTO convertToDto(Task task) {

        TaskDTO taskDto = modelMapper.map(task, TaskDTO.class);
        taskDto.setSubmissionsCount(task.getSubmissions() != null ? task.getSubmissions().size() : 0);

        taskDto.setAssignees(task.getAssignees() != null ? task.getAssignees()
                .stream()
                .map(assignee -> userService.convertToDto(assignee))
                .collect(Collectors.toList()) : null);

        taskDto.setSubmissions(task.getSubmissions() != null ? task.getSubmissions()
                .stream()
                .map(submission -> submissionService.convertToDto(submission))
                .collect(Collectors.toList()) : null);

        taskDto.setTaskOwner(userService.convertToDto(task.getTaskOwner()));

        taskDto.setEventId(eventService.getParentEvent(task).getId());

        taskDto.setParentEventState(eventService.getParentEvent(task).getState());

        if (task.getPhoto() != null) {
            try {
                taskDto.setPhoto(FileUtils.getEncodedStringFromBlob(task.getPhoto()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return taskDto;
    }

    public TaskLightDTO convertToLightDto(Task task) {

        TaskLightDTO taskDto = modelMapper.map(task, TaskLightDTO.class);
        taskDto.setSubmissionsCount(task.getSubmissions() != null ? task.getSubmissions().size() : 0);

        taskDto.setAssignees(task.getAssignees() != null ? task.getAssignees()
                .stream()
                .map(assignee -> userService.convertToDto(assignee))
                .collect(Collectors.toList()) : null);


        taskDto.setTaskOwner(userService.convertToDto(task.getTaskOwner()));

        taskDto.setEventId(eventService.getParentEvent(task).getId());

        taskDto.setParentEventState(eventService.getParentEvent(task).getState());

        if (task.getPhoto() != null) {
            try {
                taskDto.setPhoto(FileUtils.getEncodedStringFromBlob(task.getPhoto()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return taskDto;
    }


    public Task convertToEntity(TaskDTO taskDto) {

        Task task = modelMapper.map(taskDto, Task.class);

        if (taskDto.getId() != null) {
            Optional<Task> oldTask = taskRepository.findById(taskDto.getId());
            if (oldTask.isPresent()) {
                // if exists in db
                Task existingTask = oldTask.get();

                task.setTaskOwner(existingTask.getTaskOwner());
                task.setSubmissions(existingTask.getSubmissions());
            }
        }

        if (taskDto.getPhoto() != null) {
            try {
                task.setPhoto(FileUtils.getBlobFromEncodedString(taskDto.getPhoto()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return task;
    }
}
