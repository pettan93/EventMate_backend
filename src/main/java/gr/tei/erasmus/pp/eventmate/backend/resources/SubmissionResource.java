package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.SubmissionDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.ErrorType;
import gr.tei.erasmus.pp.eventmate.backend.enums.TaskState;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.SubmissionRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.SubmissionService;
import gr.tei.erasmus.pp.eventmate.backend.services.TaskService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SubmissionResource {

    @Autowired
    private TaskService taskService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubmissionRepository submissionRepository;


    @GetMapping("/task/{id}/submission/{userId}")
    public ResponseEntity<Object> getTaskSubmissions(@PathVariable long id, @PathVariable long userId) {
        Optional<Task> task = taskService.getById(id);

        if (task.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(task.get())))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);


        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(task.get().getSubmissions().stream().filter(s -> s.getSubmitter().getId() == userId)
                        .map(submission -> submissionService.convertToDto(submission))
                        .collect(Collectors.toList()));
    }


    /**
     * Permission: Task Assignees
     */
    @PostMapping("/task/{id}/submission")
    public ResponseEntity<Object> saveSubmission(@PathVariable long id,
                                                 @RequestBody SubmissionDTO submissionDTO) {

        Optional<Task> taskOptional = taskService.getById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(taskOptional.get())))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);

        if (!taskService.isUserAssignee(taskOptional.get(), user))
            return ResponseEntity.status(400).body(ErrorType.USER_NOT_TASK_ASSIGNEE.statusCode);

        if (!taskService.isTaskInState(taskOptional.get(), TaskState.IN_PLAY))
            return ResponseEntity.status(400).body(ErrorType.TASK_IS_NOT_IN_PLAYABLE_STATE.statusCode);

        submissionDTO.setSubmitter(userService.convertToDto(user));

        var savedTask = submissionService.addTaskSubmission(taskOptional.get(), submissionService.convertToEntity(submissionDTO));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskService.convertToDto(savedTask));
    }

    @PostMapping("/submission/{id}/assignPoints/{points}")
    public ResponseEntity<Object> asignPoints(@PathVariable long id, @PathVariable long points) {

        Optional<Submission> submissionOptional = submissionRepository.findById(id);

        if (submissionOptional.isEmpty())
            return ResponseEntity.notFound().build();

        var parentTask = submissionService.getParentTask(submissionOptional.get());
        var parentEvent = eventService.getParentEvent(parentTask);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.isOwner(user, parentEvent) || !taskService.isOwner(user, parentTask))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_ASSIGN_POINTS.statusCode);

        if (!taskService.isTaskInState(parentTask,TaskState.IN_REVIEW))
            return ResponseEntity.status(400).body(ErrorType.TASK_IS_NOT_IN_REVIEW_STATE.statusCode);

        if (points > parentTask.getPoints())
            return ResponseEntity.status(400).body(ErrorType.MORE_POINTS_THAN_ALLOWED.statusCode);

        var submission = submissionOptional.get();

        var savedTask = submissionService.assignPoints(parentTask, submission, points);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskService.convertToDto(savedTask));
    }


    @PostMapping("/task/{id}/assignPoints")
    public ResponseEntity<Object> assignPoints(@PathVariable long id,
                                                 @RequestBody List<UserSubmissionPoints> points) {

        Optional<Task> taskOptional = taskService.getById(id);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();


        if (taskOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

       if (!eventService.hasPermission(user, eventService.getParentEvent(taskOptional.get())))
           return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);

        if (!taskService.isTaskInState(taskOptional.get(), TaskState.IN_REVIEW))
            return ResponseEntity.status(400).body(ErrorType.TASK_IS_NOT_IN_REVIEW_STATE.statusCode);

        if(!taskService.isOwner(user,taskOptional.get()) || !eventService.isOwner(user,eventService.getParentEvent(taskOptional.get()))){
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_ASSIGN_POINTS.statusCode);
        }

        for (UserSubmissionPoints point : points) {
            if(point.getPoints() > taskOptional.get().getPoints()){
                return ResponseEntity.status(400).body(ErrorType.MORE_POINTS_THAN_ALLOWED.statusCode);
            }
        }

        submissionService.assignPoints(taskOptional.get(),points);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();

    }

}
