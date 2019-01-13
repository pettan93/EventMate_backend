package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.ReportRequestDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.ReportResponseDTO;
import gr.tei.erasmus.pp.eventmate.backend.ReportCreator;
import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Report;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.ReportRepository;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
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

    private ReportCreator reportCreator = new ReportCreator();


//    public Blob generateReport(ReportRequestDTO reportRequestDTO, Event event, User creator) {
//        File f = reportCreator.generateReport(reportRequestDTO, event, creator);
//        return FileUtils.getFileBlob(f);
//    }
//
//    public Blob generatePreview(File reportFile) {
//        return FileUtils.getFileBlob(reportCreator.generatePreview(reportFile));
//    }


    public Report generateReport(Report report, ReportRequestDTO reportRequestDTO, Event event, User user) {
        File reportPdf = reportCreator.generateReport(reportRequestDTO, event, user);
        File preview = reportCreator.generatePreview(reportPdf);

        report.setContent(FileUtils.getFileBlob(reportPdf));
        report.setPreview(FileUtils.getFileBlob(preview));

        report.setReportCreator(user);

        return report;
    }

    public Report addReportToEvent(Event e, Report report) {

        var savedReport = reportRepository.save(report);

        e.getReports().add(report);
        eventService.saveEvent(e);

        return savedReport;
    }

    public Boolean isReportCreator(Report report, User user) {
        return report.getReportCreator().equals(user);
    }

    public Report saveReport(Report r) {
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

    public ReportResponseDTO convertToDto(Report report) {
        ReportResponseDTO reportDto = modelMapper.map(report, ReportResponseDTO.class);

        if (report.getPreview() != null) {
            try {
                reportDto.setPreview(FileUtils.getEncodedStringFromBlob(report.getPreview()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return reportDto;
    }

    public Report convertToEntity(ReportRequestDTO reportRequestDTO) {
        Report report = modelMapper.map(reportRequestDTO, Report.class);
        report.setContent(null);
        report.setCreated(new Date());
        return report;
    }

    public Report convertToEntity(ReportResponseDTO reportDto) {
        Report report = modelMapper.map(reportDto, Report.class);
        report.setContent(null);
        return report;
    }

    public void deleteReport(Report report) {
        reportRepository.delete(report);
    }


}
