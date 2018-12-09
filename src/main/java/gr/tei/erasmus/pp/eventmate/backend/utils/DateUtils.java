package gr.tei.erasmus.pp.eventmate.backend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class DateUtils {

    public static Date getRandomDateBetween(Date from,Date due){
        return new Date(ThreadLocalRandom.current()
                .nextLong(from.getTime(), due.getTime()));
    }

    public static Date toDate(LocalDate localDate){
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


}
