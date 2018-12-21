package gr.tei.erasmus.pp.eventmate.backend;

import gr.tei.erasmus.pp.eventmate.backend.enums.*;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import gr.tei.erasmus.pp.eventmate.backend.utils.DateUtils;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Component
public class StartupBean {


    @Value("${dev.dummyGenerator.enabled}")
    private Boolean dummyGeneratorEnabled;


    private final TaskRepository taskRepository;

    private final EventRepository eventRepository;
    private final EventService eventService;


    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public StartupBean(TaskRepository taskRepository,
                       EventRepository eventRepository,
                       EventService eventService,
                       UserService userService) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
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
                EventState.IN_PLAY,
                new ArrayList<>());

        event1.setEventOwner(user1);
        event1.setGuests(Arrays.asList(user2, user3));

        Invitation userInvitation = new Invitation(user4, null, InvitationType.NOTIFICATION, InvitationState.PENDING);
        Invitation emailInvitation = new Invitation(null, "neregistrovany@user.cz", InvitationType.EMAIL, InvitationState.PENDING);

        event1.setInvitations(Arrays.asList(userInvitation, emailInvitation));

        Task task1 = new Task(
                "Rakia shots",
                "Bar",
                "Drink 10 rakia shots",
                10L,
                null
        );

        task1.setTaskState(TaskState.IN_REVIEW);

        task1.setTaskOwner(user1);
        task1.setAssignees(Arrays.asList(user2, user3));


        var sbf = new SubmissionFile();
        sbf.setContent(FileUtils.getFileBlob(new File("blank.jpg")));
        sbf.setType(FileType.PHOTO);
        sbf.setCreated(new Date());

        var submission1 = new Submission();
        submission1.setSubmitter(user2);
        submission1.setContent(Arrays.asList(sbf));

        task1.setSubmissions(Arrays.asList(submission1));

        event1.getTasks().add(task1);


        eventRepository.save(event1);

    }


}
