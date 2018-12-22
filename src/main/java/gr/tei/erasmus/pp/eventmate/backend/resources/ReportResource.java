package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.ReportDTO;
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

    /**
     * Permission: Everyone involved in event
     */
    @GetMapping("/event/{id}/report")
    public ResponseEntity<Object> getEventByIdReports(@PathVariable long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, event.get()))
            return ResponseEntity.status(403).build();


        if (!reportService.hasEventAnyReports(event.get()))
            return ResponseEntity.status(403).body("Event has no reports yet");

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
            return ResponseEntity.status(404).body("No such report found");

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventService.getParentEvent(report.get())))
            return ResponseEntity.status(403).body("Use dont have permission for read given report");

        return ResponseEntity.ok(reportService.convertToDto(report.get()));
    }


    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/event/{id}/report")
    public ResponseEntity<Object> addReportToEvent(@RequestBody ReportDTO reportDTO, @PathVariable long id) {

        Report report = reportService.convertToEntity(reportDTO);

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.status(404).body("No such event found");

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventOptional.get()))
            return ResponseEntity.status(403).body("User dont have permission for event");

        if (!reportService.isEventInReportableState(eventOptional.get()))
            return ResponseEntity.status(400).body("Event is finished yet");


        var editedReport = reportService.addReportToEvent(eventOptional.get(),report);

        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.convertToDto(editedReport));

    }


}

