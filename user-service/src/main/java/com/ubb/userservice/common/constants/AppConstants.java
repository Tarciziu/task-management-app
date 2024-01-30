package com.ubb.userservice.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {
    // cookies
    public static final String AUTH_COOKIE_NAME = "ubb-software";

    // jwt
    public static final String USER_ID_CLAIM_NAME = "userId";
    public static final String EMAIL_CLAIM_NAME = "email";
}
