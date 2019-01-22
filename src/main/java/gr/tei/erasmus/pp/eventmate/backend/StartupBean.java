package gr.tei.erasmus.pp.eventmate.backend;

import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.enums.FileType;
import gr.tei.erasmus.pp.eventmate.backend.enums.TaskState;
import gr.tei.erasmus.pp.eventmate.backend.models.*;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.ChatService;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import gr.tei.erasmus.pp.eventmate.backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Component
public class StartupBean {


    @Value("${dev.dummyGenerator.enabled}")
    private Boolean dummyGeneratorEnabled;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {

        if (dummyGeneratorEnabled)
            generateDummyStructure();


        mock();

    }


    private void generateDummyStructure() {

//        User user1 = new User("user1", "user1@email", "pass", null, 0);
//        User user2 = new User("user2", "user2@email", "pass", null, 0);
//        User user3 = new User("user3", "user3@email", "pass", null, 0);
//        User user4 = new User("user4", "user4@email", "pass", null, 0);
//
//        User petra_user = new User("petra", "petra@email", "petra1", null, 0);
//        User petan_user = new User("petan", "petan@email", "petan1", null, 0);
//
//        petra_user.setPhoto(FileUtils.getFileBlob(new File("petra.jpg")));
//        petan_user.setPhoto(FileUtils.getFileBlob(new File("petan.jpg")));
//        user1.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//
//        userService.register(Arrays.asList(user1, user2, user3, user4, petra_user, petan_user));
//
//        Task task_petra = new Task(
//                "Petra task",
//                "Bar",
//                "Drink 10 rakia shots",
//                10L,
//                null,
//                petra_user,
//                TaskState.EDITABLE
//        );
//        Task task_petra2 = new Task(
//                "Rum shots",
//                "Bar",
//                "Drink 10 rum shots",
//                10L,
//                null
//        );
//
//        task_petra2.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//        task_petra2.setTaskState(TaskState.READY_TO_START);
//        task_petra2.setTaskOwner(petra_user);
//        task_petra2.setAssignees(Arrays.asList(user1, user2));
//
//        Task task_petra3 = new Task(
//                "Vodka shots",
//                "Bar",
//                "Drink 10 vodka shots",
//                10L,
//                null
//        );
//        task_petra3.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//        task_petra3.setTaskState(TaskState.IN_REVIEW);
//        task_petra3.setTaskOwner(petra_user);
//        task_petra3.setAssignees(Arrays.asList(user1, user2));
//
//        Event petra_event = new Event(
//                "Petra event",
//                new Date(),
//                "Pub 321",
//                new ArrayList<>(Arrays.asList(task_petra, task_petra2, task_petra3)),
//                EventState.EDITABLE,
//                new ArrayList<>());
//        petra_event.setEventOwner(petra_user);
//        petra_event.setGuests(new ArrayList<>(Arrays.asList(user1, user2, petan_user)));
//
//        eventService.createEvent(petra_user, petra_event);
//
//
//        Task task_petra_in_play = new Task(
//                "Rozehrany",
//                "Bar",
//                "Drink 10 vodka shots",
//                10L,
//                null
//        );
//        task_petra_in_play.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//        task_petra_in_play.setTaskState(TaskState.IN_PLAY);
//        task_petra_in_play.setTaskOwner(petan_user);
//        task_petra_in_play.setAssignees(Arrays.asList(user1, user2, petan_user));
//
//        var sbfa1 = new SubmissionFile();
//        sbfa1.setData(FileUtils.getFileBlob(new File("blank.jpg")));
//        sbfa1.setType(FileType.PHOTO);
//        sbfa1.setCreated(new Date());
//
//        var submissiona1 = new Submission();
//        submissiona1.setSubmitter(user2);
//        submissiona1.setContent(Collections.singletonList(sbfa1));
//
//        task_petra_in_play.setSubmissions(Arrays.asList(submissiona1));
//
//        Event petra_event_in_play = new Event(
//                "Hrajeme",
//                new Date(),
//                "Pub 321",
//                new ArrayList<>(Arrays.asList(task_petra_in_play)),
//                EventState.IN_PLAY,
//                new ArrayList<>());
//        petra_event_in_play.setEventOwner(petra_user);
//        petra_event_in_play.setGuests(new ArrayList<>(Arrays.asList(user1, user2, petan_user)));
//
//        eventService.createEvent(petra_user, petra_event_in_play);
//
//
//        Task task_petan = new Task(
//                "Petan task",
//                "Bar",
//                "Drink 10 rakia shots",
//                10L,
//                null,
//                petan_user,
//                TaskState.EDITABLE
//        );
//        Event petan_event = new Event(
//                "Petan event",
//                new Date(),
//                "Pub 321",
//                Collections.singletonList(task_petan),
//                EventState.EDITABLE,
//                new ArrayList<>());
//        petan_event.setEventOwner(petan_user);
//        petan_event.setGuests(new ArrayList<>(Arrays.asList(user1, user2, petra_user)));
//        eventService.createEvent(petan_user, petan_event);
//
//
//        Date date1 = DateUtils.toDate(LocalDateTime.now()
//                .plusDays(2)
//                .withHour(20)
//                .withHour(0));
//
//        Event event1 = new Event(
//                "Tims stag party",
//                date1,
//                "Pub 321",
//                new ArrayList<>(),
//                EventState.IN_PLAY,
//                new ArrayList<>());
//
//        event1.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//
//        event1.setEventOwner(user1);
//        event1.setGuests(Arrays.asList(user2, user3));
//
//        Invitation userInvitation = new Invitation(user4, null, InvitationType.NOTIFICATION, InvitationState.PENDING);
//        Invitation emailInvitation = new Invitation(null, "neregistrovany@user.cz", InvitationType.EMAIL, InvitationState.PENDING);
//
//        event1.setInvitations(Arrays.asList(userInvitation, emailInvitation));
//
//        Task task1 = new Task(
//                "Rakia shots",
//                "Bar",
//                "Drink 10 rakia shots",
//                10L,
//                null
//        );
//
//        task1.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//        task1.setTaskState(TaskState.IN_PLAY);
//        task1.setTaskOwner(user1);
//        task1.setAssignees(Arrays.asList(user2, user3));
//
//        var sbf = new SubmissionFile();
//        sbf.setName("photo proof");
//        sbf.setComment("comment");
//        sbf.setData(FileUtils.getFileBlob(new File("blank.jpg")));
//        sbf.setType(FileType.PHOTO);
//        sbf.setCreated(new Date());
//
//        var submission1 = new Submission();
//        submission1.setSubmitter(user2);
//        submission1.setContent(Arrays.asList(sbf));
//
//        task1.setSubmissions(Arrays.asList(submission1));
//
//        Task task2 = new Task(
//                "Rum shots",
//                "Bar",
//                "Drink 10 rum shots",
//                10L,
//                null
//        );
//
//        task2.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//        task2.setTaskState(TaskState.READY_TO_START);
//        task2.setTaskOwner(user1);
//        task2.setAssignees(Arrays.asList(user2, user3));
//
//
//        Task task3 = new Task(
//                "Vodka shots",
//                "Bar",
//                "Drink 10 vodka shots",
//                10L,
//                null
//        );
//        task3.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//        task3.setTaskState(TaskState.IN_REVIEW);
//        task3.setTaskOwner(user1);
//        task3.setAssignees(Arrays.asList(user2, user3));
//
//        Task task4 = new Task(
//                "Vodka shots",
//                "Bar",
//                "Drink 10 vodka shots",
//                10L,
//                null
//        );
//        task4.setPhoto(FileUtils.getFileBlob(new File("blank.jpg")));
//        task4.setTaskState(TaskState.FINISHED);
//        task4.setTaskOwner(user1);
//        task4.setAssignees(Arrays.asList(user2, user3));
//
//        var sbf2 = new SubmissionFile();
//        sbf2.setName("photo proof");
//        sbf2.setComment("comment");
//        sbf2.setData(FileUtils.getFileBlob(new File("blank.jpg")));
//        sbf2.setType(FileType.PHOTO);
//        sbf2.setCreated(new Date());
//
//        var sbf3 = new SubmissionFile();
//        sbf3.setName("photo proof 2");
//        sbf3.setComment("comment 2");
//        sbf3.setData(FileUtils.getFileBlob(new File("blank.jpg")));
//        sbf3.setType(FileType.PHOTO);
//        sbf3.setCreated(new Date());
//
//        var sbf4 = new SubmissionFile();
//        sbf4.setName("my proof");
//        sbf4.setComment("comment 2");
//        sbf4.setData(FileUtils.getFileBlob(new File("blank.jpg")));
//        sbf4.setType(FileType.PHOTO);
//        sbf4.setCreated(new Date());
//
//        var submission2 = new Submission();
//        submission2.setSubmitter(user2);
//        submission2.setContent(Arrays.asList(sbf2, sbf3));
//
//        var submission3 = new Submission();
//        submission3.setSubmitter(user3);
//        submission3.setContent(Arrays.asList(sbf4));
//
//        submission2.setEarnedPoints(5);
//        submission3.setEarnedPoints(8);
//
//        task4.setSubmissions(Arrays.asList(submission2, submission3));
//
//
//        event1.getTasks().addAll(Arrays.asList(task1, task2, task3, task4));
//
//
//        eventRepository.save(event1);
//
//// ------------------------
//
//        var event2 = new Event(
//                "Another finished event",
//                date1,
//                "Sport 321 !!",
//                new ArrayList<>(),
//                EventState.FINISHED,
//                new ArrayList<>());
//
//        event2.setEventOwner(user2);
//        event2.setGuests(Arrays.asList(user1, user3, user4));
//
//
//        var task5 = new Task(
//                "Kiss your crush",
//                "Ampfitear",
//                "Just kiss her",
//                50L,
//                null
//        );
//
//        task5.setTaskState(TaskState.FINISHED);
//
//        task5.setTaskOwner(user4);
//        task5.setAssignees(Collections.singletonList(user2));
//
//        var sbf5 = new SubmissionFile();
//        sbf5.setData(FileUtils.getFileBlob(new File("blank.jpg")));
//        sbf5.setType(FileType.PHOTO);
//        sbf5.setCreated(new Date());
//
//        var submission4 = new Submission();
//        submission4.setSubmitter(user2);
//        submission4.setContent(Collections.singletonList(sbf5));
//        submission4.setEarnedPoints(40);
//
//        task5.setSubmissions(Arrays.asList(submission4));
//
//        var report = new Report();
//        report.setComment("Prvni report, dost cool");
//        report.setCreated(new Date());
//        report.setName("Report 1");
//        report.setType(ReportType.CERTIFICATE);
//        report.setReportCreator(user1);
//        report.setPreview(FileUtils.getFileBlob(new File("blank.jpg")));
//        report.setContent(FileUtils.getFileBlob(new File("blank.jpg")));
//
//
//        var report2 = new Report();
//        report2.setComment("Report to be deleted");
//        report2.setCreated(new Date());
//        report2.setName("Report 2");
//        report2.setReportCreator(user3);
//        report2.setType(ReportType.CERTIFICATE);
//        report2.setPreview(FileUtils.getFileBlob(new File("blank.jpg")));
//        report2.setContent(FileUtils.getFileBlob(new File("blank.jpg")));
//
//        var report3 = new Report();
//        report3.setComment("Report to be deleted");
//        report3.setCreated(new Date());
//        report3.setName("Report 3");
//        report3.setReportCreator(user3);
//        report3.setType(ReportType.CERTIFICATE);
//        report3.setPreview(FileUtils.getFileBlob(new File("blank.jpg")));
//        report3.setContent(FileUtils.getFileBlob(new File("blank.jpg")));
//
//        var report4 = new Report();
//        report4.setComment("Report to be deleted");
//        report4.setCreated(new Date());
//        report4.setName("Report 4");
//        report4.setReportCreator(user3);
//        report4.setType(ReportType.CERTIFICATE);
//        report4.setPreview(FileUtils.getFileBlob(new File("blank.jpg")));
//        report4.setContent(FileUtils.getFileBlob(new File("blank.jpg")));
//
//
//        event2.setReports(List.of(report, report2, report3, report4));
//
//        eventRepository.save(event2);
//
//
//        chatService.sendMessage(new ChatMessage(user1, user2, "ahoj"));
//        chatService.sendMessage(new ChatMessage(user1, user2, "jak se mas?"));
//        chatService.sendMessage(new ChatMessage(user1, user2, "ses tam?"));
//
//        chatService.sendMessage(new ChatMessage(user1, user3, "user 2 me ignoruje"));
//        chatService.sendMessage(new ChatMessage(user1, user3, ":("));
//        chatService.sendMessage(new ChatMessage(user1, user3, "LAST"));
//
//        chatService.sendMessage(new ChatMessage(user3, user2, "odpovez mu vole"));
//        chatService.sendMessage(new ChatMessage(user3, user2, "LAST"));
//
//        chatService.sendMessage(new ChatMessage(user2, user1, "ahoj, jo jsem"));
//        chatService.sendMessage(new ChatMessage(user2, user1, "mam se fajn"));
//        chatService.sendMessage(new ChatMessage(user2, user1, "LAST"));
//
//        chatService.sendMessage(new ChatMessage(petra_user, petan_user, "ahoj"));
//        chatService.sendMessage(new ChatMessage(petra_user, petan_user, "jak se mas?"));
//        chatService.sendMessage(new ChatMessage(petra_user, petan_user, "ses tam?"));
//
//        chatService.sendMessage(new ChatMessage(petra_user, user3, "user 2 me ignoruje"));
//        chatService.sendMessage(new ChatMessage(petra_user, user3, ":("));
//
//        chatService.sendMessage(new ChatMessage(petan_user, petra_user, "ahoj, jo jsem OK"));
//
//        chatService.sendMessage(new ChatMessage(user2, petra_user, "PARTYYYYYYYYYYY"));
//        chatService.sendMessage(new ChatMessage(user2, petra_user, "you comming?"));
//        chatService.sendMessage(new ChatMessage(user2, petra_user, "HURRY PU"));
//
//
//
//
//        Event event3 = new Event(
//                "Another event",
//                date1,
//                "Dorms",
//                new ArrayList<>(),
//                EventState.READY_TO_PLAY,
//                new ArrayList<>());
//
//        event3.setEventOwner(user1);
//        eventRepository.save(event3);
//
//        Event event4 = new Event(
//                "Goodbye Agnes",
//                date1,
//                "Dorms",
//                new ArrayList<>(),
//                EventState.UNDER_EVALUATION,
//                new ArrayList<>());
//
//        event4.setEventOwner(user1);
//        eventRepository.save(event4);
//
//        Event event5 = new Event(
//                "Country presentation",
//                date1,
//                "TEI",
//                new ArrayList<>(),
//                EventState.UNDER_EVALUATION,
//                new ArrayList<>());
//
//        event5.setEventOwner(user1);
//        eventRepository.save(event5);
//
//
//        createPromoEvent();
    }


