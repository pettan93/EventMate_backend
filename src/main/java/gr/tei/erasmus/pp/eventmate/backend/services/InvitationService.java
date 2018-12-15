package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Invitation;
import gr.tei.erasmus.pp.eventmate.backend.repository.InvitationRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {

    private final UserRepository userRepository;

    private final InvitationRepository invitationRepository;


    @Autowired
    public InvitationService(UserRepository userRepository,
                             InvitationRepository invitationRepository) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
    }


    public void processInvitation(Invitation invitation, Event event) {

        switch (invitation.getInvitationType()) {
            case EMAIL: {
                processEmailInvitation(invitation, event);
                break;
            }
            case NOTIFICATION: {
                processNotificationInvitation(invitation, event);
                break;
            }
            case EMAIL_AND_NOTIFICATION: {
                processEmailInvitation(invitation, event);
                processNotificationInvitation(invitation, event);
                break;
            }
        }

    }


    private void processEmailInvitation(Invitation invitation, Event event) {


    }

    private void processNotificationInvitation(Invitation invitation, Event event) {


    }


}
