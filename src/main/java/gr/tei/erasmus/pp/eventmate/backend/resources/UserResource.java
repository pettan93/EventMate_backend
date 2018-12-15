package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.PermissionService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.ws.rs.NotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserResource {

    private final UserRepository userRepository;

    private final UserService userService;

    private final PermissionService permissionService;

    private final EventRepository eventRepository;


    @Autowired
    public UserResource(UserRepository userRepository,
                        UserService userService, PermissionService permissionService,
                        EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.permissionService = permissionService;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/public/register")
    public ResponseEntity<Object> registerNewUser(@RequestBody User user) {

        User newUser = userService.register(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {


        // TODO only public profile

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("user id " + id + " not found");
        }

        System.out.println(userOptional.get());

        return userOptional.get();
    }

    @GetMapping("/me")
    public User getUserDetail() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        // TODO detail

        return user;
    }


    @GetMapping("/me/events")
    public List<Event> retrieveAllMyEvents() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();


        var result = permissionService.getModels(Event.class, user);

        if (result.isEmpty()) {
            return new ArrayList<>();
        }

        return (List<Event>) result.get();
    }

    @GetMapping("/me/events/forRole/{userRoleNum}")
    public List<Event> retrieveAllMyEventsForRole(@PathVariable Long userRoleNum) {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        var result = permissionService.getModels(Event.class, user, UserRole.getByNumber(userRoleNum));

        if (result.isEmpty()) {
            return null;
        }

        return (List<Event>) result.get();
    }


    @GetMapping("/me/tasks")
    public List<Task> retrieveAllMyTasks() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        var result = permissionService.getModels(Task.class, user);

        if (result.isEmpty()) {
            return null;
        }

        return (List<Task>) result.get();
    }

    @GetMapping("/me/tasks/forRole/{userRoleNum}")
    public List<Task> retrieveAllMyTaskForRole(@PathVariable Long userRoleNum) {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        var result = permissionService.getModels(Task.class, user, UserRole.getByNumber(userRoleNum));

        if (result.isEmpty()) {
            return null;
        }

        return (List<Task>) result.get();
    }

    @GetMapping("/me/event/{id}/tasks")
    public List<Task> retrieveAllMyEventTask(@PathVariable Long id) {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            throw new NotFoundException("event id " + id + " not found");

        var result = permissionService.getModels(Task.class, user);

        if (result.isEmpty()) {
            return null;
        }

        return ((List<Task>) result.get())
                .stream()
                .filter(task -> event.get().getTasks().contains(task))
                .collect(Collectors.toList());
    }

    @GetMapping("/me/event/{id}/tasks/forRole/{userRoleNum}")
    public List<Task> retrieveAllMyEventTaskForRole(@PathVariable Long id, @PathVariable Long userRoleNum) {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            throw new NotFoundException("event id " + id + " not found");

        var result = permissionService.getModels(Task.class, user, UserRole.getByNumber(userRoleNum));

        if (result.isEmpty()) {
            return null;
        }

        return (List<Task>) result.get();
    }

}

