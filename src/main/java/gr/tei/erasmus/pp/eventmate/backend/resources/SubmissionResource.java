package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.SubmissionDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.SubmissionService;
import gr.tei.erasmus.pp.eventmate.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SubmissionResource {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private EventService eventService;


    /**
     * Permission: Everyone involved in parent event
     */
    @GetMapping("/task/{id}/submission")
    public ResponseEntity<Object> getTaskSumission(@PathVariable long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();


        if (!eventService.hasPermission(user, eventService.getParentEvent(task.get())))
            return ResponseEntity.status(403).body("User has no permission task parent event");

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(task.get().getSubmissions()
                        .stream()
                        .map(submission -> submissionService.convertToDto(submission))
                        .collect(Collectors.toList()));
    }


    /**
     * Permission: Task Assignees
     */
    @PostMapping("/task/{id}/submission")
    public ResponseEntity<Object> saveSubmission(@PathVariable long id,
                                            @RequestBody SubmissionDTO submissionDTO) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(taskOptional.get())))
            return ResponseEntity.status(403).body("User has no permission for task parent event");

        if(!taskService.isUserAssignee(taskOptional.get(),user))
            return ResponseEntity.status(403).body("User is not task asignee");

        if(!taskService.isTaskInSubmissionState(taskOptional.get()))
            return ResponseEntity.status(400).body("Task is not in state for creating submissions.");


        var saved = submissionService.addTaskSubmission(taskOptional.get(),submissionService.convertToEntity(submissionDTO));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(submissionService.convertToDto(saved));
    }


}
