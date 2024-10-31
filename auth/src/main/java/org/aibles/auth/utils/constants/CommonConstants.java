package org.aibles.auth.utils.constants;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstants {
    public static class DateTimeConstants {
        public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    }

    public static final String ACCESS_TOKEN_HASH_KEY = "accessToken";
    public static final String REFRESH_TOKEN_HASH_KEY = "refreshToken";
    public static final String FAILED_PASSWORD_ATTEMPT_KEY = "failedPasswordAttempt";
    public static final String UNLOCK_TIME_KEY = "unlockTime";
    public static final Long FIVE_MINUTES = 5 * 60L;
    public static final Long TEN_MINUTES = 10 * 60L;
}
