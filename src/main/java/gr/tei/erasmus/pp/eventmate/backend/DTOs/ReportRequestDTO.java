package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import gr.tei.erasmus.pp.eventmate.backend.enums.ReportType;

public class ReportRequestDTO {
    private String name;
    private String comment;
    private ReportType type;
    private ReportInfoDTO reportInfoDTO;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public ReportInfoDTO getReportInfoDTO() {
        return reportInfoDTO;
    }

    public void setReportInfoDTO(ReportInfoDTO reportInfoDTO) {
        this.reportInfoDTO = reportInfoDTO;
    }
}
