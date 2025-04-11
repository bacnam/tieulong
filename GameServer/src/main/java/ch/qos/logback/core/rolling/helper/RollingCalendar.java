package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.spi.ContextAwareBase;

import java.text.SimpleDateFormat;
import java.util.*;

public class RollingCalendar
        extends GregorianCalendar {
    static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
    private static final long serialVersionUID = -5937537740925066161L;
    PeriodicityType periodicityType = PeriodicityType.ERRONEOUS;

    public RollingCalendar() {
    }

    public RollingCalendar(TimeZone tz, Locale locale) {
        super(tz, locale);
    }

    public static int diffInMonths(long startTime, long endTime) {
        if (startTime > endTime)
            throw new IllegalArgumentException("startTime cannot be larger than endTime");
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(startTime);
        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(endTime);
        int yearDiff = endCal.get(1) - startCal.get(1);
        int monthDiff = endCal.get(2) - startCal.get(2);
        return yearDiff * 12 + monthDiff;
    }

    public void init(String datePattern) {
        this.periodicityType = computePeriodicityType(datePattern);
    }

    public PeriodicityType getPeriodicityType() {
        return this.periodicityType;
    }

    private void setPeriodicityType(PeriodicityType periodicityType) {
        this.periodicityType = periodicityType;
    }

    public long getNextTriggeringMillis(Date now) {
        return getNextTriggeringDate(now).getTime();
    }

    public PeriodicityType computePeriodicityType(String datePattern) {
        RollingCalendar rollingCalendar = new RollingCalendar(GMT_TIMEZONE, Locale.getDefault());

        Date epoch = new Date(0L);

        if (datePattern != null) {
            for (PeriodicityType i : PeriodicityType.VALID_ORDERED_LIST) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
                simpleDateFormat.setTimeZone(GMT_TIMEZONE);

                String r0 = simpleDateFormat.format(epoch);
                rollingCalendar.setPeriodicityType(i);

                Date next = new Date(rollingCalendar.getNextTriggeringMillis(epoch));
                String r1 = simpleDateFormat.format(next);

                if (r0 != null && r1 != null && !r0.equals(r1)) {
                    return i;
                }
            }
        }

        return PeriodicityType.ERRONEOUS;
    }

    public void printPeriodicity(ContextAwareBase cab) {
        switch (this.periodicityType) {
            case TOP_OF_MILLISECOND:
                cab.addInfo("Roll-over every millisecond.");
                return;

            case TOP_OF_SECOND:
                cab.addInfo("Roll-over every second.");
                return;

            case TOP_OF_MINUTE:
                cab.addInfo("Roll-over every minute.");
                return;

            case TOP_OF_HOUR:
                cab.addInfo("Roll-over at the top of every hour.");
                return;

            case HALF_DAY:
                cab.addInfo("Roll-over at midday and midnight.");
                return;

            case TOP_OF_DAY:
                cab.addInfo("Roll-over at midnight.");
                return;

            case TOP_OF_WEEK:
                cab.addInfo("Rollover at the start of week.");
                return;

            case TOP_OF_MONTH:
                cab.addInfo("Rollover at start of every month.");
                return;
        }

        cab.addInfo("Unknown periodicity.");
    }

    public long periodsElapsed(long start, long end) {
        if (start > end) {
            throw new IllegalArgumentException("Start cannot come before end");
        }
        long diff = end - start;
        switch (this.periodicityType) {

            case TOP_OF_MILLISECOND:
                return diff;
            case TOP_OF_SECOND:
                return diff / 1000L;
            case TOP_OF_MINUTE:
                return diff / 60000L;
            case TOP_OF_HOUR:
                return ((int) diff / 3600000);
            case TOP_OF_DAY:
                return diff / 86400000L;
            case TOP_OF_WEEK:
                return diff / 604800000L;
            case TOP_OF_MONTH:
                return diffInMonths(start, end);
        }
        throw new IllegalStateException("Unknown periodicity type.");
    }

    public Date getRelativeDate(Date now, int periods) {
        setTime(now);

        switch (this.periodicityType) {
            case TOP_OF_MILLISECOND:
                add(14, periods);

                return getTime();
            case TOP_OF_SECOND:
                set(14, 0);
                add(13, periods);
                return getTime();
            case TOP_OF_MINUTE:
                set(13, 0);
                set(14, 0);
                add(12, periods);
                return getTime();
            case TOP_OF_HOUR:
                set(12, 0);
                set(13, 0);
                set(14, 0);
                add(11, periods);
                return getTime();
            case TOP_OF_DAY:
                set(11, 0);
                set(12, 0);
                set(13, 0);
                set(14, 0);
                add(5, periods);
                return getTime();
            case TOP_OF_WEEK:
                set(7, getFirstDayOfWeek());
                set(11, 0);
                set(12, 0);
                set(13, 0);
                set(14, 0);
                add(3, periods);
                return getTime();
            case TOP_OF_MONTH:
                set(5, 1);
                set(11, 0);
                set(12, 0);
                set(13, 0);
                set(14, 0);
                add(2, periods);
                return getTime();
        }
        throw new IllegalStateException("Unknown periodicity type.");
    }

    public Date getNextTriggeringDate(Date now) {
        return getRelativeDate(now, 1);
    }
}

