## Configuring the WSO2 Identity server

1. If you haven’t downloaded WSO2 Identity server yet, please visit [https://wso2.com/identity-server/](https://wso2.
   com/identity-server/) and download the latest version of the Identity Server. </br></br>

2. Go to [https://github.com/PasinduYeshan/identity-auth-entgra](https://github.
   com/PasinduYeshan/identity-auth-entgra) and follow exact steps to add Entgra connector and conditional 
   authentication function to WSO2 Identity Server. </br></br> 

3. Start the WSO2 Identity server.  Login to WSO2 IS management console from [http://localhost:9443/carbon]
(http://localhost:9443/carbon)/ and navigate to **Service Providers** tab listed under the Identity section. </br></br>

4. Click Add to add a new service provider. </br></br>

5. Provide a name for the service provider (ex:- ISEntgra) and click **Register**. Now you will be redirected to the 
   **Edit Service Provider** page. </br></br>

6. Expand the **Inbound Authentication Configuration** section and click **Configure** under the **OAuth/OpenID 
   Connect Configuration** section. </br></br>

7. Under **Allowed Grant Types** uncheck everything except `Code` and `Refresh Token`. </br></br>

8. Enter Callback URL(s) as for the following values.

   Callback Url: `wso2entgra://oauth2`

   > Alternatively if you’re running in an emulator, you can use `http://10.0.2.2:8081` as the 
   > callback url.

9. Once the configurations are added, you will be redirected to the **Service Provider Details**
   page. Here, expand the **Inbound Authentication Configuration** section and click on the **OAuth/OpenID 
   Connect Configuration**. Copy the value of `OAuth Client Key` shown here. </br></br>

    ![Screen Shot 2022-05-30 at 1.03.15 PM.png](https://user-images.githubusercontent.com/61885844/171583227-e957efec-df32-4728-af18-004929481cfa.png) </br></br>

10. Expand the **Local & Outbound Authentication Configuration** section and select **Advanced Configuration,** Now 
    you will be redirected to **Advanced Authentication Configuration** page. </br></br>

11. Expand **Script Based Adaptive Authentication** section and paste following code and edit according to your 
    needs.

    ```jsx
    var onLoginRequest = function(context) {
        deviceID = context.request.params.deviceID[0];
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

   > In above code, inside the `getDeviceInfoEntgra` function’s `onSuccess` event handler you can access device 
   > information  as follows.
   > 1. Development mode enabled :- deviceInfo.IS_DEV_MODE
   > 2. Device is rooted :- deviceInfo.ROOTED
   > 3. ADB enabled :- deviceInfo.IS_ADB
   
   To see the Entgra adaptive authentication script documentation, Click [here](files/adaptive_script.md).

12. Expand the **Authentication Step Configuration** and click on **Add Authentication Step**  button make sure to 
    mark **Use subject identifier from this step** and **Use attributes from this step**. Then under **Local Authenticators** select an option and click on **Add authenticator** button. According to the above code there should be two authentication steps, therefore add another authentication steps and do not mark **Use subject identifier from this step** and **Use attributes from this step** options in this step.

   ![Screen Shot 2022-05-30 at 1.33.19 PM.png](https://user-images.githubusercontent.com/61885844/171583623-45b674e7-5fc9-4156-9f4e-2cf2640a243e.png)

13. Make sure to click the `Update` button to save the changes. Now you will be redirected **Service Providers** page 
   and make sure to click the `Update` button on the bottom to save all the changes. </br></br>

14. Navigate to **Identity Providers** tab listed under the Identity section. Click on **Resident** button and, you 
    will be redirected to **Resident Realm Configuration.** </br></br>

15. Expand **Other Settings** and add the following configurations under **Entgra Configurations**.


    | Enable Entgra | True |
    | --- | --- |
    | Token URL | https://{hostname}/oauth2/token |
    | Device Information URL | https://{hostname}/api/device-mgt/v1.0/devices/1.0.0 |
    | Client Key | Client ID of the Service Provider created in the Entgra IoT server |
    | Client Secret | Client Secret of the Service Provider created in the Entgra IoT server |

16. Click on `Update` button to save the changes.

