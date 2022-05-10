package org.wso2.carbon.identity.conditional.auth.functions.entgra;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.wso2.carbon.identity.application.authentication.framework.AsyncProcess;
import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.JsGraphBuilder;
import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticatedUser;
import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticationContext;
import org.wso2.carbon.identity.conditional.auth.functions.common.utils.CommonUtils;

import org.wso2.carbon.identity.conditional.auth.functions.entgra.exception.EntgraConnectorException;
import org.wso2.carbon.identity.conditional.auth.functions.entgra.utils.CommonUtil;
import org.wso2.carbon.identity.conditional.auth.functions.entgra.utils.HTTPFunctions;
import org.wso2.carbon.identity.event.IdentityEventException;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;


/**
 * Implementation of the {@link GetDeviceInfoEntgraFunction}
 */
public class GetDeviceInfoEntgraFunctionImpl implements GetDeviceInfoEntgraFunction {

    private static final Log LOG = LogFactory.getLog(GetDeviceInfoEntgraFunctionImpl.class);
    HTTPFunctions httpFunctions;

    public GetDeviceInfoEntgraFunctionImpl() {
        super();
        httpFunctions = new HTTPFunctions();
    }

    @Override
    public void getDeviceInfoEntgra(JsAuthenticationContext context, String platformOS, String deviceID, Map<String, Object> eventHandlers) throws EntgraConnectorException {

        try {
            JsAuthenticatedUser user = CommonUtil.getUser(context);
            String tenantDomain = user.getWrapped().getTenantDomain();

            // Getting connector configurations
            String clientKey = CommonUtils.getConnectorConfig(Constants.CLIENT_KEY, tenantDomain);
            String clientSecret = CommonUtils.getConnectorConfig(Constants.CLIENT_SECRET, tenantDomain);
            String tokenURL = CommonUtils.getConnectorConfig(Constants.TOKEN_URL, tenantDomain);
            String deviceInfoURL = CommonUtils.getConnectorConfig(Constants.DEVICE_INFO_URL, tenantDomain);
            String username = CommonUtils.getConnectorConfig(Constants.USERNAME, tenantDomain);
            String credential = CommonUtils.getConnectorConfig(Constants.CREDENTIAL, tenantDomain);

            AsyncProcess asyncProcess = new AsyncProcess((authenticationContext, asyncReturn) -> {
                String outcome;
                JSONObject response = null;

                try {
                    Map<String, Object> tokenRequestPayload = new HashMap<String, Object>();
                    tokenRequestPayload.put("grant_type", "password");
                    tokenRequestPayload.put("username", username);
                    tokenRequestPayload.put("password", credential);
                    tokenRequestPayload.put("scope", "default perm:devices:details perm:devices:view");

                    Map<String, String> tokenRequestHeader = new HashMap<>();
                    String basicAuthString = clientKey + ":" + clientSecret;
                    String tokenRequestAuthorizationHeader = "Basic " + Base64.getEncoder().encodeToString(basicAuthString.getBytes(StandardCharsets.UTF_8));
                    tokenRequestHeader.put(AUTHORIZATION, tokenRequestAuthorizationHeader);
                    tokenRequestHeader.put(CONTENT_TYPE, HTTPFunctions.TYPE_APPLICATION_FORM_URLENCODED);

                    Map<String, Object> tokenResponse = httpFunctions.httpPost(tokenURL, tokenRequestPayload, tokenRequestHeader);

                    if (OUTCOME_SUCCESS.equals(tokenResponse.get("outcome"))) {

                        JSONObject tokenResponseJson = (JSONObject) tokenResponse.get("json");
                        String accessToken = (String) tokenResponseJson.get("access_token");

                        String deviceInformationURL = deviceInfoURL + "/" + platformOS + "/" + deviceID;


                        outcome = OUTCOME_SUCCESS;
                    } else {

                        outcome = (String) tokenResponse.get("outcome");
                    }

                } catch (Exception e) {
                    outcome = OUTCOME_FAIL;
                    // TODO: Error message
                    LOG.error("Error ");
                }

                asyncReturn.accept(authenticationContext, response != null ? response : Collections.emptyMap(), outcome);
            });
            JsGraphBuilder.addLongWaitProcess(asyncProcess, eventHandlers);

        } catch (IdentityEventException e) {
            throw new EntgraConnectorException("Can not retrieve configurations from tenant.", e);
        }

    }

}
