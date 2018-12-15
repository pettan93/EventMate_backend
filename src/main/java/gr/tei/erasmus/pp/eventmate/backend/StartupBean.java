package gr.tei.erasmus.pp.eventmate.backend;

import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.enums.UserRole;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Permission;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.PermissionRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import gr.tei.erasmus.pp.eventmate.backend.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class StartupBean {


    @Value("${dev.dummyGenerator.enabled}")
    private Boolean dummyGeneratorEnabled;


    private final TaskRepository taskRepository;

    private final EventRepository eventRepository;
    private final EventService eventService;


    private final PermissionRepository permissionRepository;

    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public StartupBean(TaskRepository taskRepository, EventRepository eventRepository,
                       EventService eventService,
                       PermissionRepository permissionRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.permissionRepository = permissionRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {

        if (dummyGeneratorEnabled)
            generateDummyStructure();

    }


    private void generateDummyStructure() {

        User user1 = new User("user1", "user1@email", "pass", null, 0);
        User user2 = new User("user2", "user2@email", "pass", null, 0);
        User user3 = new User("user3", "user3@email", "pass", null, 0);
        User user4 = new User("user4", "user4@email", "pass", null, 0);

        userService.register(Arrays.asList(user1, user2, user3, user4));

        Date date1 = DateUtils.toDate(LocalDateTime.now()
                .plusDays(2)
                .withHour(20)
                .withHour(0));

        Event event1 = new Event(
                "Tims stag party",
                date1,
                "Pub 321",
                new ArrayList<>(),
                EventState.READY_TO_PLAY,
                new ArrayList<>());


        Task task1 = new Task(
                "Rakia shots",
                "Bar",
                "Drink 10 rakia shots",
                10L,
                null
        );


        event1.getTasks().add(task1);

        eventRepository.save(event1);


        // event permissions
        Permission ownerPermission = new Permission(user1.getId(), null, event1.getId(), UserRole.EVENT_OWNER);
        Permission guestPermission1 = new Permission(user2.getId(), null, event1.getId(), UserRole.EVENT_GUEST);
        Permission guestPermission2 = new Permission(user3.getId(), null, event1.getId(), UserRole.EVENT_GUEST);
        Permission guestPermission3 = new Permission(user4.getId(), null, event1.getId(), UserRole.EVENT_GUEST);
        List<Permission> eventPermissions = Arrays.asList(ownerPermission, guestPermission1, guestPermission2, guestPermission3);
        event1.setPermissions(new ArrayList<>(eventPermissions));

        // tasks permissions
        Permission taskOwnerPermission = new Permission(user1.getId(), task1.getId(), null, UserRole.TASK_OWNER);
        Permission asigneePermission1 = new Permission(user2.getId(), task1.getId(), null, UserRole.TASK_ASSIGNEE);
        Permission asigneePermission2 = new Permission(user3.getId(), task1.getId(), null, UserRole.TASK_ASSIGNEE);
        List<Permission> task1Permissions = Arrays.asList(taskOwnerPermission, asigneePermission1, asigneePermission2);
        task1.setPermissions(new ArrayList<>(task1Permissions));

        eventRepository.save(event1);

    }


}
