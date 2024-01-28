## Entgra adaptive script documentation

### Adaptive script parameters description

`getDeviceInfoEntgra` function is used to verify if the current device is registered under the current logged-in user 
and returns the following parameters with data.

| No  | Parameter     | Description                                                          | Sample values                                                                    |
|-----|---------------|----------------------------------------------------------------------|----------------------------------------------------------------------------------|
| 1   | `IS_DEV_MODE` | A string, indicate whether the development mode is enabled           | `true` - Development mode is enabled <br> `false` - Development mode is disabled |
| 2   | `IS_ADB`      | A string, indicate whether the Android Debug Bridge (ADB) is enabled | `true` - ADB mode is enabled <br> `false` - ADB mode is disabled                 | 
| 3   | `ROOTED`      | A string, indicate whether the device is rooted                      | `true` - Device is rooted <br> `false` - Device is not rooted                    |

### getDeviceInfoEntgra method error codes description
`getDeviceInfoEntgra` returns the following error codes on `onFail` scenario.

| No  | Error Code                               | Description                                                                         | 
|-----|------------------------------------------|-------------------------------------------------------------------------------------|
| 1   | `DEVICE_NOT_ENROLLED`                    | Mobile device is not enrolled in the Entgra server.                                 |
| 2   | `DEVICE_NOT_ENROLLED_UNDER_CURRENT_USER` | Mobile device is enrolled in the Entgra server but not under the <br/>current user. | 
| 3   | `ACCESS_DENIED`                          | For other error scenarios.                                                          |

### Sample adaptive authentication script

```JS
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

[Back to Previous Page](../README.md)
