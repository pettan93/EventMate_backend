package gr.tei.erasmus.pp.eventmate.backend.models;

import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private Date date;

    private String place;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Task> tasks;

    private EventState state;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToOne
    private User eventOwner;

    @ManyToMany
    private List<User> guests;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Invitation> invitations;

    private Blob photo;


    public Event() {
    }

    public Event(String name, Date date, String place, List<Task> tasks, EventState state, List<Report> reports) {
        this.name = name;
        this.date = date;
        this.place = place;
        this.tasks = tasks;
        this.state = state;
        this.reports = reports;
        this.guests = new ArrayList<>();
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
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

    public User getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(User eventOwner) {
        this.eventOwner = eventOwner;
    }

    public List<User> getGuests() {
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", tasks=" + tasks +
                ", state=" + state +
                ", reports=" + reports +
                ", eventOwner=" + eventOwner +
                ", guests=" + guests +
                ", invitations=" + invitations +
//                ", photo=" + photo +
                '}';
    }
}
