package com.svs.goodtimer;

import java.util.Locale;

/**
 * Created by Виталий on 28.11.2016.
 */

class ItemListOfActualTimers {
    private long timeInMillis;
    private int hours, minutes, seconds;
    private String description, timeInString;
    private static String formatHours = "%d ч ";
    private static String formatMinutes = "%d мин ";
    private static String formatSeconds = "%d сек";
    private static Locale locale = Locale.getDefault();

    public ItemListOfActualTimers(int hours, int minutes, int seconds, String description) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.description = description;
        this.timeInMillis = (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
        this.timeInString = (hours > 0 ? String.format(locale, formatHours, hours) : "") +
                (minutes > 0 ? String.format(locale, formatMinutes, minutes) : "") +
                (seconds > 0 ? String.format(locale, formatSeconds, seconds) : "");
    }

    public ItemListOfActualTimers(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.timeInMillis = (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
        this.timeInString = (hours > 0 ? String.format(locale, formatHours, hours) : "") +
                (minutes > 0 ? String.format(locale, formatMinutes, minutes) : "") +
                (seconds > 0 ? String.format(locale, formatSeconds, seconds) : "");
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeInString() {
        return timeInString;
    }
}
