package gr.tei.erasmus.pp.eventmate.backend.resources;


import gr.tei.erasmus.pp.eventmate.backend.DTOs.EventDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.TaskDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.ReportService;
import gr.tei.erasmus.pp.eventmate.backend.services.TaskService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class EventResource {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportService reportService;


    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/event/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, event.get()))
            return ResponseEntity.status(403).build();


        return ResponseEntity.ok(eventService.convertToDto(event.get()));
    }

    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/event/{id}/guests")
    public ResponseEntity<Object> getEventByIdGuests(@PathVariable long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, event.get()))
            return ResponseEntity.status(403).build();

        return ResponseEntity.ok(event.get().getGuests()
                .stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList()));
    }


    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/event/{id}/tasks")
    public ResponseEntity<Object> getEventTasksById(@PathVariable long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, event.get()))
            return ResponseEntity.status(403).build();

        return ResponseEntity.ok(event.get().getTasks()
                .stream()
                .map(taskService::convertToPlainTaskDto)
                .collect(Collectors.toList()));
    }


    /**
     * Permission: Everyone
     */
    @PostMapping("/event")
    public ResponseEntity<Object> createEvent(@RequestBody EventDTO eventDto) {

        Event event = eventService.convertToEntity(eventDto);

        if (eventService.isNameUsed(event.getName()))
            return ResponseEntity.status(400).body("Eventname is already used");

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Event savedEvent = eventService.createEvent(user, event);

        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.convertToDto(savedEvent));
    }


    /**
     * Permission: EventOwner
     */
    @DeleteMapping("/event/{id}")
    public ResponseEntity<Object> deleteEvent(@PathVariable long id) {

        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, event.get()))
            return ResponseEntity.status(403).build();


        eventService.deleteEvent(event.get());

        return ResponseEntity.ok().build();
    }


    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/event/{id}/task")
    public ResponseEntity<Object> addEventTask(@RequestBody TaskDTO taskDto, @PathVariable long id) {

        Task task = taskService.convertToEntity(taskDto);

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventOptional.get()))
            return ResponseEntity.status(403).build();

        if (!eventService.isEditable(eventOptional.get()))
            return ResponseEntity.status(400).body("Event is no longer in editable mode.");


        Task savedTask = taskService.createTask(user, task);

        eventService.addTask(eventOptional.get(), task);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.convertToDto(savedTask));

    }

    /**
     * Permission: EventOwner
     */
    @PutMapping("/event/{id}")
    public ResponseEntity<Object> updateEvent(@RequestBody EventDTO eventDto, @PathVariable long id) {

        Event event = eventService.convertToEntity(eventDto);

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.isOwner(user, eventOptional.get()))
            return ResponseEntity.status(403).build();

        Event updatedEvent = eventService.updateEvent(eventOptional.get().getId(), event);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventService.convertToDto(updatedEvent));
    }


    /**
     * Permission: EventOwner
     */
    @PostMapping("/event/{id}/pushState")
    public ResponseEntity<Object> pushState(@PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.isOwner(user, eventOptional.get()))
            return ResponseEntity.status(403).build();


        Event savedEvent = eventService.pushEventState(eventOptional.get());


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventService.convertToDto(savedEvent));
    }


}
