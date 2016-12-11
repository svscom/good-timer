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
    private static String divider = ";!;";
    private static Locale locale = Locale.getDefault();

    ItemListOfActualTimers(int hours, int minutes, int seconds, String description) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.description = description;
        this.timeInMillis = (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
        this.timeInString = (hours > 0 ? String.format(locale, formatHours, hours) : "") +
                (minutes > 0 ? String.format(locale, formatMinutes, minutes) : "") +
                (seconds > 0 ? String.format(locale, formatSeconds, seconds) : "");
    }

    ItemListOfActualTimers(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.description = "";
        this.timeInMillis = (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
        this.timeInString = (hours > 0 ? String.format(locale, formatHours, hours) : "") +
                (minutes > 0 ? String.format(locale, formatMinutes, minutes) : "") +
                (seconds > 0 ? String.format(locale, formatSeconds, seconds) : "");
    }

    long getTimeInMillis() {
        return timeInMillis;
    }

    String getDescription() {
        return description;
    }

    String getTimeInString() {
        return timeInString;
    }

    int getHours() {
        return hours;
    }

    int getMinutes() {
        return minutes;
    }

    int getSeconds() {
        return seconds;
    }

    void setHours(int hours) {
        this.hours = hours;
    }

    void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.valueOf(hours) + divider + minutes + divider + seconds + (description == null || description.equals("") ? "" : divider + description);
    }

    static ItemListOfActualTimers getItemFromString(String s) {
        if (s == null) return null;
        String[] data = s.split(divider);
        ItemListOfActualTimers item;
        if (data.length == 3) item = new ItemListOfActualTimers(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
        else item = new ItemListOfActualTimers(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[3]);
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemListOfActualTimers that = (ItemListOfActualTimers) o;

        if (hours != that.hours) return false;
        if (minutes != that.minutes) return false;
        if (seconds != that.seconds) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = hours;
        result = 31 * result + minutes;
        result = 31 * result + seconds;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
