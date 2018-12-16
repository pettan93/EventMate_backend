package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvitationResource {

    private final UserRepository userRepository;

    private final UserService userService;


    private final EventRepository eventRepository;


    @Autowired
    public InvitationResource(UserRepository userRepository,
                              UserService userService,
                              EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.eventRepository = eventRepository;
    }


    // TODO /me/invitations
}

