package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.PermissionService;
import gr.tei.erasmus.pp.eventmate.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TaskResource {

    private final TaskRepository taskRepository;

    private final TaskService taskService;

    private final PermissionService permissionService;

    private final EventService eventService;

    @Autowired
    public TaskResource(TaskRepository taskRepository,
                        TaskService taskService,
                        PermissionService permissionService,
                        EventService eventService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.permissionService = permissionService;
        this.eventService = eventService;
    }

    /**
     * Permission: Everyone involved in parent event
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<Object> retrieveTask(@PathVariable long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty())
            return ResponseEntity.notFound().build();


        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!permissionService.hasPermission(user, eventService.getParentEvent(task.get())))
            return ResponseEntity.status(403).build();

        return ResponseEntity.ok(task.get());
    }

    /**
     * Permission: Parent event EventOnwer and TaskOwner
     */
    @PutMapping("/task/{id}")
    public ResponseEntity<Object> updateTask(@RequestBody Task task, @PathVariable long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!permissionService.hasPermissionRole(user, eventService.getParentEvent(taskOptional.get()), UserRole.EVENT_OWNER)
                ||
                !permissionService.hasPermissionRole(user, taskOptional.get(), UserRole.TASK_OWNER))
            return ResponseEntity.status(403).build();


        taskService.updateTask(taskOptional.get().getId(), task);

        return ResponseEntity.noContent().build();
    }


}
