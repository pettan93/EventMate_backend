package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.PermissionService;
import gr.tei.erasmus.pp.eventmate.backend.services.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EventResource {

    private final EventRepository eventRepository;

    private final EventService eventService;

    private final TaskService taskService;

    private final PermissionService permissionService;

    @Autowired
    public EventResource(EventRepository eventRepository,
                         EventService eventService,
                         TaskService taskService,
                         PermissionService permissionService,
                         ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.taskService = taskService;
        this.permissionService = permissionService;
    }


    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/event/{id}")
    public ResponseEntity<Object> retrieveEvent(@PathVariable long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!permissionService.hasPermission(user, event.get()))
            return ResponseEntity.status(403).build();


        return ResponseEntity.ok(eventService.convertToDto(event.get()));
    }

    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/event/{id}/dto")
    public ResponseEntity<Object> retrieveEventDto(@PathVariable long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!permissionService.hasPermission(user, event.get()))
            return ResponseEntity.status(403).build();


        return ResponseEntity.ok(eventService.convertToDto(event.get()));
    }

    /**
     * Permission: Everyone
     */
    @PostMapping("/event")
    public ResponseEntity<Object> createEvent(@RequestBody Event event) {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Event savedEvent = eventService.createEvent(event);

        permissionService.addPermissionNew(event, user, UserRole.EVENT_OWNER);

        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.convertToDto(savedEvent));
    }

    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/event/{id}/task")
    public ResponseEntity<Object> addEventTask(@RequestBody Task task, @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!permissionService.hasPermission(user, eventOptional.get()))
            return ResponseEntity.status(403).build();


        Task savedTask = taskService.createTask(task);

        eventService.addTask(eventOptional.get(), task);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.convertToDto(savedTask));

    }

    /**
     * Permission: EventOwner
     */
    @PutMapping("/event/{id}")
    public ResponseEntity<Object> updateEvent(@RequestBody Event event, @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!permissionService.hasPermissionRole(user, eventOptional.get(), UserRole.EVENT_OWNER))
            return ResponseEntity.status(403).build();

        Event updatedEvent = eventService.updateEvent(eventOptional.get().getId(),event);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventService.convertToDto(updatedEvent));
    }

    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/event/{id}/invitation")
    public ResponseEntity<Object> inviteUsers(@RequestBody Invitation invitation, @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!permissionService.hasPermission(user, eventOptional.get()))
            return ResponseEntity.status(403).build();

        eventService.addInvitation(eventOptional.get(), invitation);


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventService.convertToDto(eventOptional.get()));
    }

    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/event/{id}/invitation/list")
    public ResponseEntity<Object> inviteUsers(@RequestBody List<Invitation> invitations, @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!permissionService.hasPermission(user, eventOptional.get()))
            return ResponseEntity.status(403).build();

        eventService.addInvitations(eventOptional.get(), invitations);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventService.convertToDto(eventOptional.get()));
    }


}