    public  void createPromoEvent() {

        User tom = new User("Tom", "tom@email", "passpass", FileUtils.getFileBlob(new File("promo/photos/tom.png")), 30);
        User george = new User("George", "george@email", "passpass", FileUtils.getFileBlob(new File("promo/photos/george.png")), 150);
        User alice = new User("Alice", "alice@email", "passpass", FileUtils.getFileBlob(new File("promo/photos/alice.png")), 400);
        User maria = new User("Maria", "maria@email", "passpass", FileUtils.getFileBlob(new File("promo/photos/maria.png")), 20);
        User mario = new User("Mario", "mario@email", "passpass", FileUtils.getFileBlob(new File("promo/photos/mario.png")), 0);

        userService.register(Arrays.asList(tom,george,alice,maria,mario));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);


        var shoots_proof_photo = new SubmissionFile();
        shoots_proof_photo.setData(FileUtils.getFileBlob(new File("promo/shoots_proof.jpg")));
        shoots_proof_photo.setType(FileType.PHOTO);
        shoots_proof_photo.setCreated(new Date());
        shoots_proof_photo.setName("Look at it!");
        shoots_proof_photo.setComment("Almost did it 9/10");


        var shoots_proof = new Submission();
        shoots_proof.setSubmitter(tom);
        shoots_proof.setContent(Arrays.asList(shoots_proof_photo));
        shoots_proof.setEarnedPoints(8);

