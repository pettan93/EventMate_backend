package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Invitation;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public EventService(EventRepository eventRepository, TaskRepository taskRepository) {
        this.eventRepository = eventRepository;
        this.taskRepository = taskRepository;
    }

    public Event updateEvent(Long id, Event event) {
        event.setId(id);
        return eventRepository.save(event);
    }

    public Event addTask(Event event, Task task) {

        if (event.getTasks() == null) {
            event.setTasks(new ArrayList<>());
        }

        event.getTasks().add(task);

        return eventRepository.save(event);
    }


    public Event addInvitation(Event event, Invitation invitation) {

        if (event.getInvitations() == null) {
            event.setInvitations(new ArrayList<>());
        }

        //TODO fire invitation
        event.getInvitations().add(invitation);

        return eventRepository.save(event);
    }

    public Event addInvitations(Event event, List<Invitation> invitations) {

        if (event.getInvitations() == null) {
            event.setInvitations(new ArrayList<>());
        }

        for (Invitation invitation : invitations) {

            //TODO fire invitation
            event.getInvitations().add(invitation);

        }

        return eventRepository.save(event);
    }

    public Event getParentEvent(Task task) {

        return eventRepository.findAll()
                .stream()
                .filter(event -> (event.getTasks().contains(task)))
                .collect(Collectors.toList()).get(0);

    }


}
