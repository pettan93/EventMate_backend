package gr.tei.erasmus.pp.eventmate.backend.DTOs;

import gr.tei.erasmus.pp.eventmate.backend.enums.InvitationState;
import gr.tei.erasmus.pp.eventmate.backend.enums.InvitationType;

public class InvitationDTO {

    private Long id;

    private UserDTO user;

    private String email;

    private InvitationType invitationType;

    private InvitationState invitationState;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
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
