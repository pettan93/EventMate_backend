package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.PermissionService;
import gr.tei.erasmus.pp.eventmate.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class PermissionResource {

    private final PermissionService permissionService;

    private final EventRepository eventRepository;
    private final EventService eventService;

    private final TaskRepository taskRepository;
    private final TaskService taskService;



    @Autowired
    public PermissionResource(PermissionService permissionService,
                              EventRepository eventRepository,
                              EventService eventService,
                              TaskRepository taskRepository,
                              TaskService taskService) {
        this.permissionService = permissionService;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }


    /**
     * Permission: EventOwner
     */
    @PostMapping("/permission/event/{id}")
    public ResponseEntity<Object> addEventPermission(@RequestBody Permission permission,
                                                     @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (permissionService.hasPermissionRole(user, eventOptional.get(), UserRole.EVENT_OWNER))
            return ResponseEntity.status(403).build();

        permissionService.addPermission(eventOptional.get(),permission);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(eventOptional.get().getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Permission: TaskOwner
     */
    @PostMapping("/permission/task/{id}")
    public ResponseEntity<Object> addTaskPermission(@RequestBody Permission permission,
                                                     @PathVariable long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (permissionService.hasPermissionRole(user, taskOptional.get(), UserRole.TASK_OWNER))
            return ResponseEntity.status(403).build();

        permissionService.addPermission(taskOptional.get(),permission);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(taskOptional.get().getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Permission: EventOwner
     */
    @PostMapping("/permission/event/{id}/list")
    public ResponseEntity<Object> addEventPermissions(@RequestBody List<Permission> permission,
                                                     @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (permissionService.hasPermissionRole(user, eventOptional.get(), UserRole.EVENT_OWNER))
            return ResponseEntity.status(403).build();

        permissionService.addPermissions(eventOptional.get(),permission);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(eventOptional.get().getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Permission: TaskOwner
     */
    @PostMapping("/permission/task/{id}/list")
    public ResponseEntity<Object> addTaskPermissions(@RequestBody List<Permission> permission,
                                                    @PathVariable long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (permissionService.hasPermissionRole(user, taskOptional.get(), UserRole.TASK_OWNER))
            return ResponseEntity.status(403).build();

        permissionService.addPermissions(taskOptional.get(),permission);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(taskOptional.get().getId()).toUri();

        return ResponseEntity.created(location).build();
    }






}
