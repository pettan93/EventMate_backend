package gr.tei.erasmus.pp.eventmate.backend.models;

import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long taskId;

    private Long eventId;

    private UserRole userRole;


    public Permission() {
    }

    public Permission(Long userId, Long taskId, Long eventId, UserRole userRole) {
        this.userId = userId;
        this.taskId = taskId;
        this.eventId = eventId;
        this.userRole = userRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", eventId=" + eventId +
                ", userRole=" + userRole +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(eventId, that.eventId) &&
                userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, taskId, eventId, userRole);
    }
}
