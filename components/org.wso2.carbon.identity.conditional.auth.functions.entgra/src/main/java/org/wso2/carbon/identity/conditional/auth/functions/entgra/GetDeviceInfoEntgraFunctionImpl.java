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

import java.io.*;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

import static org.apache.http.HttpHeaders.*;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_SUCCESS;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_FAIL;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_TIMEOUT;

/**
 * Implementation of the {@link GetDeviceInfoEntgraFunction}
 */
public class GetDeviceInfoEntgraFunctionImpl implements GetDeviceInfoEntgraFunction {

    private static final Log LOG = LogFactory.getLog(GetDeviceInfoEntgraFunctionImpl.class);
    private CloseableHttpClient client;

    public GetDeviceInfoEntgraFunctionImpl() {
        super();
        // Configure Http Client
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
    public void getDeviceInfoEntgra(JsAuthenticationContext context, String platformOS, String deviceID, Map<String, Object> eventHandlers) throws EntgraConnectorException {

        try {
            JsAuthenticatedUser user = Util.getUser(context);
            String tenantDomain = user == null ? "carbon.super" : user.getWrapped().getTenantDomain();

            // Getting connector configurations
            String clientKey = CommonUtils.getConnectorConfig(Constants.CLIENT_KEY, tenantDomain);
            String clientSecret = CommonUtils.getConnectorConfig(Constants.CLIENT_SECRET, tenantDomain);
            String tokenURL = CommonUtils.getConnectorConfig(Constants.TOKEN_URL, tenantDomain);
            String deviceInfoBaseURL = CommonUtils.getConnectorConfig(Constants.DEVICE_INFO_URL, tenantDomain);
            String username = CommonUtils.getConnectorConfig(Constants.USERNAME, tenantDomain);
            String credential = CommonUtils.getConnectorConfig(Constants.CREDENTIAL, tenantDomain);
            String deviceInfoURL = deviceInfoBaseURL + "/" + platformOS + "/" + deviceID;

            AsyncProcess asyncProcess = new AsyncProcess((authenticationContext, asyncReturn) -> {
                String outcome;
                JSONObject response = null;

                try {
                    HttpPost tokenRequest = getTokenRequest(tokenURL, clientKey, clientSecret, username, credential);

                    try (CloseableHttpResponse tResponse = client.execute(tokenRequest)) {
                        int tokenResponseCode = tResponse.getStatusLine().getStatusCode();

                        if (tokenResponseCode >= 200 && tokenResponseCode < 300) {
                            String tJsonString = EntityUtils.toString(tResponse.getEntity());
                            JSONParser parser = new JSONParser();
                            JSONObject jsonTokenResponse = (JSONObject) parser.parse(tJsonString);
                            String accessToken = (String) jsonTokenResponse.get(Constants.ACCESS_TOKEN);

                            HttpGet deviceInfoRequest = getDeviceInfoRequest(deviceInfoURL, accessToken);

                            try (CloseableHttpResponse dResponse = client.execute(deviceInfoRequest)) {
                                int dResponseCode = dResponse.getStatusLine().getStatusCode();

                                if (dResponseCode >= 200 && dResponseCode < 300) {
                                    String dJsonString = EntityUtils.toString(tResponse.getEntity());
                                    JSONObject jsonDeviceInfoResponse = (JSONObject) parser.parse(dJsonString);
                                    outcome = OUTCOME_SUCCESS;

                                } else {

                                    LOG.error("Error while fetching device information from Entgra Server. Response code: " + dResponseCode);
                                    outcome = OUTCOME_FAIL;
                                }
                            }
                        } else {
                            LOG.error("Error while requesting access token from Entgra Server. Response code: " + tokenResponseCode);
                            outcome = OUTCOME_FAIL;
                        }
                    } catch (IllegalArgumentException e) {
                        LOG.error("Invalid Url: " + tokenURL, e);
                        outcome = OUTCOME_FAIL;
                    } catch (ConnectTimeoutException e) {
                        LOG.error("Error while waiting to connect to " + tokenURL, e);
                        outcome = OUTCOME_TIMEOUT;
                    } catch (SocketTimeoutException e) {
                        LOG.error("Error while waiting for data from " + tokenURL, e);
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

                asyncReturn.accept(authenticationContext, response != null ? response : Collections.emptyMap(), outcome);
            });
            JsGraphBuilder.addLongWaitProcess(asyncProcess, eventHandlers);

        } catch (IdentityEventException e) {
            throw new EntgraConnectorException("Can not retrieve configurations from tenant.", e);
        }

    }

    /**
     * Return http request for authorization token
     *
     * @param tokenURL     Token endpoint of Entgra IoT server
     * @param clientKey    Client key given by SP of Entgra IoT server
     * @param clientSecret Client secret given by SP of Enthra IoT server
     * @param username     Entgra IoT server's admin account username
     * @param credential   Entgra IoT server's admin account password
     * @return HttpPost request
     */
    private HttpPost getTokenRequest(String tokenURL, String clientKey, String clientSecret, String username, String credential) {
        HttpPost request = new HttpPost(tokenURL);

        // Creating basic authorization header value
        String basicAuthString = clientKey + ":" + clientSecret;
        String tokenRequestAuthorizationHeader = "Basic " + Base64.getEncoder().encodeToString(basicAuthString.getBytes(StandardCharsets.UTF_8));

        // Setting request headers for token request
        request.setHeader(CONTENT_TYPE, Constants.TYPE_APPLICATION_FORM_URLENCODED);
        request.setHeader(AUTHORIZATION, tokenRequestAuthorizationHeader);

        // Setting request body for the token request
        List<NameValuePair> tokenRequestPayload = new ArrayList<>();
        tokenRequestPayload.add(new BasicNameValuePair("grant_type", "password"));
        tokenRequestPayload.add(new BasicNameValuePair("username", username));
        tokenRequestPayload.add(new BasicNameValuePair("password", credential));
        tokenRequestPayload.add(new BasicNameValuePair("scope", "default perm:devices:details perm:devices:view"));

        request.setEntity(new UrlEncodedFormEntity(tokenRequestPayload, StandardCharsets.UTF_8));
        return request;
    }

    /**
     * Return http request for device information fetching
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

}
