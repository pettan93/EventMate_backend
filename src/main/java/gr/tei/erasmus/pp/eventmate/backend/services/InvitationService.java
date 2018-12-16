package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.InvitationDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Invitation;
import gr.tei.erasmus.pp.eventmate.backend.repository.InvitationRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {

    private final UserRepository userRepository;

    private final InvitationRepository invitationRepository;

    private final ModelMapper modelMapper;


    @Autowired
    public InvitationService(UserRepository userRepository,
                             InvitationRepository invitationRepository,
                             ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.modelMapper = modelMapper;
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

    public InvitationDTO convertToDto(Invitation invitation) {

        InvitationDTO invitationDTO = modelMapper.map(invitation, InvitationDTO.class);

        return invitationDTO;
    }

    public Invitation convertToEntity(InvitationDTO invitationDto) {

        Invitation invitation = modelMapper.map(invitationDto, Invitation.class);

//        if (invitationDto.getId() != null) {
//            Optional<Event> oldEvent = eventRepository.findById(invitationDto.getId());
//            if (oldEvent.isPresent()) {
//                // if exists in db
//                Event existingEvent = oldEvent.get();
//
//
//            }
//        }

        return invitation;
    }


}
