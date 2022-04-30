package id.luckynetwork.lyrams.lyralibs.core.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class DateUtils {

    private final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public String getFormattedDate() {
        return DATE_FORMAT.format(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(7L));
    }

    public String toDate(long millis) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss EEE, dd/MM/yyyy Z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return formatter.format(calendar.getTime());
    }

    public static long toTicks(long amount, TimeUnit unit) {
        try {
            return unit.toMillis(amount) / 50;
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

}
