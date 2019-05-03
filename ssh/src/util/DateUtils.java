package util;

import com.sun.istack.internal.NotNull;
import model.Semester;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.MinguoChronology;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final ZoneId TIME_ZONE_ID = ZoneId.of("Asia/Taipei");

//    private DateUtils() {
//    }

    public static Semester getCurrentSemester() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR) - 1911;
        int sem = 1;
        int month = cal.get(Calendar.MONTH) + 1;
        if(month < 8) year--;

        if (month < 8 && month > 1) {
            sem = 2;
        }

        return new Semester(year, sem);
    }

    public static String formatDate(Date date) {
        return formatDateAsMingo(date, "y/MM/dd");
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Date parseInput(String dateInput, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateInput);
    }

    private static final String DATE_INPUT_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm";

    public static String formatAsDateInput(Date date) {
        return formatDate(date, DATE_INPUT_FORMAT);
    }

    public static Date parseDateInput(String input) throws ParseException {
        return parseInput(input, DATE_INPUT_FORMAT);
    }

    public static String formatAsDateTimeInput(Date date) {
        return formatDate(date, DATETIME_INPUT_FORMAT);
    }

    public static Date parseDateTimeInput(String input) throws ParseException {
        return parseInput(input, DATETIME_INPUT_FORMAT);
    }

    public static String formatDateTime(Date date) {
        return formatDateAsMingo(date, "y/MM/dd HH:mm");
    }

    public static String joinDate(Date start, Date end, String dim) {
        return formatDateTime(start) + dim + formatDateTime(end);
    }

    public static String formatDateAsMingo(Date date, String format) {
        if (date == null) return "";

        LocalDateTime localDate = dateToLocalDateTime(date);
        Chronology chrono = MinguoChronology.INSTANCE;
        ChronoLocalDateTime cDate = chrono.localDateTime(localDate);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        return dateFormatter.format(cDate);
    }

    private static LocalDateTime dateToLocalDateTime(@NotNull final Date date) {
        Instant instant = new Date(date.getTime()).toInstant();
        return LocalDateTime.ofInstant(instant, TIME_ZONE_ID);
    }
}
