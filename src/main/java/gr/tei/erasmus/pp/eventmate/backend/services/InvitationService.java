package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.InvitationDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Invitation;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.InvitationRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    public List<Invitation> getUserInvitations(User user) {

        return invitationRepository.findAll()
                .stream()
                .filter(invitation -> invitation.getUser() != null && invitation.getUser().equals(user))
                .collect(Collectors.toList());

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

        if(invitation.getUser() != null){
            invitationDTO.setUser(userService.convertToDto(invitation.getUser()));
        }


        return invitationDTO;
    }

    public Invitation convertToEntity(InvitationDTO invitationDto) {

        Invitation invitation = modelMapper.map(invitationDto, Invitation.class);

        if(invitationDto.getUser() != null){
            invitation.setUser(userService.convertToEntity(invitationDto.getUser()));
        }


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
