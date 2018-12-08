package gr.tei.erasmus.pp.eventmate.backend;

import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.services.EventService;
import gr.tei.erasmus.pp.eventmate.backend.services.UserService;
import org.kohsuke.randname.RandomNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StartupBean {


    @Value("${dev.dummyGenerator.enabled}")
    private Boolean dummyGeneratorEnabled;

    @Value("${dev.dummyGenerator.size}")
    private Integer dummyRecordsCount;

    private RandomNameGenerator r = new RandomNameGenerator(500);

    private final EventRepository eventRepository;
    private final EventService eventService;

    private final UserService userService;


    @Autowired
    public StartupBean(EventRepository eventRepository,
                       EventService eventService,
                       UserService userService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {

        if (dummyGeneratorEnabled)
            generateDummies();

    }


    private void generateDummies() {
        for (int i = 0; i < dummyRecordsCount; i++) {
            eventRepository.save(eventService.generateDummy(r));
        }

        for (int i = 0; i < dummyRecordsCount; i++) {
            userService.register(new User(r.next(),r.next()+"@email","pass",null,0));
        }

        userService.register(new User("user","user@email","pass",null,0));
    }



}
