package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.TaskDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.Permission;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    public TaskService(TaskRepository taskRepository,
                       ModelMapper modelMapper,
                       UserService userService) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    public Task createTask(Task task){

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        task.setId(id);
        return taskRepository.save(task);
    }


    public TaskDTO convertToDto(Task task) {

        TaskDTO taskDto = modelMapper.map(task, TaskDTO.class);
        taskDto.setSubmissionsCount(task.getSubmissions() != null ? task.getSubmissions().size() : 0);

        User taskOwner =
                userService.getUserById(task.getPermissions()
                        .stream()
                        .filter(permission -> permission.getUserRole().equals(UserRole.TASK_OWNER))
                        .collect(Collectors.toList()).get(0).getUserId());

        List<User> taskAsignees = userService.getUsersById(task.getPermissions()
                .stream()
                .filter(permission -> permission.getUserRole().equals(UserRole.TASK_ASSIGNEE))
                .map(Permission::getUserId)
                .collect(Collectors.toList()));

        taskDto.setTaskOwner(userService.convertToDto(taskOwner));
        taskDto.setAssignees(taskAsignees
                .stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList()));

        return taskDto;
    }


    private Task convertToEntity(TaskDTO task) {

        return null;
    }
}
