package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.EventDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.UserDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
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

    private final EventRepository eventRepository;
    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final PermissionService permissionService;

    private final TaskService taskService;

    @Autowired
    public EventService(EventRepository eventRepository,
                        TaskRepository taskRepository,
                        ModelMapper modelMapper,
                        UserService userService,
                        PermissionService permissionService, TaskService taskService) {
        this.eventRepository = eventRepository;
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.permissionService = permissionService;
        this.taskService = taskService;
    }

    public Event createEvent(Event event) {

        return eventRepository.save(event);
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


    public EventDTO convertToDto(Event event) {

        EventDTO eventDto = modelMapper.map(event, EventDTO.class);
        eventDto.setTaskCount(event.getTasks() != null ? event.getTasks().size() : 0);
        eventDto.setReportsCount(event.getReports() != null ? event.getReports().size() : 0);

        User eventOwner =
                userService.getUserById(event.getPermissions()
                        .stream()
                        .filter(permission -> permission.getUserRole().equals(UserRole.EVENT_OWNER))
                        .collect(Collectors.toList()).get(0).getUserId());

        List<User> eventGuests = userService.getUsersById(event.getPermissions()
                .stream()
                .filter(permission -> permission.getUserRole().equals(UserRole.EVENT_GUEST))
                .map(Permission::getUserId)
                .collect(Collectors.toList()));


        eventDto.setUsersCount(event.getPermissions().size());
        eventDto.setEventOwner(userService.convertToDto(eventOwner));
        eventDto.setGuests(eventGuests
                .stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList()));

        eventDto.setTasks(event.getTasks()
                .stream()
                .map(taskService::convertToDto)
                .collect(Collectors.toList()));

        return eventDto;
    }

    private Event convertToEntity(EventDTO eventDTO) {

        // nema permission
        Event event = modelMapper.map(eventDTO, Event.class);


        if (eventDTO.getId() != null) {
            Optional<Event> oldEvent = eventRepository.findById(eventDTO.getId());
            if (oldEvent.isPresent()) {
                // if exists in db
                Event existingEvent = oldEvent.get();

                for (UserDTO guest : eventDTO.getGuests()) {
                    User user = userService.convertToEntity(guest);
                    if (!permissionService.hasPermission(user, existingEvent)) {
                        event.getPermissions().add(
                                new Permission(
                                        user.getId(),
                                        null,
                                        existingEvent.getId(),
                                        UserRole.EVENT_GUEST));
                    }
                }

                User owner = userService.convertToEntity(eventDTO.getEventOwner());
                if (!permissionService.hasPermissionRole(owner, existingEvent, UserRole.EVENT_OWNER)) {
                    event.getPermissions().add(
                            new Permission(
                                    owner.getId(),
                                    null,
                                    existingEvent.getId(),
                                    UserRole.EVENT_OWNER));
                }
            }
        }



        return event;
    }


}
