package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
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
public class UserResource {

    private final UserRepository userRepository;

    private final UserService userService;

    private final EventRepository eventRepository;

    private final EventService eventService;

    private final TaskService taskService;

    @Autowired
    public UserResource(UserRepository userRepository,
                        UserService userService,
                        EventRepository eventRepository,
                        EventService eventService,
                        TaskService taskService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.taskService = taskService;
    }

    @PostMapping("/public/register")
    public ResponseEntity<Object> registerNewUser(@RequestBody User user) {

        User newUser = userService.register(user);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.convertToDto(newUser));
    }

    @GetMapping("/public/users")
    public ResponseEntity<Object> getUsers() {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userService.getUsers()
                        .stream()
                        .map(userService::convertToPublicDto)
                        .collect(Collectors.toList()));

    }


    @GetMapping("/me")
    public ResponseEntity<Object> getUserDetail() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userService.convertToDto(user));
    }


    @GetMapping("/me/events")
    public ResponseEntity<Object> retrieveAllMyEvents() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        var result = eventService.getUserEvents(user);

        if (result.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(result
                        .stream()
                        .map(eventService::convertToDto)
                        .collect(Collectors.toList()));
    }



    @GetMapping("/me/tasks")
    public ResponseEntity<Object> retrieveAllMyTasks() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        var result = taskService.getUserTasks(user);

        if (result.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(result
                        .stream()
                        .map(taskService::convertToDto)
                        .collect(Collectors.toList()));
    }


    @GetMapping("/me/event/{id}/tasks")
    public ResponseEntity<Object> retrieveAllMyEventTask(@PathVariable Long id) {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Optional<Event> event = eventRepository.findById(id);

        var result = taskService.getUserTasks(user);

        if (event.isEmpty() || result.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(result
                        .stream()
                        .filter(task -> event.get().getTasks().contains(task))
                        .map(taskService::convertToDto)
                        .collect(Collectors.toList()));
    }



}

