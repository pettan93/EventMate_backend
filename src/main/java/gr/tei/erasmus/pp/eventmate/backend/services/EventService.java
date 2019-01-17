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


    private Event invite(Event event) {


        if (event.getInvitations() != null) {

            for (Invitation invitation : event.getInvitations()) {

                if (invitation.getInvitationType().equals(InvitationType.NOTIFICATION)) {

                    if (event.getGuests().contains(invitation.getUser())) {
                        System.out.println("uz tam je");
                    } else {
                        System.out.println("jeste tam neni");
                        event.getGuests().add(invitation.getUser());
                    }
                    continue;
                }

                if (invitation.getInvitationType().equals(InvitationType.EMAIL)) {
                    Thread thread = new Thread() {
                        public void run() {
                            emailService.sendEmailInvitation(invitation.getEmail(), event);
                        }
                    };
                    thread.start();
                }
            }
        }


        return event;
    }

    public Event createEvent(User owner, Event event) {

        System.out.println("create event");

        if (event.getGuests() == null) {
            event.setGuests(new ArrayList<>());
        }

        if (!event.getGuests().contains(owner)) {
            event.getGuests().add(owner);
        }


        var editedevent = invite(event);
        if (editedevent.getState() == null) {
            editedevent.setState(EventState.EDITABLE);
        }
        editedevent.setEventOwner(owner);

        return eventRepository.save(editedevent);
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

        System.out.println("create event");

        if (event.getGuests() == null) {
            event.setGuests(new ArrayList<>());
            event.getGuests().add(event.getEventOwner());
        }

        var newevent = invite(event);
        newevent.setId(id);

        return eventRepository.save(newevent);
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

        event.setInvitations(invitations);

        var newevent = invite(event);

        return eventRepository.save(newevent);
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

        for (Task task : event.getTasks()) {
            task.setTaskState(TaskState.READY_TO_START);
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
        eventDto.setUsersCount(event.getGuests() != null ? event.getGuests().size() : 0);
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
                event.setReports(existingEvent.getReports());
                event.setTasks(existingEvent.getTasks());

                event.setInvitations(eventDTO.getInvitations() != null ? eventDTO.getInvitations()
                        .stream()
                        .map(invitation -> invitationService.convertToEntity(invitation))
                        .collect(Collectors.toList()) : null);


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
