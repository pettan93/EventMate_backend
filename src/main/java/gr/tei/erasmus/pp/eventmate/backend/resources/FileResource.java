package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.models.SubmissionFile;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.SubmissionFileRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.FileService;
import gr.tei.erasmus.pp.eventmate.backend.services.SubmissionService;
import gr.tei.erasmus.pp.eventmate.backend.services.TaskService;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Blob;
import java.util.Optional;

@RestController
public class FileResource {

    @Autowired
    private FileService fileService;
    @Autowired
    private SubmissionFileRepository submissionFileRepository;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private TaskService taskService;


    /**
     * Permission: Event Owner, Task owner and submitter
     */
    @GetMapping("/file/submissionFile/{id}")
    public ResponseEntity<Object> getSubmissionFileById(@PathVariable long id) {

        Optional<SubmissionFile> submissionFile = submissionFileRepository.findById(id);

        if (submissionFile.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!submissionService.hasPermissionForSubmissionFile(user, submissionFile.get()))
            return ResponseEntity.status(403).body("You dont have permission for this file");

        String data = FileUtils.getEncodedStringFromBlob(submissionFile.get().getContent());

        return ResponseEntity.ok(data);
    }

    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/file/submissionFile/task/{id}")
    public ResponseEntity<Object> uploadSubmissionForTask(@PathVariable long id, @RequestBody String fileString) {

        Optional<Task> taskOptional = taskService.getById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!submissionService.hasPermissionForSubmittion(user, taskOptional.get()))
            return ResponseEntity.status(403).body("You dont have permission submitting to this task");

        Blob file = FileUtils.getBlobFromEncodedString(fileString);

        var updatedTask = submissionService.submit(user,taskOptional.get(),file);

        return ResponseEntity.ok(taskService.convertToDto(updatedTask));
    }

}
