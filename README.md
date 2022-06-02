# Entgra Connector and getDeviceInfoEntgra Conditional Auth Function

## 

## Building and Integrating the Extensions into the Identity Server.

1. Clone this project onto your computer or download it as a zip.
2. Build the OSGi bundle for the extension by running `mvn clean install`.
3. Copy the `org.wso2.carbon.identity.auth.connector.entgra-<versionNumber>-SNAPSHOT.jar` file from the `<PROJECT_HOME>/components/org.wso2.carbon.identity.auth.connector.entgra/target` directory  and `org.wso2.carbon.identity.conditional.auth.functions.entgra-<versionNumber>-SNAPSHOT.jar` file from the `<PROJECT_HOME>/components/org.wso2.carbon.identity.conditional.auth.functions.entgra/target` directory  and  insert into the `<IS_HOME>/repository/components/dropins/` directory in the WSO2 Identity Server.

## Usage

`getDeviceInfoEntgra` **function Interface**

```java
/**
     * Get device information from Entgra IoT server
     * @param context       Context from authentication flow
     * @param osPlatform    Device's operating system
     * @param deviceID      Device identifier
     * @param eventHandlers Event handlers
     * @throws EntgraConnectorException When unable to retrieve tenant configuration
     */
    void getDeviceInfoEntgra(JsAuthenticationContext context, String osPlatform, String deviceID, Map<String, Object> eventHandlers)
            throws EntgraConnectorException;
```

**Sample Adaptive Authentication Script**

```java
var onLoginRequest = function(context) {
    deviceID = context.request.params.device_id[0];
    platformOS = context.request.params.platformOS[0];
            executeStep(1, {
                onSuccess : function (context) {
                    getDeviceInfoEntgra(context, platformOS, deviceID, {
                       onSuccess : function (context, deviceInfo) {
                            if (deviceInfo) {
                                if(deviceInfo.IS_DEV_MODE == "true") {
                                    executeStep(2);
                                }
                            }
                           },
                        onFail : function (context, error) {
                            var errorMap = {
                                "errorCode": error.errorCode,
                                "errorMessage" : error.errorMessage
                            };
                            fail(errorMap);
                        }
                    });
                }
            }); 
};
```

**Device Information List**

| Key | Value |  |
| --- | --- | --- |
| IS_DEV_MODE | “true” | “false” |  Development mode is enabled |
| IS_ADB | “true” | “false” | Android Debug Bridge (ADB) is enabled |
| ROOTED | “true” | “false” | Device is rooted |