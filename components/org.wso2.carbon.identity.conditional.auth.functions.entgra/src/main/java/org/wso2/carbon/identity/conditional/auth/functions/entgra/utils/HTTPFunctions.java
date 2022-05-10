package org.wso2.carbon.identity.conditional.auth.functions.entgra.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.wso2.carbon.identity.conditional.auth.functions.common.utils.ConfigProvider;
import org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants;
import org.wso2.carbon.identity.conditional.auth.functions.entgra.GetDeviceInfoEntgraFunctionImpl;

import javax.net.ssl.SSLException;

import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_FAIL;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_SUCCESS;
import static org.wso2.carbon.identity.conditional.auth.functions.common.utils.Constants.OUTCOME_TIMEOUT;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class HTTPFunctions {
    public static final String TYPE_APPLICATION_JSON = "application/json";
    public static final String TYPE_APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final Log LOG = LogFactory.getLog(GetDeviceInfoEntgraFunctionImpl.class);

    private CloseableHttpClient client;

    public HTTPFunctions() {

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(ConfigProvider.getInstance().getConnectionTimeout())
                .setConnectionRequestTimeout(ConfigProvider.getInstance().getConnectionRequestTimeout())
                .setSocketTimeout(ConfigProvider.getInstance().getReadTimeout())
                .setRedirectsEnabled(false)
                .setRelativeRedirectsAllowed(false)
                .build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public Map<String, Object> httpPost(String epUrl, Map<String, Object> payloadData, Map<String, String> headers) {

        HttpPost request = new HttpPost(epUrl);

        if (headers != null) {
            // Check if "Content-Type" is in headers map else set APPLICATION_JSON as default "Content-Type"
            if (!headers.containsKey(CONTENT_TYPE)){
                request.setHeader(CONTENT_TYPE, TYPE_APPLICATION_JSON);
            }
            // Add headers to the request
            for (Map.Entry<String, String> dataElements : headers.entrySet()) {
                request.setHeader(dataElements.getKey(), dataElements.getValue());
            }
        } else {
            // If no headers were given, set Content-Type to "application/json"
            request.setHeader(CONTENT_TYPE, TYPE_APPLICATION_JSON);
        }
        request.setHeader(ACCEPT, TYPE_APPLICATION_JSON);

        /*
          For the header Content-Type : application/x-www-form-urlencoded
          Request body data should be UrlEncodedFormEntity
         */
        if (headers != null && TYPE_APPLICATION_FORM_URLENCODED.equals(headers.get(CONTENT_TYPE))) {
            List<NameValuePair> headersList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> dataElements : payloadData.entrySet()) {
                headersList.add(new BasicNameValuePair(dataElements.getKey(), (String) dataElements.getValue()));
            }
            request.setEntity(new UrlEncodedFormEntity(headersList, StandardCharsets.UTF_8));
        } else {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> dataElements : payloadData.entrySet()) {
                jsonObject.put(dataElements.getKey(), dataElements.getValue());
            }
            request.setEntity(new StringEntity(jsonObject.toJSONString(), StandardCharsets.UTF_8));
        }

        return executeHttpMethod(request);
    }

    protected Map<String, Object> executeHttpMethod(HttpUriRequest request) {
        JSONObject json = null;
        int responseCode;
        String outcome;
        String epUrl = null;

        if (request.getURI() != null) {
            epUrl = request.getURI().toString();
        }

        try (CloseableHttpResponse response = client.execute(request)) {
            responseCode = response.getStatusLine().getStatusCode();
            if (responseCode >= 200 && responseCode < 300) {
                outcome = OUTCOME_SUCCESS;
                String jsonString = EntityUtils.toString(response.getEntity());
                JSONParser parser = new JSONParser();
                json = (JSONObject) parser.parse(jsonString);
            } else {
                outcome = OUTCOME_FAIL;
            }

        } catch (IllegalArgumentException e) {
            LOG.error("Invalid Url: " + epUrl, e);
            outcome = OUTCOME_FAIL;
        } catch (ConnectTimeoutException e) {
            LOG.error("Error while waiting to connect to " + epUrl, e);
            outcome = OUTCOME_TIMEOUT;
        } catch (SocketTimeoutException e) {
            LOG.error("Error while waiting for data from " + epUrl, e);
            outcome = OUTCOME_TIMEOUT;
        } catch (IOException e) {
            LOG.error("Error while calling endpoint. ", e);
            outcome = OUTCOME_FAIL;
        } catch (ParseException e) {
            LOG.error("Error while parsing response. ", e);
            outcome = OUTCOME_FAIL;
        }

        Map<String, Object> output = new HashMap<String, Object>();
        output.put("json", json);
        output.put("outcome", outcome);

        return output;
    }
}
