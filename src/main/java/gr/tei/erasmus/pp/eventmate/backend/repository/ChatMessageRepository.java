package gr.tei.erasmus.pp.eventmate.backend.repository;

import gr.tei.erasmus.pp.eventmate.backend.models.ChatMessage;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


//    @Query("SELECT m FROM ChatMessage m WHERE ?1 member m.from")
//    List<ChatMessage> findChatMessageByFrom(User user);
//
//    @Query("SELECT m FROM ChatMessage m WHERE ?1 member m.to")
//    List<ChatMessage> findChatMessageByTo(User user);



    @Query("SELECT m FROM ChatMessage m WHERE (?1 like m.to) or (?1 like m.from)")
    List<ChatMessage> findChatMessageByFromOrTo(User user);

    @Query("SELECT m FROM ChatMessage m WHERE (?1 like m.to and ?2 like m.from) or (?1 like m.from and ?2 like m.to)")
    List<ChatMessage> findChatMessageByFromAndTo(User user1, User user2);


}
