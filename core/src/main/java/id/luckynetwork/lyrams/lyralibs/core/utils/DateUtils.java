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

    /**
     * This function returns the current date in the format of yyyy-MM-dd HH:mm:ss.
     *
     * @return The current date and time in the format of "yyyy-MM-dd HH:mm:ss"
     */
    public String getFormattedDate() {
        return DATE_FORMAT.format(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(7L));
    }

    /**
     * Convert milliseconds to date
     *
     * @param millis The time in milliseconds.
     * @return A string representation of the date and time in the format HH:mm:ss EEE, dd/MM/yyyy Z
     */
    public String toDate(long millis) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss EEE, dd/MM/yyyy Z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return formatter.format(calendar.getTime());
    }

    /**
     * Converts the given amount of time in the given unit to ticks.
     *
     * @param amount The amount of time to convert
     * @param unit The unit of time to convert to ticks.
     * @return The amount of ticks in the given amount of time.
     */
    public static long toTicks(long amount, TimeUnit unit) {
        try {
            return unit.toMillis(amount) / 50;
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

}
