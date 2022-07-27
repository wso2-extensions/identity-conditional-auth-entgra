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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.wso2.carbon.identity.application.authentication.framework.AsyncProcess;
import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.JsGraphBuilder;
import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticatedUser;
import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticationContext;
import org.wso2.carbon.identity.conditional.auth.functions.common.utils.CommonUtils;
import org.wso2.carbon.identity.conditional.auth.functions.common.utils.ConfigProvider;
import org.wso2.carbon.identity.conditional.auth.functions.entgra.exception.EntgraConnectorException;
import org.wso2.carbon.identity.event.IdentityEventException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_SUCCESS;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_FAIL;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_TIMEOUT;

/**
 * Implementation of the {@link GetDeviceInfoEntgraFunction}.
 */
public class GetDeviceInfoEntgraFunctionImpl implements GetDeviceInfoEntgraFunction {

    private static final Log LOG = LogFactory.getLog(GetDeviceInfoEntgraFunctionImpl.class);
    private CloseableHttpClient client;

    public GetDeviceInfoEntgraFunctionImpl() {

        // Configure http client.
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(ConfigProvider.getInstance().getConnectionTimeout())
                .setConnectionRequestTimeout(ConfigProvider.getInstance().getConnectionRequestTimeout())
                .setSocketTimeout(ConfigProvider.getInstance().getReadTimeout())
                .setRedirectsEnabled(false)
                .setRelativeRedirectsAllowed(false)
                .build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    }

    @Override
    public void getDeviceInfoEntgra(JsAuthenticationContext context, String platformOS, String deviceID,
                                    Map<String, Object> eventHandlers) throws EntgraConnectorException {

        try {
            JsAuthenticatedUser user = Util.getUser(context);
            String tenantDomain = user.getWrapped().getTenantDomain();
            String username = user.getWrapped().getUserName();

            // Getting connector configurations.
            String clientKey = CommonUtils.getConnectorConfig(Constants.CLIENT_KEY, tenantDomain);
            String clientSecret = CommonUtils.getConnectorConfig(Constants.CLIENT_SECRET, tenantDomain);
            String tokenURL = CommonUtils.getConnectorConfig(Constants.TOKEN_URL, tenantDomain);
            String deviceInfoBaseURL = CommonUtils.getConnectorConfig(Constants.DEVICE_INFO_URL, tenantDomain);

            String deviceInfoURL = deviceInfoBaseURL + "/" + platformOS + "/" + deviceID;

            AsyncProcess asyncProcess = new AsyncProcess((authenticationContext, asyncReturn) -> {
                String outcome;
                JSONObject response = null;

                try {
                    HttpPost tokenRequest = getTokenRequest(tokenURL, clientKey, clientSecret);

                    // For catching and logging errors.
                    String errorURL = tokenURL;
                    try (CloseableHttpResponse tResponse = client.execute(tokenRequest)) {
                        int tokenResponseCode = tResponse.getStatusLine().getStatusCode();

                        if (tokenResponseCode >= 200 && tokenResponseCode < 300) {
                            String tJsonString = EntityUtils.toString(tResponse.getEntity());
                            JSONParser parser = new JSONParser();
                            JSONObject jsonTokenResponse = (JSONObject) parser.parse(tJsonString);
                            String accessToken = (String) jsonTokenResponse.get(Constants.ACCESS_TOKEN);

                            HttpGet deviceInfoRequest = getDeviceInfoRequest(deviceInfoURL, accessToken);

                            tResponse.close();
                            try (CloseableHttpResponse dResponse = client.execute(deviceInfoRequest)) {
                                int dResponseCode = dResponse.getStatusLine().getStatusCode();

                                if (dResponseCode >= 200 && dResponseCode < 300) {
                                    String dJsonString = EntityUtils.toString(dResponse.getEntity());
                                    JSONObject jsonDeviceInfoResponse = (JSONObject) parser.parse(dJsonString);
                                    String enrolledUser = (String) ((JSONObject) jsonDeviceInfoResponse
                                            .get("enrolmentInfo")).get("owner");
                                    String enrollmentStatus = (String) ((JSONObject) jsonDeviceInfoResponse
                                            .get("enrolmentInfo")).get("status");

                                    // Check if the device is enrolled to current user.
                                    if ("REMOVED".equals(enrollmentStatus)) {
                                        outcome = OUTCOME_FAIL;
                                        response = getErrorJsonObject(
                                                Constants.AuthResponseErrorCode.DEVICE_NOT_ENROLLED,
                                                "Device is not recognized. Please register your device.");
                                    } else if (username.equalsIgnoreCase(enrolledUser)) {
                                        outcome = OUTCOME_SUCCESS;
                                        response = (JSONObject) ((JSONObject) jsonDeviceInfoResponse.get("deviceInfo"))
                                                .get("deviceDetailsMap");
                                    } else {
                                        outcome = OUTCOME_FAIL;
                                        response = getErrorJsonObject(
                                                Constants.AuthResponseErrorCode.DEVICE_NOT_ENROLLED_UNDER_CURRENT_USER,
                                                "Access is denied. Please contact your administrator.");
                                    }
                                } else {

                                    LOG.error("Error while fetching device information from Entgra Server. " +
                                            "Response code: " + dResponseCode);
                                    outcome = OUTCOME_FAIL;
                                }
                            } catch (Exception e) {
                                errorURL = deviceInfoURL;
                                throw e;
                            }

                        } else if (tokenResponseCode == 404) {
                            LOG.error("Error while requesting access token from Entgra Server. Response code: "
                                    + tokenResponseCode);
                            outcome = OUTCOME_FAIL;
                            response = getErrorJsonObject(Constants.AuthResponseErrorCode.DEVICE_NOT_ENROLLED,
                                    "Device is not recognized. Please register your device.");

                        } else {
                            LOG.error("Error while requesting access token from Entgra Server. Response code: "
                                    + tokenResponseCode);
                            outcome = OUTCOME_FAIL;
                        }
                    } catch (IllegalArgumentException e) {
                        LOG.error("Invalid Url: " + errorURL, e);
                        outcome = OUTCOME_FAIL;
                    } catch (ConnectTimeoutException e) {
                        LOG.error("Error while waiting to connect to " + errorURL, e);
                        outcome = OUTCOME_TIMEOUT;
                    } catch (SocketTimeoutException e) {
                        LOG.error("Error while waiting for data from " + errorURL, e);
                        outcome = OUTCOME_TIMEOUT;
                    } catch (IOException e) {
                        LOG.error("Error while calling endpoint. ", e);
                        outcome = OUTCOME_FAIL;
                    } catch (ParseException e) {
                        LOG.error("Error while parsing response. ", e);
                        outcome = OUTCOME_FAIL;
                    }
                } catch (Exception e) {
                    outcome = OUTCOME_FAIL;
                    LOG.error("Error while generating request.");
                }

                // If outcome fails and response is null, set error object as response.
                if (outcome.equals(OUTCOME_FAIL) && response == null) {
                    response = getErrorJsonObject(Constants.AuthResponseErrorCode.ACCESS_DENIED,
                            "Access is denied. Please contact your administrator.");
                }

                asyncReturn.accept(authenticationContext,
                        response != null ? response : Collections.emptyMap(), outcome);
            });
            JsGraphBuilder.addLongWaitProcess(asyncProcess, eventHandlers);
        } catch (IdentityEventException e) {
            throw new EntgraConnectorException("Can not retrieve configurations from tenant.", e);
        }
    }

