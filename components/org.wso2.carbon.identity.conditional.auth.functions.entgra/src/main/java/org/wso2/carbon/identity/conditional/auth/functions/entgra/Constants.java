/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.conditional.auth.functions.entgra;

public abstract class Constants {

    // Connector configurations constants.
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
