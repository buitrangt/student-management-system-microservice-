package org.aibles.auth.utils.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {
    public static String formatLocalDateTime(LocalDateTime input, String pattern) {
        var datetimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return datetimeFormatter.format(input);
    }
}