    /**
     * Return http request for requesting authorization token.
     *
     * @param tokenURL     Token endpoint of Entgra IoT server
     * @param clientKey    Client key given by SP of Entgra IoT server
     * @param clientSecret Client secret given by SP of Enthra IoT server
     * @return HttpPost request
     */
    private HttpPost getTokenRequest(String tokenURL, String clientKey, String clientSecret) {

        HttpPost request = new HttpPost(tokenURL);

        // Creating basic authorization header value.
        String basicAuthString = clientKey + ":" + clientSecret;
        String tokenRequestAuthorizationHeader = "Basic " + Base64.getEncoder().encodeToString(basicAuthString
                .getBytes(StandardCharsets.UTF_8));

        // Setting request headers for token request.
        request.setHeader(CONTENT_TYPE, Constants.TYPE_APPLICATION_FORM_URLENCODED);
        request.setHeader(AUTHORIZATION, tokenRequestAuthorizationHeader);

        // Setting request body for the token request.
        List<NameValuePair> tokenRequestPayload = new ArrayList<>();
        tokenRequestPayload.add(new BasicNameValuePair("grant_type", "client_credentials"));
        tokenRequestPayload.add(new BasicNameValuePair("scope",
                "default perm:devices:details perm:devices:view"));
        request.setEntity(new UrlEncodedFormEntity(tokenRequestPayload, StandardCharsets.UTF_8));
        return request;
    }

    /**
     * Return http request for fetching device information.
     *
     * @param deviceInfoURL Device information fetching endpoint of Entgra IoT Server
     * @param accessToken   Access token received by the Entgra IoT server
     * @return HttpGet request
     */
    private HttpGet getDeviceInfoRequest(String deviceInfoURL, String accessToken) {

        HttpGet request = new HttpGet(deviceInfoURL);

        request.setHeader(ACCEPT, Constants.TYPE_APPLICATION_JSON);
        request.setHeader(AUTHORIZATION, "Bearer " + accessToken);
        return request;
    }

    /**
     * Return error json object.
     * @param errorCode
     * @param errorMessage
     * @return errorMap JSONObject
     */
    private JSONObject getErrorJsonObject(Constants.AuthResponseErrorCode errorCode, String errorMessage) {

        JSONObject errorMap = new JSONObject();
        errorMap.put("errorCode", errorCode);
        errorMap.put("errorMessage", errorMessage);
        return errorMap;
    }
}
