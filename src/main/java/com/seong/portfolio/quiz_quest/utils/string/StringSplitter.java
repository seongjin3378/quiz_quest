package com.seong.portfolio.quiz_quest.utils.string;

public class StringSplitter {

    public static String[] splitAndGet(String input, String delimiter, int startIndex, int lastIndex) {
        // 문자열을 주어진 구분자로 분할
        String[] parts = input.split(delimiter);

        // 유효성 검사: 범위가 배열의 길이를 초과하지 않도록
        if (startIndex < 0 || lastIndex >= parts.length || startIndex > lastIndex) {
            throw new IllegalArgumentException("Invalid index range.");
        }

        // 지정된 범위의 요소를 포함하는 배열 생성
        String[] result = new String[lastIndex - startIndex + 1];
        System.arraycopy(parts, startIndex, result, 0, lastIndex - startIndex + 1);

        return result;
    }
}

