package com.seong.portfolio.quiz_quest.utils.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    public static boolean isThisMonth(LocalDateTime dateTime) {
        // 현재 날짜와 시간을 가져옵니다.
        LocalDateTime now = LocalDateTime.now();

        // 주어진 dateTime의 월과 현재의 월을 비교합니다.
        return dateTime.getMonth() == now.getMonth() && dateTime.getYear() == now.getYear();
    }
    public static boolean isThisWeek(LocalDateTime dateTime) {
        LocalDateTime mondayOfThisWeek = getThisWeekMonday();
        LocalDateTime sundayOfThisWeek = getThisWeekSunday();

        boolean isEqualMondayOrAfter = dateTime.isAfter(mondayOfThisWeek) || dateTime.isEqual(mondayOfThisWeek);
        boolean isEqualSundayOrBefore = dateTime.isBefore(sundayOfThisWeek) || dateTime.isEqual(sundayOfThisWeek);

        return isEqualMondayOrAfter && isEqualSundayOrBefore;
    }

    public static boolean isThisYear(LocalDateTime dateTime) {
        return dateTime.getYear() == LocalDate.now().getYear();
    }
    private static LocalDateTime getThisWeekMonday()
    {
        LocalDate today = LocalDate.now();
        //현재 날짜가 월요일이면 현재 날짜를 반환하고, 아닐 경우 최근 월요일을 반환함
        LocalDate thisWeekMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        return thisWeekMonday.atStartOfDay();
    }

    private static LocalDateTime getThisWeekSunday()
    {
        LocalDate today = LocalDate.now();

        LocalDate thisWeekSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return thisWeekSunday.atStartOfDay();
    }

}
