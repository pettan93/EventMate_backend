package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.models.Report;

import java.sql.Blob;
import java.util.Date;
import java.util.List;

public class EventDTO {

    private Long id;

    private String name;

    private Date date;

    private String place;

    private List<TaskDTO> tasks;

    private EventState state;

    private List<Report> reports;

    private List<InvitationDTO> invitations;

    private Blob photo;

    /************/

    private Integer taskCount;

    private Integer reportsCount;

    private Integer usersCount;

    private UserDTO eventOwner;

    private List<UserDTO> guests;

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

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
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

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public EventState getState() {
        return state;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
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
                ", tasks=" + tasks +
                ", state=" + state +
                ", reports=" + reports +
                ", invitations=" + invitations +
                ", photo=" + photo +
                ", taskCount=" + taskCount +
                ", reportsCount=" + reportsCount +
                ", usersCount=" + usersCount +
                ", eventOwner=" + eventOwner +
                ", guests=" + guests +
                '}';
    }
}
