package org.wso2.carbon.identity.auth.connector.entgra;

import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.common.IdentityConnectorConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EntgraConfigImpl implements IdentityConnectorConfig {

    public static final String ENABLE = "adaptive_authentication.entgra.enable";
    public static final String TOKEN_URL = "adaptive_authentication.entgra.token_url";
    public static final String DEVICE_INFO_URL = "adaptive_authentication.entgra.device_info_url";
    public static final String CLIENT_KEY = "adaptive_authentication.entgra.client_key";
    public static final String CLIENT_SECRET = "adaptive_authentication.entgra.client_secret";
    public static final String USERNAME = "adaptive_authentication.entgra.username";
    public static final String CREDENTIAL = "__secret__adaptive_authentication.entgra.password";

    public static final String DEFAULT_ENABLE = "true";
    public static final String DEFAULT_TOKEN_URL = "https://500.mgt.entgra.net/oauth/token";
    public static final String DEFAULT_DEVICE_INFO_URL = "https://500.gw.entgra.net/api/device-mgt/v1.0/devices/1.0.0";
    public static final String DEFAULT_CLIENT_KEY = "change-me";
    public static final String DEFAULT_CLIENT_SECRET = "change-me";
    public static final String DEFAULT_USERNAME = "change-me";
    public static final String DEFAULT_CREDENTIAL = "change-me";

    @Override
    public String getName() {

        return "entgra-config";
    }

    @Override
    public String getFriendlyName() {

        return "Entgra Configuration";
    }

    @Override
    public String getCategory() {

        return "Other Settings";
    }

    @Override
    public String getSubCategory() {

        return "DEFAULT";
    }

    @Override
    public int getOrder() {

        return 11;
    }

    @Override
    public Map<String, String> getPropertyNameMapping() {

        Map<String, String> mapping = new HashMap<>();

        mapping.put(ENABLE, "Enable Entgra");
        mapping.put(TOKEN_URL, "Token URL");
        mapping.put(DEVICE_INFO_URL, "Device Information URL");
        mapping.put(CLIENT_KEY, "Client Key");
        mapping.put(CLIENT_SECRET, "Client Secret");
        mapping.put(USERNAME, "Username");
        mapping.put(CREDENTIAL, "Password");

        return mapping;
    }

    @Override
    public Map<String, String> getPropertyDescriptionMapping() {

        Map<String, String> mapping = new HashMap<>();

        mapping.put(ENABLE, "Enable Entgra Authentication");
        mapping.put(TOKEN_URL, "Entgra Token URL");
        mapping.put(DEVICE_INFO_URL, "Entgra Device Information URL");
        mapping.put(CLIENT_KEY, "Entgra Client Key");
        mapping.put(CLIENT_SECRET, "Entgra Client Secret");
        mapping.put(USERNAME, "Entgra Username");
        mapping.put(CREDENTIAL, "Entgra Password");

        return mapping;
    }

    @Override
    public String[] getPropertyNames() {

        List<String> properties = new ArrayList<>();
        properties.add(ENABLE);
        properties.add(TOKEN_URL);
        properties.add(DEVICE_INFO_URL);
        properties.add(CLIENT_KEY);
        properties.add(CLIENT_SECRET);
        properties.add(USERNAME);
        properties.add(CREDENTIAL);

        return properties.toArray(new String[0]);
    }

    @Override
    public Properties getDefaultPropertyValues(String s) throws IdentityGovernanceException {

        Map<String, String> defaultProperties = new HashMap<>();
        defaultProperties.put(ENABLE, DEFAULT_ENABLE);
        defaultProperties.put(TOKEN_URL, DEFAULT_TOKEN_URL);
        defaultProperties.put(DEVICE_INFO_URL, DEFAULT_DEVICE_INFO_URL);
        defaultProperties.put(CLIENT_KEY, DEFAULT_CLIENT_KEY);
        defaultProperties.put(CLIENT_SECRET, DEFAULT_CLIENT_SECRET);
        defaultProperties.put(USERNAME, DEFAULT_USERNAME);
        defaultProperties.put(CREDENTIAL, DEFAULT_CREDENTIAL);

        Properties properties = new Properties();
        properties.putAll(defaultProperties);
        return properties;
    }

    @Override
    public Map<String, String> getDefaultPropertyValues(String[] strings, String s) throws IdentityGovernanceException {

        return null;
    }


}
