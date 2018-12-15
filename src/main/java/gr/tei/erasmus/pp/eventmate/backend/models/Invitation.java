package gr.tei.erasmus.pp.eventmate.backend.models;

import gr.tei.erasmus.pp.eventmate.backend.enums.InvitationState;
import gr.tei.erasmus.pp.eventmate.backend.enums.InvitationType;

import javax.persistence.*;

@Entity
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    private String email;

    private InvitationType invitationType;

    private InvitationState invitationState;

    public Invitation() {
    }

    public Invitation(User user, String email, InvitationType invitationType, InvitationState invitationState) {
        this.user = user;
        this.email = email;
        this.invitationType = invitationType;
        this.invitationState = invitationState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public InvitationType getInvitationType() {
        return invitationType;
    }

    public void setInvitationType(InvitationType invitationType) {
        this.invitationType = invitationType;
    }

    public InvitationState getInvitationState() {
        return invitationState;
    }

    public void setInvitationState(InvitationState invitationState) {
        this.invitationState = invitationState;
    }
}
