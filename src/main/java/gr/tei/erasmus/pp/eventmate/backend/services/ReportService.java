package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.ReportDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Report;
import gr.tei.erasmus.pp.eventmate.backend.repository.ReportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventService eventService;



    public Report addReportToEvent(Event e, Report report) {

        var savedReport = reportRepository.save(report);

        e.getReports().add(report);
        eventService.saveEvent(e);

        return savedReport;
    }

    public Report saveReport(Report r){
        return reportRepository.save(r);
    }

    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    public Event getParentEvent(Report report) {
        return eventService.getParentEvent(report);
    }

    public List<Report> getEventReports(Event e) {
        return e.getReports();
    }

    public Boolean hasEventAnyReports(Event e) {
        return e.getReports() != null && e.getReports().size() > 0;
    }

    public Boolean isEventInReportableState(Event e) {
        return e.getState().equals(EventState.FINISHED);
    }

    public ReportDTO convertToDto(Report report) {
        ReportDTO reportDto = modelMapper.map(report, ReportDTO.class);
        return reportDto;
    }


    public Report convertToEntity(ReportDTO reportDto) {
        Report report = modelMapper.map(reportDto, Report.class);
        report.setContent(null);
        return report;
    }

}