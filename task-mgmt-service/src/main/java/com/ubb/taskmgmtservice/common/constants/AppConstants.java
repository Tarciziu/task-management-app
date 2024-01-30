package com.ubb.taskmgmtservice.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {
    // cookies
    public static final String X_USER_ID_HEADER_NAME = "X-User-Id";

    // jwt
    public static final String USER_ID_CLAIM_NAME = "userId";
    public static final String EMAIL_CLAIM_NAME = "email";
}
