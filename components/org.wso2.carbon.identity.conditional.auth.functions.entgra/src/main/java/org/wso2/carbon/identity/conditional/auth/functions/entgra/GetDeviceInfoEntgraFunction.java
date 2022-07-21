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

import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticationContext;
import org.wso2.carbon.identity.conditional.auth.functions.entgra.exception.EntgraConnectorException;

import java.util.Map;

/**
 * Function to get device information from Entgra IoT server.
 */
@FunctionalInterface
public interface GetDeviceInfoEntgraFunction {

    /**
     * Get device information from Entgra IoT server.
     * @param context       Context from authentication flow
     * @param osPlatform    Device's operating system
     * @param deviceID      Device identifier
     * @param eventHandlers Event handlers
     * @throws EntgraConnectorException When unable to retrieve tenant configuration
     */
    void getDeviceInfoEntgra(JsAuthenticationContext context, String osPlatform, String deviceID,
                             Map<String, Object> eventHandlers) throws EntgraConnectorException;
}
