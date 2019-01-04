package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.UserDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.InvitationState;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserResource {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private SubmissionService submissionService;


    @PostMapping("/public/register")
    public ResponseEntity<Object> registerNewUser(@RequestBody UserDTO user) {

        if(userService.isEmailUsed(user.getEmail()))
            return ResponseEntity.status(400).body("User email is already used");

        User newUser = userService.register(userService.convertToEntity(user));

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
    public ResponseEntity<Object> getMyDetail()  {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userService.convertToDto(user));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserDetail(@PathVariable long id) {

        User user = userService.getUserById(id);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userService.convertToDto(user));
    }

    @GetMapping("/me/invitations")
    public ResponseEntity<Object> getMyInvitations() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(invitationService.getUserInvitations(user)
                        .stream()
                        .filter(invitation -> invitation.getInvitationState().equals(InvitationState.PENDING))
                        .map(invitation -> invitationService.convertToDto(invitation))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/me/events")
    public ResponseEntity<Object> retrieveAllMyEvents() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        var result = eventService.getUserEvents(user);

        if (result.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ArrayList<>());


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

    /**
     * Permission: Everyone involved in parent event
     */
    @GetMapping("/me/task/{id}/submission")
    public ResponseEntity<Object> getTaskSumission(@PathVariable long id) {
        Optional<Task> task = taskService.getById(id);

        if (task.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();


        if (!eventService.hasPermission(user, eventService.getParentEvent(task.get())))
            return ResponseEntity.status(403).body("User has no permission task parent event");


        var submission = submissionService.getUserSubmissionForTask(task.get(),user);

        if(submission == null)
            return ResponseEntity.status(404).body("No submission for current user");

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(submissionService.convertToDto(submission));
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

