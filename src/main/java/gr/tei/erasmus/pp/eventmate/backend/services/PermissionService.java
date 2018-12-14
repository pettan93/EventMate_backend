package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Permission;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.PermissionRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    private final EventRepository eventRepository;
    private final EventService eventService;

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository,
                             EventRepository eventRepository,
                             EventService eventService,
                             TaskRepository taskRepository,
                             TaskService taskService) {
        this.permissionRepository = permissionRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }


    public Event addPermission(Event event, Permission permission) {

        // TODO : check if permission valid

        Permission savedPermission = permissionRepository.save(permission);

        event.getPermissions().add(savedPermission);

        eventRepository.save(event);

        return event;
    }

    public Task addPermission(Task task, Permission permission) {

        // TODO : check if permission valid

        Permission savedPermission = permissionRepository.save(permission);

        task.getPermissions().add(savedPermission);

        taskRepository.save(task);

        return task;
    }

    public Event addPermissions(Event event, List<Permission> permissions) {

        for (Permission permission : permissions) {
            addPermission(event, permission);
        }

        return event;
    }

    public Task addPermissions(Task task, List<Permission> permissions) {

        for (Permission permission : permissions) {
            addPermission(task, permission);
        }

        return task;
    }

    public Optional<List> getModels(Class model, User user) {
        return getModels(model, user, null);
    }

    public Optional<List> getModels(Class model, User user, UserRole userRole) {

        List<Permission> permissions = permissionRepository.findPermissionsByUserId(user.getId());

        if (permissions.size() > 0) {
            if (model.equals(Event.class)) {

                return Optional.of(
                        eventRepository.findAllById(permissions
                                .stream()
                                .filter(permission -> userRole == null || permission.getUserRole().equals(userRole))
                                .filter(permission -> Objects.nonNull(permission.getEventId()))
                                .map(Permission::getEventId)
                                .collect(Collectors.toList())));
            }

            if (model.equals(Task.class)) {
                return Optional.of(
                        taskRepository.findAllById(permissions
                                .stream()
                                .filter(permission -> userRole == null || permission.getUserRole().equals(userRole))
                                .filter(permission -> Objects.nonNull(permission.getTaskId()))
                                .map(Permission::getTaskId)
                                .collect(Collectors.toList())));
            }
        }

        return Optional.empty();
    }

    public Boolean hasPermission(User user, Event event) {

        return !event.getPermissions()
                .stream()
                .filter(permission -> permission.getUserId().equals(user.getId()))
                .collect(Collectors.toList()).isEmpty();
    }

    public Boolean hasPermissionRole(User user, Event event, UserRole userRole) {

        return !event.getPermissions()
                .stream()
                .filter(permission -> permission.getUserId().equals(user.getId()))
                .filter(permission -> permission.getUserRole().equals(userRole))
                .collect(Collectors.toList()).isEmpty();
    }

    public Boolean hasPermissionRole(User user, Task task, UserRole userRole) {

        return !task.getPermissions()
                .stream()
                .filter(permission -> permission.getUserId().equals(user.getId()))
                .filter(permission -> permission.getUserRole().equals(userRole))
                .collect(Collectors.toList()).isEmpty();
    }

}
