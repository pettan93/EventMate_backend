package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.SubmissionFileDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.SubmissionFileRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.*;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Blob;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private ReportService reportService;
    @Autowired
    private EventService eventService;

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

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(submissionService.convertFileToDto(submissionFile.get()));
    }

    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/file/report/{id}/content")
    public ResponseEntity<Object> getReportContent(@PathVariable long id) {

        Optional<Report> report = reportService.getReportById(id);

        if (report.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(report.get())))
            return ResponseEntity.status(403).body("Use dont have permission for read given report");

        String data = FileUtils.getEncodedStringFromBlob(report.get().getContent());

        return ResponseEntity.ok(data);
    }

    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/file/report/{id}/preview")
    public ResponseEntity<Object> getReportPreview(@PathVariable long id) {

        Optional<Report> report = reportService.getReportById(id);

        if (report.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(report.get())))
            return ResponseEntity.status(403).body("Use dont have permission for read given report");

        String data = FileUtils.getEncodedStringFromBlob(report.get().getPreview());

        return ResponseEntity.ok(data);
    }

    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/file/report/{id}/content")
    public ResponseEntity<Object> saveReportContent(@PathVariable long id, @RequestBody String fileString) {

        Optional<Report> reportOptional = reportService.getReportById(id);

        if (reportOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(reportOptional.get())))
            return ResponseEntity.status(403).body("User dont have permission for read given report");

        Blob file = FileUtils.getBlobFromEncodedString(fileString);

        reportOptional.get().setContent(file);

        var report = reportOptional.get();

        report.setContent(file);

        var savedReport = reportService.saveReport(report);

        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.convertToDto(savedReport));
    }

//    /**
//     * Permission: Everyone involved in event
//     */
//    @PostMapping("/file/report/{id}/preview")
//    public ResponseEntity<Object> saveReportPreview(@PathVariable long id,@RequestBody String fileString) {
//
//        Optional<Report> reportOptional = reportService.getReportById(id);
//
//        if (reportOptional.isEmpty())
//            return ResponseEntity.notFound().build();
//
//        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//
//        if (!eventService.hasPermission(user, eventService.getParentEvent(reportOptional.get())))
//            return ResponseEntity.status(403).body("User dont have permission for read given report");
//
//        Blob file = FileUtils.getBlobFromEncodedString(fileString);
//
//
//        reportOptional.get().setPreview(file);
//
//        var report = reportOptional.get();
//
//        report.setData(file);
//
//        var savedReport = reportService.saveReport(report);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.convertToDto(savedReport));
//    }

    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/file/submissionFile/task/{id}")
    public ResponseEntity<Object> uploadSubmissionForTask(@PathVariable long id, @RequestBody SubmissionFileDTO submissionFileDTO) {

        Optional<Task> taskOptional = taskService.getById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!submissionService.hasPermissionForSubmittion(user, taskOptional.get()))
            return ResponseEntity.status(403).body("You dont have permission submitting to this task");

        var updatedTask = submissionService.submit(user, taskOptional.get(), submissionFileDTO);

        return ResponseEntity.ok(taskService.convertToDto(updatedTask));
    }

    // todo delete submissionFile

}
