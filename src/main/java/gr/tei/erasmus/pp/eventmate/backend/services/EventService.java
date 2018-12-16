package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.EventDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Invitation;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private InvitationService invitationService;


    public Boolean hasPermission(User user, Event event) {
        return event.getGuests().contains(user) || event.getEventOwner().equals(user);
    }

    public Boolean isOwner(User user, Event event) {
        return event.getEventOwner().equals(user);
    }


    public Event asignOwner(User user, Event event) {

        event.setEventOwner(user);

        return eventRepository.save(event);
    }

    public Event createEvent(Event event) {

        event.setState(EventState.EDITABLE);

        return eventRepository.save(event);
    }

    public Event createEvent(User user, Event event) {

        event.setState(EventState.EDITABLE);

        event.setEventOwner(user);

        return eventRepository.save(event);
    }


    public List<Event> getUserEvents(User user) {
        return eventRepository.findAll()
                .stream()
                .filter(event ->
                        (event.getGuests() != null && event.getGuests().contains(user)) ||
                                (event.getEventOwner() != null && event.getEventOwner().equals(user)))
                .collect(Collectors.toList());
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
            if (!alreadyInvited(event, invitation)) {
                event.getInvitations().add(invitation);
            }
        }

        return eventRepository.save(event);
    }

    public Event pushEventState(Event event){

        event.setState(EventState.next(event.getState()));

        eventRepository.save(event);

        return event;
    }


    public Boolean isEditable(Event event){
        return event.getState().equals(EventState.EDITABLE);
    }


    public Boolean alreadyInvited(Event event, Invitation invitation) {

        return !event.getInvitations()
                .stream()
                .filter(existingInv ->
                        existingInv.getEmail() != null && invitation.getEmail() != null &&
                                existingInv.getEmail().strip().equalsIgnoreCase(invitation.getEmail().strip()) ||
                        existingInv.getUser() != null && invitation.getUser() != null &&
                                invitation.getUser().equals(existingInv.getUser()))
                .collect(Collectors.toList()).isEmpty();
    }

    public Event getParentEvent(Task task) {

        return eventRepository.findAll()
                .stream()
                .filter(event -> (event.getTasks().contains(task)))
                .collect(Collectors.toList()).get(0);
    }


    public EventDTO convertToDto(Event event) {

        EventDTO eventDto = modelMapper.map(event, EventDTO.class);
        eventDto.setTaskCount(event.getTasks() != null ? event.getTasks().size() : 0);
        eventDto.setReportsCount(event.getReports() != null ? event.getReports().size() : 0);
        eventDto.setUsersCount(event.getGuests() != null ? event.getReports().size() + 1 : 0);


        eventDto.setEventOwner(userService.convertToDto(event.getEventOwner()));
        eventDto.setGuests(event.getGuests() != null ? event.getGuests()
                .stream()
                .map(guest -> userService.convertToDto(guest))
                .collect(Collectors.toList()) : null);

        eventDto.setTasks(event.getTasks() != null ? event.getTasks()
                .stream()
                .map(task -> taskService.convertToDto(task))
                .collect(Collectors.toList()) : null);

        eventDto.setInvitations(event.getInvitations() != null ? event.getInvitations()
                .stream()
                .map(invitation -> invitationService.convertToDto(invitation))
                .collect(Collectors.toList()) : null);

        return eventDto;
    }

    public Event convertToEntity(EventDTO eventDTO) {

        Event event = modelMapper.map(eventDTO, Event.class);


        if (eventDTO.getId() != null) {
            Optional<Event> oldEvent = eventRepository.findById(eventDTO.getId());
            if (oldEvent.isPresent()) {
                // if exists in db
                Event existingEvent = oldEvent.get();

                event.setEventOwner(existingEvent.getEventOwner());
                event.setGuests(existingEvent.getGuests());
                event.setInvitations(existingEvent.getInvitations());
                event.setReports(existingEvent.getReports());
                event.setTasks(existingEvent.getTasks());

            }
        }


        return event;
    }


}
