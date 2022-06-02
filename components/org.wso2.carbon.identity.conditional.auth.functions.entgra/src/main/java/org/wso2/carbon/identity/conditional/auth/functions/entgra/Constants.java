package org.wso2.carbon.identity.conditional.auth.functions.entgra;

public abstract class Constants {
    // Connector configurations constants
    public static final String TOKEN_URL = "adaptive_authentication.entgra.token_url";
    public static final String DEVICE_INFO_URL = "adaptive_authentication.entgra.device_info_url";
    public static final String CLIENT_KEY = "adaptive_authentication.entgra.client_key";
    public static final String CLIENT_SECRET = "adaptive_authentication.entgra.client_secret";

    public static final String TYPE_APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String TYPE_APPLICATION_JSON = "application/json";
    public static final String ACCESS_TOKEN = "access_token";

    public static enum AuthResponseErrorCode {
            ACCESS_DENIED,
            DEVICE_NOT_ENROLLED,
            DEVICE_NOT_ENROLLED_UNDER_CURRENT_USER,
            NETWORK_ERROR,
            INTERNAL_SERVER_ERROR
    }


}
