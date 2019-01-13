package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.ChatMessageDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.ChatMessage;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.ChatMessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class ChatService {


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage sendMessage(ChatMessage chatMessage) {
        chatMessage.setDate(new Date());
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getAllMessagesOfUser(User user) {

        return chatMessageRepository.findChatMessageByFromOrTo(user);
    }

    public List<ChatMessage> getAllMessagessBetweenUsers(User user1, User user2) {

        return chatMessageRepository.findChatMessageByFromAndTo(user1, user2);
    }


    public List<ChatMessage> getAllLastMessagesOfUser(User user) {

        var msgs = chatMessageRepository.findChatMessageByFromOrTo(user);
        var result = new ArrayList<ChatMessage>();

        var map = new HashMap<User, ArrayList<ChatMessage>>();

        for (ChatMessage msg : msgs) {

            if (!map.containsKey(msg.getFrom())) {
                map.put(msg.getFrom(), new ArrayList<>());
            }

            if (!map.containsKey(msg.getTo())) {
                map.put(msg.getTo(), new ArrayList<>());
            }

            if (!map.get(msg.getFrom()).contains(msg)) {
                map.get(msg.getFrom()).add(msg);
            }

            if (!map.get(msg.getTo()).contains(msg)) {
                map.get(msg.getTo()).add(msg);
            }
        }

        map.put(user, null);

        for (User key : map.keySet()) {

            long maxDate = 0;
            ChatMessage resMsg = null;

            if(map.get(key) == null){
                continue;
            }

            for (ChatMessage chatMessage : map.get(key)) {
                if (chatMessage.getDate().getTime() > maxDate) {
                    resMsg = chatMessage;
                    maxDate = chatMessage.getDate().getTime();
                }
            }

            result.add(resMsg);

        }


        return result;
    }

    public ChatMessageDTO convertToDto(ChatMessage chatMessage) {

        ChatMessageDTO msgDto = modelMapper.map(chatMessage, ChatMessageDTO.class);

        msgDto.setFrom(userService.convertToLightDto(chatMessage.getFrom()));
        msgDto.setTo(userService.convertToLightDto(chatMessage.getTo()));

        return msgDto;
    }

    public ChatMessage convertToEntity(ChatMessageDTO chatMessageDTO) {

        ChatMessage msg = modelMapper.map(chatMessageDTO, ChatMessage.class);

        msg.setFrom(userService.convertFromLightDto(chatMessageDTO.getFrom()));
        msg.setTo(userService.convertFromLightDto(chatMessageDTO.getTo()));

        return msg;
    }

}
