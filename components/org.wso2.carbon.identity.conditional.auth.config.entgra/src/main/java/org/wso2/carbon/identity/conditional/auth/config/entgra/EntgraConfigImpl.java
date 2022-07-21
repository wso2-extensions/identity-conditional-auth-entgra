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

    private static Map<String, String> mapping;

    static {

        mapping = new HashMap<>();
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

        mapping.clear();
        mapping.put(ENABLE, "Enable Entgra");
        mapping.put(TOKEN_URL, "Token URL");
        mapping.put(DEVICE_INFO_URL, "Device Information URL");
        mapping.put(CLIENT_KEY, "Client Key");
        mapping.put(CLIENT_SECRET, "Client Secret");

        return mapping;
    }

    @Override
    public Map<String, String> getPropertyDescriptionMapping() {

        mapping.clear();
        mapping.put(ENABLE, "Enable Entgra Authentication");
        mapping.put(TOKEN_URL, "Entgra Token URL");
        mapping.put(DEVICE_INFO_URL, "Entgra Device Information URL");
        mapping.put(CLIENT_KEY, "Entgra Client Key");
        mapping.put(CLIENT_SECRET, "Entgra Client Secret");

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

        return properties.toArray(new String[0]);
    }

    @Override
    public Properties getDefaultPropertyValues(String s) throws IdentityGovernanceException {

        mapping.clear();
        mapping.put(ENABLE, DEFAULT_ENABLE);
        mapping.put(TOKEN_URL, DEFAULT_TOKEN_URL);
        mapping.put(DEVICE_INFO_URL, DEFAULT_DEVICE_INFO_URL);
        mapping.put(CLIENT_KEY, DEFAULT_CLIENT_KEY);
        mapping.put(CLIENT_SECRET, DEFAULT_CLIENT_SECRET);

        Properties properties = new Properties();
        properties.putAll(mapping);
        return properties;
    }

    @Override
    public Map<String, String> getDefaultPropertyValues(String[] strings, String s) throws IdentityGovernanceException {

        return null;
    }
}
