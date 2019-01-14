package gr.tei.erasmus.pp.eventmate.backend;

import gr.tei.erasmus.pp.eventmate.backend.enums.*;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.ChatService;
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
import java.util.*;

@Component
public class StartupBean {


    @Value("${dev.dummyGenerator.enabled}")
    private Boolean dummyGeneratorEnabled;


    private final TaskRepository taskRepository;

    private final EventRepository eventRepository;
    private final EventService eventService;

    @Autowired
    private ChatService chatService;


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

        user1.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));

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

        event1.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));

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

        task1.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));

        task1.setTaskState(TaskState.IN_REVIEW);

        task1.setTaskOwner(user1);
        task1.setAssignees(Arrays.asList(user2, user3));


        var sbf = new SubmissionFile();
        sbf.setName("photo proof");
        sbf.setComment("comment");
        sbf.setData(FileUtils.getFileBlob(new File("blank.jpg")));
        sbf.setType(FileType.PHOTO);
        sbf.setCreated(new Date());

        var submission1 = new Submission();
        submission1.setSubmitter(user2);
        submission1.setContent(Arrays.asList(sbf));
//
//        var submission2 = new Submission();
//        submission2.setSubmitter(user2);
//        submission2.setContent(Arrays.asList(sbf));
//
//        var submission3 = new Submission();
//        submission3.setSubmitter(user2);
//        submission3.setContent(Arrays.asList(sbf));

        task1.setSubmissions(Arrays.asList(submission1));

        event1.getTasks().add(task1);


        eventRepository.save(event1);

// ------------------------

        var event2 = new Event(
                "Another finished event",
                date1,
                "Sport 321 !!",
                new ArrayList<>(),
                EventState.FINISHED,
                new ArrayList<>());

        event2.setEventOwner(user2);
        event2.setGuests(Arrays.asList(user1, user3, user4));


        var task2 = new Task(
                "Kiss your crush",
                "Ampfitear",
                "Just kiss her",
                50L,
                null
        );

        task2.setTaskState(TaskState.FINISHED);

        task2.setTaskOwner(user4);
        task2.setAssignees(Collections.singletonList(user2));

        var sbf2 = new SubmissionFile();
        sbf2.setData(FileUtils.getFileBlob(new File("blank.jpg")));
        sbf2.setType(FileType.PHOTO);
        sbf2.setCreated(new Date());

        var submission4 = new Submission();
        submission4.setSubmitter(user2);
        submission4.setContent(Collections.singletonList(sbf2));
        submission4.setEarnedPoints(40);

        submission4.setContent(Collections.singletonList(sbf2));

        var report = new Report();
        report.setComment("Prvni report, dost cool");
        report.setCreated(new Date());
        report.setName("Report 1");
        report.setType(ReportType.CERTIFICATE);
        report.setReportCreator(user1);
        report.setPreview(FileUtils.getFileBlob(new File("blank.jpg")));
        report.setContent(FileUtils.getFileBlob(new File("blank.jpg")));


        var report2 = new Report();
        report2.setComment("Report to be deleted");
        report2.setCreated(new Date());
        report2.setName("Report 2");
        report2.setReportCreator(user3);
        report2.setType(ReportType.CERTIFICATE);
        report2.setPreview(FileUtils.getFileBlob(new File("blank.jpg")));
        report2.setContent(FileUtils.getFileBlob(new File("blank.jpg")));

        var report3 = new Report();
        report3.setComment("Report to be deleted");
        report3.setCreated(new Date());
        report3.setName("Report 3");
        report3.setReportCreator(user3);
        report3.setType(ReportType.CERTIFICATE);
        report3.setPreview(FileUtils.getFileBlob(new File("blank.jpg")));
        report3.setContent(FileUtils.getFileBlob(new File("blank.jpg")));

        var report4 = new Report();
        report4.setComment("Report to be deleted");
        report4.setCreated(new Date());
        report4.setName("Report 4");
        report4.setReportCreator(user3);
        report4.setType(ReportType.CERTIFICATE);
        report4.setPreview(FileUtils.getFileBlob(new File("blank.jpg")));
        report4.setContent(FileUtils.getFileBlob(new File("blank.jpg")));


        event2.setReports(List.of(report, report2, report3, report4));

        eventRepository.save(event2);


        chatService.sendMessage(new ChatMessage(user1,user2,"ahoj"));
        chatService.sendMessage(new ChatMessage(user1,user2,"jak se mas?"));
        chatService.sendMessage(new ChatMessage(user1,user2,"ses tam?"));

        chatService.sendMessage(new ChatMessage(user1,user3,"user 2 me ignoruje"));
        chatService.sendMessage(new ChatMessage(user1,user3,":("));
        chatService.sendMessage(new ChatMessage(user1,user3,"LAST"));

        chatService.sendMessage(new ChatMessage(user3,user2,"odpovez mu vole"));
        chatService.sendMessage(new ChatMessage(user3,user2,"LAST"));

        chatService.sendMessage(new ChatMessage(user2,user1,"ahoj, jo jsem"));
        chatService.sendMessage(new ChatMessage(user2,user1,"mam se fajn"));
        chatService.sendMessage(new ChatMessage(user2,user1,"LAST"));


        Event event3 = new Event(
                "Another event",
                date1,
                "Dorms",
                new ArrayList<>(),
                EventState.READY_TO_PLAY,
                new ArrayList<>());

        event3.setEventOwner(user1);
        eventRepository.save(event3);

        Event event4 = new Event(
                "Goodbye Agnes",
                date1,
                "Dorms",
                new ArrayList<>(),
                EventState.UNDER_EVALUATION,
                new ArrayList<>());

        event4.setEventOwner(user1);
        eventRepository.save(event4);

        Event event5 = new Event(
                "Country presentation",
                date1,
                "TEI",
                new ArrayList<>(),
                EventState.UNDER_EVALUATION,
                new ArrayList<>());

        event5.setEventOwner(user1);
        eventRepository.save(event5);

    }


}
