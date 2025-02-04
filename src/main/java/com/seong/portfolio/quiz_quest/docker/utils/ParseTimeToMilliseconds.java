package com.seong.portfolio.quiz_quest.docker.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class ParseTimeToMilliseconds {

    public  static int execute(String timeString)
    {
        // 정규식으로 분과 초 추출
        String regex = "(\\d+)m(\\d+)(\\.\\d+)?s";
        Matcher matcher = compile(regex).matcher(timeString);

        if (matcher.find()) {
            int minutes = Integer.parseInt(matcher.group(1)); // 분
            int seconds = Integer.parseInt(matcher.group(2)); // 초
            String millisecondsPart = matcher.group(3); // 소수점 초 (있을 수도 있고 없을 수도 있음)

            // 총 밀리초 계산
            int totalMilliseconds =  minutes * 60 * 1000; // 분을 밀리초로 변환
            totalMilliseconds += seconds * 1000; // 초를 밀리초로 변환

            if (millisecondsPart != null) {
                // 소수점 초가 있는 경우
                int milliseconds = (int) (Double.parseDouble(millisecondsPart) * 1000);
                totalMilliseconds += milliseconds; // 소수점 초를 밀리초로 변환하여 추가
            }

            return totalMilliseconds;
        }

        throw new IllegalArgumentException("Invalid time format: " + timeString);
    }
}
