package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import java.util.List;

public class ReportInfoDTO {
    private Boolean includeName;
    private Boolean includePlace;
    private Boolean includeOwner;
    private Boolean includeDate;
    private Boolean includeReportCreator;
    private Boolean includeReportCreatedDate;
    private List<Long> listOfIncludedGuests;
    private List<Long> listOfIncludedTasks;

    public Boolean getIncludeName() {
        return includeName;
    }

    public void setIncludeName(Boolean includeName) {
        this.includeName = includeName;
    }

    public Boolean getIncludePlace() {
        return includePlace;
    }

    public void setIncludePlace(Boolean includePlace) {
        this.includePlace = includePlace;
    }

    public Boolean getIncludeOwner() {
        return includeOwner;
    }

    public void setIncludeOwner(Boolean includeOwner) {
        this.includeOwner = includeOwner;
    }

    public Boolean getIncludeDate() {
        return includeDate;
    }

    public void setIncludeDate(Boolean includeDate) {
        this.includeDate = includeDate;
    }

    public Boolean getIncludeReportCreator() {
        return includeReportCreator;
    }

    public void setIncludeReportCreator(Boolean includeReportCreator) {
        this.includeReportCreator = includeReportCreator;
    }

    public Boolean getIncludeReportCreatedDate() {
        return includeReportCreatedDate;
    }

    public void setIncludeReportCreatedDate(Boolean includeReportCreatedDate) {
        this.includeReportCreatedDate = includeReportCreatedDate;
    }

    public List<Long> getListOfIncludedGuests() {
        return listOfIncludedGuests;
    }

    public void setListOfIncludedGuests(List<Long> listOfIncludedGuests) {
        this.listOfIncludedGuests = listOfIncludedGuests;
    }

    public List<Long> getListOfIncludedTasks() {
        return listOfIncludedTasks;
    }

    public void setListOfIncludedTasks(List<Long> listOfIncludedTasks) {
        this.listOfIncludedTasks = listOfIncludedTasks;
    }
}
