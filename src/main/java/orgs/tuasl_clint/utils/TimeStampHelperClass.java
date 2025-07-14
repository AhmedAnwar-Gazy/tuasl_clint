package orgs.tuasl_clint.utils;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class TimeStampHelperClass {
    public static String formatTimeLeft(Timestamp timestamp) {
        if (timestamp == null) {
            return "N/A";
        }

        Instant now = Instant.now();
        Instant past = timestamp.toInstant();
        Duration duration = Duration.between(past, now);

        long seconds = duration.getSeconds();

        // If same day, return time only
        if (isSameDay(past, now)) {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a")
                    .withZone(ZoneId.systemDefault());
            return timeFormat.format(past);
        }

        // Calculate time units
        long days = ChronoUnit.DAYS.between(past, now);
        long months = ChronoUnit.MONTHS.between(
                past.atZone(ZoneId.systemDefault()).toLocalDate(),
                now.atZone(ZoneId.systemDefault()).toLocalDate()
        );
        long years = ChronoUnit.YEARS.between(
                past.atZone(ZoneId.systemDefault()).toLocalDate(),
                now.atZone(ZoneId.systemDefault()).toLocalDate()
        );

        if (years > 0) {
            return years + (years == 1 ? " Year" : " Years");
        } else if (months > 0) {
            return months + (months == 1 ? " Month" : " Months");
        } else if (days > 0) {
            return days + (days == 1 ? " Day" : " Days");
        } else {
            // Shouldn't reach here because same day is handled above
            return "Today";
        }
    }
    public static Timestamp timeNow(){
        return new Timestamp(new Date().getTime());
    }


    private static boolean isSameDay(Instant instant1, Instant instant2) {
        LocalDate date1 = instant1.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = instant2.atZone(ZoneId.systemDefault()).toLocalDate();
        return date1.equals(date2);
    }

}

