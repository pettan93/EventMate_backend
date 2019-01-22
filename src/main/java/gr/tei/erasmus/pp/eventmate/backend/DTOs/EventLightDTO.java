package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;

import java.util.Date;
import java.util.List;

public class EventLightDTO {

    private Long id;

    private String name;

    private Date date;

    private String place;

    private EventState state;

    private List<ReportResponseDTO> reports;

    private List<InvitationDTO> invitations;

    /************/

    private Integer taskCount;

    private Integer invitationsCount;

    private Integer reportsCount;

    private Integer usersCount;

    private UserDTO eventOwner;

    private List<UserDTO> guests;

    private String photo;

    public Integer getInvitationsCount() {
        return invitationsCount;
    }

    public void setInvitationsCount(Integer invitationsCount) {
        this.invitationsCount = invitationsCount;
    }

    /*******************/



    public UserDTO getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(UserDTO eventOwner) {
        this.eventOwner = eventOwner;
    }

    public List<UserDTO> getGuests() {
        return guests;
    }

    public void setGuests(List<UserDTO> guests) {
        this.guests = guests;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getReportsCount() {
        return reportsCount;
    }

    public void setReportsCount(Integer reportsCount) {
        this.reportsCount = reportsCount;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public EventState getState() {
        return state;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public List<ReportResponseDTO> getReports() {
        return reports;
    }

    public void setReports(List<ReportResponseDTO> reports) {
        this.reports = reports;
    }


    public List<InvitationDTO> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<InvitationDTO> invitations) {
        this.invitations = invitations;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", state=" + state +
                ", reports=" + reports +
                ", invitations=" + invitations +
//                ", photo=" + photo +
                ", taskCount=" + taskCount +
                ", reportsCount=" + reportsCount +
                ", usersCount=" + usersCount +
                ", eventOwner=" + eventOwner +
                ", guests=" + guests +
                '}';
    }
}
