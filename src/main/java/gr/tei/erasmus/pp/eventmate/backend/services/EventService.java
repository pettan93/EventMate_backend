package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.EventDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.enums.InvitationType;
import gr.tei.erasmus.pp.eventmate.backend.enums.TaskState;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
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
    @Autowired
    private ReportService reportService;
    @Autowired
    private EmailService emailService;


    public Boolean isNameUsed(String name) {
        return eventRepository.findByName(name.strip()) != null;
    }

    public Boolean hasPermission(User user, Event event) {
//        System.out.println("has permission user " + user + " na " + event);
        return event.getGuests().contains(user) || event.getEventOwner().equals(user);
    }

    public Boolean isOwner(User user, Event event) {
        return event.getEventOwner().equals(user);
    }

    public Event saveEvent(Event e) {
        return eventRepository.save(e);
    }

    public Event asignOwner(User user, Event event) {

        event.setEventOwner(user);

        return eventRepository.save(event);
    }

    public Event createEvent(Event event) {

        event.setState(EventState.EDITABLE);

        return eventRepository.save(event);
    }


    private Event invite(Event event) {

        if (event.getGuests() == null) {
            event.setGuests(new ArrayList<>());
        }

        if (event.getInvitations() != null) {


            for (Invitation invitation : event.getInvitations()) {

                if (invitation.getInvitationType().equals(InvitationType.NOTIFICATION)) {
                    System.out.println("process invite user");

                    if (event.getGuests().contains(invitation.getUser())) {
                        System.out.println("user already guest");
                    } else {
                        event.getGuests().add(invitation.getUser());
                    }
                    continue;
                }

                if (invitation.getInvitationType().equals(InvitationType.EMAIL)) {
                    System.out.println("process invite mail");
                    emailService.sendEmailInvitation(invitation.getEmail(), event);
                }
            }
        }

        return event;
    }

    public Event createEvent(User user, Event event) {

        System.out.println("create event");

        event = invite(event);

        event.setState(EventState.EDITABLE);

        event.setEventOwner(user);

        return eventRepository.save(event);
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
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


        event = invite(event);


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

    public Event pushEventState(Event event) {

        var nextState = EventState.next(event.getState());

        event.setState(EventState.next(event.getState()));


        if (nextState.equals(EventState.IN_PLAY)) {
            for (Task task : event.getTasks()) {
                task.setTaskState(TaskState.READY_TO_START);
            }
        }

        if (nextState.equals(EventState.UNDER_EVALUATION)) {
            for (Task task : event.getTasks()) {
                task.setTaskState(TaskState.IN_REVIEW);
            }
        }

        eventRepository.save(event);

        return event;
    }


    public Boolean isEditable(Event event) {
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

    public Event getParentEvent(Report report) {

        return eventRepository.findAll()
                .stream()
                .filter(event -> (event.getReports() != null && event.getReports().contains(report)))
                .collect(Collectors.toList()).get(0);
    }

    public EventDTO convertToDto(Event event) {

        EventDTO eventDto = modelMapper.map(event, EventDTO.class);
        eventDto.setTaskCount(event.getTasks() != null ? event.getTasks().size() : 0);
        eventDto.setReportsCount(event.getReports() != null ? event.getReports().size() : 0);
        eventDto.setUsersCount(event.getGuests() != null ? event.getGuests().size() + 1 : 0);
        eventDto.setInvitationsCount(event.getInvitations() != null ? event.getInvitations().size() : 0);


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

        eventDto.setReports(event.getReports() != null ? event.getReports()
                .stream()
                .map(report -> reportService.convertToDto(report))
                .collect(Collectors.toList()) : null);

        if (event.getPhoto() != null) {
            try {

                eventDto.setPhoto(FileUtils.getEncodedStringFromBlob(event.getPhoto()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

        if (eventDTO.getPhoto() != null) {
            try {

                event.setPhoto(FileUtils.getBlobFromEncodedString(eventDTO.getPhoto()));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return event;
    }


}
