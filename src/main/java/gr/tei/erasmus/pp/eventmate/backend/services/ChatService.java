package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.ChatMessageDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.ChatMessage;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.ChatMessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


@Component
public class ChatService {


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage sendMessage(ChatMessage chatMessage){
        chatMessage.setDate(new Date());
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getAllMessagesOfUser(User user){

        return chatMessageRepository.findChatMessageByFromOrTo(user);
    }

    public List<ChatMessage> getAllMessagessBetweenUsers(User user1,User user2){

        return chatMessageRepository.findChatMessageByFromAndTo(user1,user2);
    }


    public List<ChatMessage> getAllLastMessagesOfUser(User user){

        var msgs = chatMessageRepository.findChatMessageByFromOrTo(user);
        var result = new ArrayList<ChatMessage>();
        var used = new HashSet<>();

        for (ChatMessage msg : msgs) {
            if(!used.contains(msg.getFrom()) && (!msg.getFrom().equals(user))){
                    result.add(msg);
                    used.add(msg.getFrom());
                    continue;
            }

            if(!used.contains(msg.getTo()) && (!msg.getTo().equals(user))){
                    result.add(msg);
                    used.add(msg.getTo());
            }
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
