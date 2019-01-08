package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.models.Report;
import gr.tei.erasmus.pp.eventmate.backend.models.SubmissionFile;
import gr.tei.erasmus.pp.eventmate.backend.repository.SubmissionFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private SubmissionFileRepository submissionFileRepository;


    public Optional<SubmissionFile> getSubmissionFileById(long id){
        return submissionFileRepository.findById(id);
    }

    public Blob getSubmissionFileContent(SubmissionFile submissionFile){
        return submissionFile.getData();
    }

    public Blob getReportContent(Report report){
        return report.getContent();
    }

    public Blob getReportPreview(Report report){
        return report.getPreview();
    }






}
