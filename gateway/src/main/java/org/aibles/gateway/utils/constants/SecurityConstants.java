package org.aibles.gateway.utils.constants;


public class SecurityConstants {

    public static final String LOGIN_ENDPOINT = "/api/v1/uses/login";
    public static final String REGISTER_ENDPOINT = "/api/v1/register";
    public static final String VERIFY_TOKEN_ENDPOINT = "/api/v1/user/verify-token";
    public static final String LOGOUT_ENDPOINT = "/api/v1/user/logout";

    public static final String[] PUBLIC_ENDPOINTS = {
            LOGIN_ENDPOINT,
            REGISTER_ENDPOINT,
            VERIFY_TOKEN_ENDPOINT,
            LOGOUT_ENDPOINT
    };
}
