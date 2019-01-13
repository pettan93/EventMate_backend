package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.ChatMessageDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.ErrorType;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.models.UserPrincipal;
import gr.tei.erasmus.pp.eventmate.backend.services.ChatService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ChatResource {


    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;

    /**
     * Sends message
     */
    @PostMapping("/message")
    public ResponseEntity<Object> sendMessage(@RequestBody ChatMessageDTO msgDto) {
        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        var msg = chatService.convertToEntity(msgDto);
        msg.setFrom(user);

        return ResponseEntity.ok().body(chatService.convertToDto(chatService.sendMessage(msg)));
    }


    /**
     * All messages
     * - from me to anyone OR from anyone to me
     */
    @GetMapping("/me/messages")
    public ResponseEntity<Object> getAllMessagess() {
        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        return ResponseEntity.ok().body(
                chatService.getAllMessagesOfUser(user).stream()
                        .map(invitation -> chatService.convertToDto(invitation))
                        .collect(Collectors.toList()));
    }


    /**
     * All messages with me and given user
     */
    @GetMapping("/me/messages/user/{id}")
    public ResponseEntity<Object> getAllMessagessWithUser(@PathVariable long id) {
        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Optional<User> user2 = Optional.ofNullable(userService.getUserById(id));

        if (user2.isEmpty())
            return ResponseEntity.status(400).body(ErrorType.ENTITY_NOT_FOUND.statusCode);

        return ResponseEntity.ok().body(chatService.getAllMessagessBetweenUsers(user,user2.get()).stream()
                .map(invitation -> chatService.convertToDto(invitation))
                .collect(Collectors.toList()));
    }


    /**
     * List of lasts messages with each user I have chatted with
     */
    @GetMapping("/me/messages/last")
    public ResponseEntity<Object> getLastMessages() {
        User user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        return ResponseEntity.ok().body(chatService.getAllLastMessagesOfUser(user).stream()
                .map(invitation -> chatService.convertToDto(invitation))
                .collect(Collectors.toList()));
    }

}
