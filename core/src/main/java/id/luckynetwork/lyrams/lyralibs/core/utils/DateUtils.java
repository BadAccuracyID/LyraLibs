package id.luckynetwork.lyrams.lyralibs.core.utils;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class DateUtils {

    private SimpleDateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
    }

    /**
     * This function sets the date format to the format specified by the user.
     *
     * @param format The format of the date.
     */
    public void setDateFormat(String format) {
        dateFormat = new SimpleDateFormat(format);
    }

    /**
     * Set the time zone for the date format.
     *
     * @param timeZone The time zone to use for the date format.
     */
    public void setTimeZone(String timeZone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    /**
     * This function returns the current date
     *
     * @return The current date and time
     */
    public String getFormattedDate() {
        return toDate(System.currentTimeMillis());
    }

    /**
     * > It takes a long value representing milliseconds since the epoch, and returns a string representing the date in the
     * format "yyyy-MM-dd"
     *
     * @param millis The time in milliseconds to convert to a date.
     * @return A string representation of the date.
     */
    public String toDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return dateFormat.format(calendar.getTime());
    }

    /**
     * Converts the given amount of time in the given unit to ticks.
     *
     * @param amount   The amount of time to convert
     * @param timeUnit The unit of time to convert to ticks.
     * @return The amount of ticks in the given amount of time.
     */
    public static long toTicks(long amount, TimeUnit timeUnit) {
        try {
            return timeUnit.toMillis(amount) / 50;
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Convert a duration to milliseconds.
     *
     * @param duration The amount of time to convert
     * @param timeUnit The time unit of the duration parameter.
     * @return The duration in milliseconds.
     */
    public long durationToLong(int duration, TimeUnit timeUnit) {
        return timeUnit.toMillis(duration);
    }

    /**
     * It takes a string like "1h" or "2weeks" and converts it to a long value in milliseconds
     *
     * @param duration The duration string to convert.
     * @return The duration in milliseconds.
     */
    public long durationToLong(String duration) {
        String[] split = duration.split("(?<=[a-zA-Z])|(?=[a-zA-Z])");
        if (split.length <= 1 || duration.equals("-1")) {
            return -1L;
        }

        int amount = Integer.parseInt(split[0]);
        String unit = split[1];
        switch (unit.toUpperCase()) {
            case "S":
            case "SEC":
            case "SECS":
            case "SECOND":
            case "SECONDS":
                return durationToLong(amount, TimeUnit.SECONDS);
            case "MI":
            case "MIN":
            case "MINS":
            case "MINUTE":
            case "MINUTES":
                return durationToLong(amount, TimeUnit.MINUTES);
            case "H":
            case "HR":
            case "HRS":
            case "HOUR":
            case "HOURS":
                return durationToLong(amount, TimeUnit.HOURS);
            case "D":
            case "DAY":
            case "DAYS":
                return durationToLong(amount, TimeUnit.DAYS);
            case "W":
            case "WK":
            case "WEEK":
            case "WEEKS":
                return durationToLong(amount, TimeUnit.DAYS) * 7;
            case "M":
            case "MON":
            case "MONTH":
            case "MONTHS":
                return durationToLong(amount, TimeUnit.DAYS) * 30;
            default:
                return -1L;
        }
    }

    /**
     * It converts a duration in milliseconds to a human-readable string.
     *
     * @param duration The duration in milliseconds
     * @return A string that represents the duration in months, days, hours, minutes, and seconds.
     */
    public String durationToString(long duration) {
        long months = TimeUnit.MILLISECONDS.toDays(duration) / 30;
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));

        StringBuilder builder = new StringBuilder();
        if (duration < 0) {
            builder.append("-");

            duration = Math.abs(duration);

            months = TimeUnit.MILLISECONDS.toDays(duration) / 30;
            days = TimeUnit.MILLISECONDS.toDays(duration);
            hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
            minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
            seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        }

        if (months > 0) {
            builder.append(months).append(" month").append(months > 1 ? "s" : "").append(" ");
        }
        if (days > 0) {
            builder.append(days).append("d");
        }
        if (hours > 0) {
            builder.append(hours).append("h");
        }
        if (minutes > 0) {
            builder.append(minutes).append("m");
        }
        if (seconds > 0) {
            builder.append(seconds).append("s");
        }

        return builder.toString();
    }

}