        Task stag_rakia_shots = new Task(
                "Rakia shots",
                "Meraki pub",
                "Drink 10 rakia shots in row",
                10L,
                Collections.singletonList(shoots_proof)
        );
        stag_rakia_shots.setPhoto(FileUtils.getFileBlob(new File("promo/panaky.jpg")));
        stag_rakia_shots.setTaskState(TaskState.FINISHED);
        stag_rakia_shots.setTaskOwner(george);
        stag_rakia_shots.setAssignees(Collections.singletonList(tom));



        var kiss_proof = new SubmissionFile();
        kiss_proof.setData(FileUtils.getFileBlob(new File("promo/kiss_proof.jpg")));
        kiss_proof.setType(FileType.PHOTO);
        kiss_proof.setCreated(new Date());
        kiss_proof.setName("Look at it!");
        kiss_proof.setComment("Yeah, I did.");

        var tom_kiss_submission = new Submission();
        tom_kiss_submission.setSubmitter(tom);
        tom_kiss_submission.setContent(Collections.singletonList(kiss_proof));
        tom_kiss_submission.setEarnedPoints(20);

        Task stag_kiss_bartender = new Task(
                "Kiss a bartender",
                "Meraki pub",
                "Tom, you always liked her.",
                20L,
                Collections.singletonList(tom_kiss_submission)
        );
        stag_kiss_bartender.setPhoto(FileUtils.getFileBlob(new File("promo/kiss.jpg")));
        stag_kiss_bartender.setTaskState(TaskState.FINISHED);
        stag_kiss_bartender.setTaskOwner(george);
        stag_kiss_bartender.setAssignees(Collections.singletonList(tom));



