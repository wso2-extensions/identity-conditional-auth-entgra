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

package org.wso2.carbon.identity.conditional.auth.config.entgra;

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

    public static final String DEFAULT_ENABLE = "true";
    public static final String DEFAULT_TOKEN_URL = "https://500.mgt.entgra.net/oauth2/token";
    public static final String DEFAULT_DEVICE_INFO_URL = "https://500.gw.entgra.net/api/device-mgt/v1.0/devices/1.0.0";
    public static final String DEFAULT_CLIENT_KEY = "change-me";
    public static final String DEFAULT_CLIENT_SECRET = "change-me";

    private static Map<String, String> propertyNameMapping;
    private static Map<String, String> propertyDescriptionMapping;
    private static Map<String, String> defaultPropertyValuesMapping;
    private static List<String> propertyNames;
    private static Properties defaultPropertyValues;

    static {

        propertyNameMapping = new HashMap<>();
        propertyNameMapping.put(ENABLE, "Enable Entgra");
        propertyNameMapping.put(TOKEN_URL, "Token URL");
        propertyNameMapping.put(DEVICE_INFO_URL, "Device Information URL");
        propertyNameMapping.put(CLIENT_KEY, "Client Key");
        propertyNameMapping.put(CLIENT_SECRET, "Client Secret");

        propertyDescriptionMapping = new HashMap<>();
        propertyDescriptionMapping.put(ENABLE, "Enable Entgra Authentication");
        propertyDescriptionMapping.put(TOKEN_URL, "Entgra Token URL");
        propertyDescriptionMapping.put(DEVICE_INFO_URL, "Entgra Device Information URL");
        propertyDescriptionMapping.put(CLIENT_KEY, "Entgra Client Key");
        propertyDescriptionMapping.put(CLIENT_SECRET, "Entgra Client Secret");

        propertyNames = new ArrayList<>();
        propertyNames.add(ENABLE);
        propertyNames.add(TOKEN_URL);
        propertyNames.add(DEVICE_INFO_URL);
        propertyNames.add(CLIENT_KEY);
        propertyNames.add(CLIENT_SECRET);

        defaultPropertyValuesMapping = new HashMap<>();
        defaultPropertyValuesMapping.put(ENABLE, DEFAULT_ENABLE);
        defaultPropertyValuesMapping.put(TOKEN_URL, DEFAULT_TOKEN_URL);
        defaultPropertyValuesMapping.put(DEVICE_INFO_URL, DEFAULT_DEVICE_INFO_URL);
        defaultPropertyValuesMapping.put(CLIENT_KEY, DEFAULT_CLIENT_KEY);
        defaultPropertyValuesMapping.put(CLIENT_SECRET, DEFAULT_CLIENT_SECRET);

        defaultPropertyValues = new Properties();
        defaultPropertyValues.putAll(defaultPropertyValuesMapping);
    }

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

        return propertyNameMapping;
    }

    @Override
    public Map<String, String> getPropertyDescriptionMapping() {

        return propertyDescriptionMapping;
    }

    @Override
    public String[] getPropertyNames() {

        return propertyNames.toArray(new String[0]);
    }

    @Override
    public Properties getDefaultPropertyValues(String s) throws IdentityGovernanceException {

        return defaultPropertyValues;
    }

    @Override
    public Map<String, String> getDefaultPropertyValues(String[] strings, String s) throws IdentityGovernanceException {

        return null;
    }
}
