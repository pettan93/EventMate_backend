package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Permission;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.PermissionRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