        Event stag_party = new Event(
                "Tom stag party",
                cal.getTime(),
                "Meraki pub",
                new ArrayList<>(Arrays.asList(stag_rakia_shots,stag_kiss_bartender)),
                EventState.FINISHED,
                new ArrayList<>());

        stag_party.setPhoto(FileUtils.getFileBlob(new File("promo/stag_party.png")));
        stag_party.setGuests(new ArrayList<>(Arrays.asList(tom,george,maria)));


        eventService.createEvent(george,stag_party);




        //messaging

        chatService.sendMessage(new ChatMessage(tom, george, "Hi George!"));
        chatService.sendMessage(new ChatMessage(tom, george, "How are you?"));
        chatService.sendMessage(new ChatMessage(george, tom, "I'm fine, and you?"));

    }

    public void mock(){
        User petan = new User("Petr Kalas", "petrkalas93@gmail.com", "passpass", null, 0);
        petan.setPhoto(FileUtils.getFileBlob(new File("event/petan.jpg")));

        User petra = new User("Petra Cendelínová", "cendpetra@gmail.com", "passpass", null, 0);
        petra.setPhoto(FileUtils.getFileBlob(new File("event/petra.jpg")));
        petra.setScore(14);

        User simon = new User("Simao Frade", "simaofrade5@gmail.com", "passpass", null, 0);
        simon.setPhoto(FileUtils.getFileBlob(new File("event/simon.png")));

        User filipe = new User("Filipe Silva", "filipsilva96@gmail.com", "passpass", null, 0);
        filipe.setPhoto(FileUtils.getFileBlob(new File("event/filipe.jpg")));

        User kelson = new User("Kelson Endembo", "ndembokelson@gmail.com", "passpass", null, 0);
        kelson.setPhoto(FileUtils.getFileBlob(new File("event/kelson.jpg")));

        User kamila = new User("Kamila Tul", "kamila.tul@seznam.cz", "passpass", null, 0);
        kamila.setPhoto(FileUtils.getFileBlob(new File("event/kamila.jpg")));

        User arturas = new User("Arturas Kliukas", "arturas@gmail.com", "passpass", null, 0);
        arturas.setPhoto(FileUtils.getFileBlob(new File("event/arturas.jpg")));

        User watna = new User("Watna Camala", "watnacamala1@gmail.com", "passpass", null, 0);
        watna.setPhoto(FileUtils.getFileBlob(new File("event/watna.jpg")));

        var all_users = Arrays.asList(petan, petra, simon, filipe, kelson, kamila,arturas,watna);
        userService.register(all_users);



        Event dparty = new Event(
                "Friday dorms party",
                new Date(1547830800000L),
                "TEI Dorms",
                new ArrayList<>(),
                EventState.FINISHED,
                new ArrayList<>());
        dparty.setEventOwner(petra);
        dparty.setPhoto(FileUtils.getFileBlob(new File("event/dorms.png")));
        dparty.setGuests(all_users);



        Task foodTask = new Task(
                "Prepare food",
                "Kitchen",
                "Prepare your fancy food",
                10L,
                new ArrayList<>()
        );
        foodTask.setPhoto(FileUtils.getFileBlob(new File("event/food_task.jpg")));
        foodTask.setTaskState(TaskState.FINISHED);
        foodTask.setTaskOwner(petra);
        foodTask.setAssignees(Arrays.asList(petra,petan,filipe,watna));


        var petra_bramboraky = new SubmissionFile();
        petra_bramboraky.setName("Potato pancakes");
        petra_bramboraky.setComment("Czech fat food. Good for drinking.");
        petra_bramboraky.setData(FileUtils.getFileBlob(new File("event/food2.jpg")));
        petra_bramboraky.setType(FileType.PHOTO);
        petra_bramboraky.setCreated(new Date(1547835524000L));
        var petra_food_submission = new Submission(Arrays.asList(petra_bramboraky),petra,9);


        var petan_bramboraky = new SubmissionFile();
        petan_bramboraky.setName("potato pancakes");
        petan_bramboraky.setComment("yeah I helped Petra");
        petan_bramboraky.setData(FileUtils.getFileBlob(new File("event/food4.jpg")));
        petan_bramboraky.setType(FileType.PHOTO);
        petan_bramboraky.setCreated(new Date(1547835764000L));
        var petan_food_submission = new Submission(Arrays.asList(petan_bramboraky),petan,7);


        var filipe_eggs = new SubmissionFile();
        filipe_eggs.setName("Scrambled eggs with sousages");
        filipe_eggs.setComment("");
        filipe_eggs.setData(FileUtils.getFileBlob(new File("event/eggs.jpg")));
        filipe_eggs.setType(FileType.PHOTO);
        filipe_eggs.setCreated(new Date(1547837264000L));
        var filipe_food_submission = new Submission(Arrays.asList(filipe_eggs),filipe,2);


        foodTask.setSubmissions(Arrays.asList(petan_food_submission, petra_food_submission,filipe_food_submission));




        Task orangeTask = new Task(
                "Bring a orange",
                "Cantina",
                "Bring one orange from dinner",
                5L,
                new ArrayList<>()
        );
        orangeTask.setPhoto(FileUtils.getFileBlob(new File("event/orange.png")));
        orangeTask.setTaskState(TaskState.FINISHED);
        orangeTask.setTaskOwner(petan);
        orangeTask.setAssignees(Arrays.asList(petra,kamila,arturas,kelson,watna,petan));

        var petra_orange = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/orange2.jpg")),
                "My orange",
                "",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var petra_orange_submission = new Submission(Arrays.asList(petra_orange),petra,5);

        var watna_orange = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/orange4.jpg")),
                "",
                "",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var watna_orange_submission = new Submission(Arrays.asList(watna_orange),watna,5);

        var arturas_orange = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/orange1.jpg")),
                "",
                "",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var arturas_orange_submission = new Submission(Arrays.asList(arturas_orange),arturas,5);

        var kamila_orange = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/orange3.jpg")),
                "Orange",
                "comment?",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var kamila_orange_submission = new Submission(Arrays.asList(kamila_orange),kamila,5);


        orangeTask.getSubmissions().addAll(Arrays.asList(petra_orange_submission,watna_orange_submission,arturas_orange_submission,kamila_orange_submission));




        Task crazy_task = new Task(
                "Do something crazy",
                "Dorms",
                "Just crazy guys",
                20L,
                new ArrayList<>()
        );
        crazy_task.setPhoto(FileUtils.getFileBlob(new File("event/orange.png")));
        crazy_task.setTaskState(TaskState.FINISHED);
        crazy_task.setTaskOwner(petra);
        crazy_task.setAssignees(Arrays.asList(filipe,arturas));

        var filipe_crazy = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/sit3.jpg")),
                "I sit on Arturas!!!",
                "",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var filipe_crazy_submission = new Submission(Arrays.asList(filipe_crazy),filipe,20);


        var arturas_crazy = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/sit2.jpg")),
                "no comment",
                "",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var arturas_crazy_submission = new Submission(Arrays.asList(arturas_crazy),arturas,20);

        crazy_task.getSubmissions().addAll(Arrays.asList(
                filipe_crazy_submission,
                arturas_crazy_submission
        ));




        Task alcohol_task = new Task(
                "Bring some drinks",
                "Dorms",
                "Wine, beer, anything..",
                10L,
                new ArrayList<>()
        );
        alcohol_task.setPhoto(FileUtils.getFileBlob(new File("event/orange.png")));
        alcohol_task.setTaskState(TaskState.FINISHED);
        alcohol_task.setTaskOwner(petra);
        alcohol_task.setAssignees(Arrays.asList(kelson,watna,arturas));

        var watna_alcohol = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/party_wine.png")),
                "Party wine",
                "",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var watna_alcohol_submission = new Submission(Arrays.asList(watna_alcohol),watna,10);


        var arturas_alcohol = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/wine3.jpg")),
                "hot wine guyys",
                "",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var arturas_alcohol_submission = new Submission(Arrays.asList(arturas_alcohol),arturas,20);

        alcohol_task.getSubmissions().addAll(Arrays.asList(
                watna_alcohol_submission,
                arturas_alcohol_submission
        ));






        Task game_task = new Task(
                "Make up some game",
                "Dorms",
                "for others to play",
                35L,
                new ArrayList<>()
        );
        game_task.setPhoto(FileUtils.getFileBlob(new File("event/orange.png")));
        game_task.setTaskState(TaskState.FINISHED);
        game_task.setTaskOwner(petra);
        game_task.setAssignees(Arrays.asList(kelson,watna,arturas,kamila,simon));

        var simon_game = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/beerpong2.jpg")),
                "Beerpong",
                "",
                FileType.PHOTO,
                new Date(1547835524000L)
        );
        var simon_game_submission = new Submission(Arrays.asList(simon_game),simon,35);


        var arturas_game = new SubmissionFile(
                FileUtils.getFileBlob(new File("event/orange_video.mp4")),
                "Game with a orange",
                "",
                FileType.VIDEO,
                new Date(1547835524000L)
        );
        var arturas_game_submission = new Submission(Arrays.asList(arturas_game),arturas,20);

        game_task.getSubmissions().addAll(Arrays.asList(
                simon_game_submission,
                arturas_game_submission
        ));







        dparty.getTasks().add(foodTask);
        dparty.getTasks().add(orangeTask);
        dparty.getTasks().add(crazy_task);
        dparty.getTasks().add(alcohol_task);
        dparty.getTasks().add(game_task);

        eventService.saveEvent(dparty);
    }

}
