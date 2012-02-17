package com.stephenhitchner.common.utils;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateTimeUtils
{

    protected static final String DATE_PATTERN      = "yyyy-MM-dd";
    protected static final String LONG_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    public enum DayOfWeek {
        SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7);

        private final int                            value;

        private static final Map<Integer, DayOfWeek> lookup = new HashMap<Integer, DayOfWeek>();

        static {
            for (final DayOfWeek dow : EnumSet.allOf(DayOfWeek.class)) {
                lookup.put(dow.value, dow);
            }
        }

        public static DayOfWeek valueOf(final int value) {
            return lookup.get(value);
        }

        private DayOfWeek(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static DateTime parseDateTime(final String dateString) {
        final DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_PATTERN);
        final DateTime dateTime = fmt.parseDateTime(dateString);
        return dateTime;
    }

    public static String formatDateTime(final DateTime dt) {
        final DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_PATTERN);
        final String ret = fmt.print(dt);
        return ret;
    }

    public static DateTime parseLongDateTime(final String dateString) {
        final DateTimeFormatter fmt = DateTimeFormat.forPattern(LONG_DATE_PATTERN);
        final DateTime dateTime = fmt.parseDateTime(dateString);
        return dateTime;
    }

    public static String formatLongDateTime(final DateTime dt) {
        final DateTimeFormatter fmt = DateTimeFormat.forPattern(LONG_DATE_PATTERN);
        final String ret = fmt.print(dt);
        return ret;
    }

    public static Calendar getStandardCalendar() {
        final Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.setMinimalDaysInFirstWeek(3);
        return cal;
    }

    public static int getAmazonStandardWeekOfYear(final DateTime dt) {
        final Calendar cal = DateTimeUtils.getStandardCalendar();
        cal.setTime(dt.toDate());
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getAmazonStandardMonthOfYear(final DateTime dt) {
        final Calendar cal = DateTimeUtils.getStandardCalendar();
        cal.setTime(dt.toDate());
        return cal.get(Calendar.MONTH) + 1;
    }

    public static DayOfWeek getStandardDayOfWeek(DateTime currentDt) {
        if (currentDt == null) {
            currentDt = new DateTime();
        }
        final int newDOW = currentDt.dayOfWeek().get() % 7 + 1;
        return DayOfWeek.valueOf(newDOW);
    }

    public static DateTime getEndOfPreviousWeek(DateTime currentDt) {
        if (currentDt == null) {
            currentDt = new DateTime();
        }
        final DayOfWeek currentDOW = getStandardDayOfWeek(currentDt);
        return currentDt.toDateMidnight().minusDays(currentDOW.value).toDateTime();
    }

    public static DateTime getStartOfCurrentWeek(DateTime currentDt) {
        if (currentDt == null) {
            currentDt = new DateTime();
        }
        final DayOfWeek currentDOW = getStandardDayOfWeek(currentDt);
        return currentDt.toDateMidnight().minusDays(currentDOW.value - 1).toDateTime();
    }

    public static DateTime getEndOfCurrentWeek(DateTime currentDt) {
        if (currentDt == null) {
            currentDt = new DateTime();
        }
        final DayOfWeek currentDOW = getStandardDayOfWeek(currentDt);
        return currentDt.toDateMidnight().plusDays(7 - currentDOW.value).toDateTime();
    }

    public static DateTime getEndOfPreviousMonth(DateTime currentDt) {
        if (currentDt == null) {
            currentDt = new DateTime();
        }
        final DateMidnight dm = currentDt.toDateMidnight().minusMonths(1);
        final int daysInMonth = dm.dayOfMonth().getMaximumValue();
        return dm.withDayOfMonth(daysInMonth).toDateTime();
    }

    public static DateTime getEndOfCurrentMonth(DateTime currentDt) {
        if (currentDt == null) {
            currentDt = new DateTime();
        }
        final int daysInMonth = currentDt.dayOfMonth().getMaximumValue();
        return currentDt.toDateMidnight().withDayOfMonth(daysInMonth).toDateTime();
    }
}
