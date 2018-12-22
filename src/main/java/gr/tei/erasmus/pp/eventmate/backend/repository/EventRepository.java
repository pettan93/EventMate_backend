package gr.tei.erasmus.pp.eventmate.backend.repository;

import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllById(List<Long> ids);

    Event findByName(String name);

}
