package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.TaskDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.TaskState;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Boolean hasPermission(User user, Task task) {
        return task.getAssignees().contains(user) || task.getTaskOwner().equals(user);
    }

    public Boolean isOwner(User user, Task task) {
        return task.getTaskOwner().equals(user);
    }

    public Task createTask(Task task) {

        return taskRepository.save(task);
    }

    public Task createTask(User user, Task task) {

        task.setTaskOwner(user);

        task.setTaskState(TaskState.EDITABLE);

        return taskRepository.save(task);
    }

    public void deleteTask(Task task){
        taskRepository.delete(task);
    }

    public List<Task> getUserTasks(User user) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getAssignees().contains(user) || task.getTaskOwner().equals(user))
                .collect(Collectors.toList());
    }

    public Task updateTask(Long id, Task task) {
        task.setId(id);
        return taskRepository.save(task);
    }

    public Task pushTaskState(Task task) {

        task.setTaskState(TaskState.next(task.getTaskState()));

        taskRepository.save(task);

        return task;
    }


    public TaskDTO convertToDto(Task task) {

        TaskDTO taskDto = modelMapper.map(task, TaskDTO.class);
        taskDto.setSubmissionsCount(task.getSubmissions() != null ? task.getSubmissions().size() : 0);

        taskDto.setAssignees(task.getAssignees() != null ? task.getAssignees()
                .stream()
                .map(assignee -> userService.convertToDto(assignee))
                .collect(Collectors.toList()) : null);

        taskDto.setTaskOwner(userService.convertToDto(task.getTaskOwner()));

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

        return task;
    }
}
