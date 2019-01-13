package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.EmailDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.ReportRequestDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.ErrorType;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Report;
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

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ReportResource {

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
    @Autowired
    private ReportService reportService;
    @Autowired
    private EmailService emailService;

    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/event/{id}/reports")
    public ResponseEntity<Object> getEventByIdReports(@PathVariable long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, event.get()))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);


        if (!reportService.hasEventAnyReports(event.get()))
            return ResponseEntity.status(400).body(ErrorType.NO_REPORTS_IN_EVENT.statusCode);

        return ResponseEntity.ok(reportService.getEventReports(event.get())
                .stream()
                .map(reportService::convertToDto)
                .collect(Collectors.toList()));
    }


    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/report/{id}")
    public ResponseEntity<Object> getEventByIdReportById(@PathVariable long id) {
        Optional<Report> report = reportService.getReportById(id);

        if (report.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(report.get())))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);

        return ResponseEntity.ok(reportService.convertToDto(report.get()));
    }


    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/event/{id}/report")
    public ResponseEntity<Object> addReportToEvent(@RequestBody ReportRequestDTO reportDTO, @PathVariable long id) {

        Report report = reportService.convertToEntity(reportDTO);

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventOptional.get()))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);

        if (!reportService.isEventInReportableState(eventOptional.get()))
            return ResponseEntity.status(400).body(ErrorType.EVENT_NOT_IN_FINISHED_STATE.statusCode);


        report = reportService.generateReport(report,reportDTO,eventOptional.get(),user);

        var editedReport = reportService.addReportToEvent(eventOptional.get(), report);

        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.convertToDto(editedReport));
    }

    @PostMapping("/report/{id}/share")
    public ResponseEntity<Object> shareReport(@RequestBody EmailDTO emailDTO, @PathVariable long id) {

        Optional<Report> report = reportService.getReportById(id);

        if (report.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        emailService.sendMessageWithAttachment(report.get(), emailDTO);

        return ResponseEntity.ok(reportService.convertToDto(report.get()));
    }

    @DeleteMapping("/report/{id}")
    public ResponseEntity<Object> deleteReport(@PathVariable long id) {

        Optional<Report> reportOptional = reportService.getReportById(id);

        if (reportOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!(eventService.isOwner(user, eventService.getParentEvent(reportOptional.get())) || reportService.isReportCreator(reportOptional.get(), user)))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_DELETE_REPORT.statusCode);

        var parentEvent = eventService.getParentEvent(reportOptional.get());
        parentEvent.getReports().remove(reportOptional.get());

        reportService.deleteReport(reportOptional.get());

        return ResponseEntity.ok(reportService.getEventReports(parentEvent)
                .stream()
                .map(reportService::convertToDto)
                .collect(Collectors.toList()));
    }


}

