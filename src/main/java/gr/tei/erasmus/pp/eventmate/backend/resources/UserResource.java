package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserResource {

    private final UserRepository userRepository;

    private final PermissionService permissionService;

    private final EventRepository eventRepository;

    @Autowired
    public UserResource(UserRepository userRepository,
                        PermissionService permissionService,
                        EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.permissionService = permissionService;
        this.eventRepository = eventRepository;
    }


    @GetMapping("/me/events")
    public List<Event> retrieveAllMyEvents() {

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();


        var result = permissionService.getModels(Event.class, user);

        if (result.isEmpty()) {
            return null;
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

