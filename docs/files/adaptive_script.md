## Entgra adaptive script documentation

### Adaptive script parameters description

`getDeviceInfoEntgra` function is used to verify if the current device registered under the current logged-in user 
and returns the following parameters with data.


| No  | Parameter     | Descriptoin                                                          | Sample values                                                                    |
|-----|---------------|----------------------------------------------------------------------|----------------------------------------------------------------------------------|
| 1   | `IS_DEV_MODE` | A string, indicate whether the development mode is enabled           | `true` - Development mode is enabled <br> `false` - Development mode is disabled |
| 2   | `IS_ADB`      | A string, indicate whether the Android Debug Bridge (ADB) is enabled | `true` - ADB mode is enabled <br> `false` - ADB mode is disabled                 | 
| 3   | `ROOTED`      | A string, indicate whether the device is rooted                      | `true` - Device is rooted <br> `false` - Device is not rooted                    |

### Sample Adaptive Authentication Script

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

