package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.utils.DateUtils;
import org.kohsuke.randname.RandomNameGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@Service
public class EventService {

    public Event generateDummy(RandomNameGenerator r) {
        Date randomDate = DateUtils.getRandomDateBetween(
                DateUtils.toDate(LocalDate.now().minusDays(5)),
                DateUtils.toDate(LocalDate.now().plusDays(5)));

        EventState eventState;
        boolean randomBoolean = new Random().nextBoolean();
        if (randomDate.getTime() > new Date().getTime()) {
            if (randomBoolean)
                eventState = EventState.EDITABLE;
            else
                eventState = EventState.READY_TO_PLAY;

        } else {
            if (randomBoolean)
                eventState = EventState.UNDER_EVALUATION;
            else
                eventState = EventState.FINISHED;

        }

        return new Event(
                r.next(),
                randomDate,
                r.next(),
                new ArrayList<>(),
                eventState,
                new ArrayList<>());
    }


}
