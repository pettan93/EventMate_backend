package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.InvitationDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.ErrorType;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Invitation;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.InvitationService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class InvitationResource {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private InvitationService invitationService;

    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/event/{id}/invitation/list")
    public ResponseEntity<Object> inviteUsers(@RequestBody List<InvitationDTO> invitations, @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventOptional.get()))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);

        var invitationList = invitations.stream()
                .map(invitationDTO -> invitationService.convertToEntity(invitationDTO))
                .collect(Collectors.toList());



        eventService.addInvitations(eventOptional.get(), invitationList);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventService.convertToDto(eventOptional.get()));
    }

    /**
     * Permission: Everyone involved in event
     */
    @PostMapping("/event/{id}/invitation")
    public ResponseEntity<Object> inviteUsers(@RequestBody Invitation invitation, @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!eventService.hasPermission(user, eventOptional.get()))
            return ResponseEntity.status(400).body(ErrorType.NO_PERMISSION_FOR_EVENT.statusCode);

        eventService.addInvitation(eventOptional.get(), invitation);


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventService.convertToDto(eventOptional.get()));
    }


}

