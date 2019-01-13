package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.TaskDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.ErrorType;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TaskResource {

    private final TaskRepository taskRepository;

    private final TaskService taskService;


    private final EventService eventService;

    @Autowired
    public TaskResource(TaskRepository taskRepository,
                        TaskService taskService,
                        EventService eventService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.eventService = eventService;
    }

    /**
     * Permission: Everyone involved in parent event
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(task.get())))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);


        if (!taskService.hasPermission(user, task.get()))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_TASK.statusCode);

        return ResponseEntity.ok(taskService.convertToDto(task.get()));
    }

    /**
     * Permission: TaskOwner or EventOwner
     */
    @DeleteMapping("/task/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!taskService.isOwner(user, taskOptional.get()) || !eventService.isOwner(user,eventService.getParentEvent(taskOptional.get())))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EDIT_TASK.statusCode);


        eventService.getParentEvent(taskOptional.get()).getTasks().remove(taskOptional.get());

        taskService.deleteTask(taskOptional.get());

        return ResponseEntity.ok().build();
    }

    /**
     * Permission: Parent event EventOnwer and TaskOwner
     */
    @PutMapping("/task/{id}")
    public ResponseEntity<Object> updateTask(@RequestBody TaskDTO taskDto, @PathVariable long id) {

        Task task = taskService.convertToEntity(taskDto);

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!taskService.isOwner(user, taskOptional.get()) || !eventService.isOwner(user,eventService.getParentEvent(taskOptional.get())))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EDIT_TASK.statusCode);


        Task updatedTask = taskService.updateTask(taskOptional.get().getId(), task);

        return ResponseEntity.ok(taskService.convertToDto(updatedTask));
    }


    /**
     * Permission: TaskOwner
     */
    @PostMapping("/task/{id}/pushState")
    public ResponseEntity<Object> pushState(@PathVariable long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!taskService.isOwner(user, taskOptional.get()))
            return ResponseEntity.status(400).body(ErrorType.USER_NOT_EVENT_OWNER.statusCode);


        Task savedTask = taskService.pushTaskState(taskOptional.get());


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskService.convertToDto(savedTask));
    }


}
