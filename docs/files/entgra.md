## Configuring the Entgra IoT Server

1. Register the application in Entgra IoT server by using following CURL command. For the Authorization header encode
   `username:password` to Base64 format and use the encoded value as `'Authorization: Basic {encodedValue}'`.
   Provide any name for the `applicationName` and provide the application owner’s username for the `username`. Copy the
   values of `client_id` and `client_secret`.

    ```shell
    curl --location --request POST 'https://${mgtURL}/api-application-registration/register' \
        --header 'Accept: application/json' \
        --header 'Authorization: Basic YWRtaW46YWRtaW4=' \
        --header 'Content-Type: application/json' \
        --data-raw '{"applicationName":"sp_sdk_s1","tags":["android","device_management"],"username":"admin",
                    "allowedToAllDomains":false,"mappingAnExistingOAuthApp":false}'
    ```

   Response:

   `{"client_secret":"gBb6LATYVyxplGhvB6tcckBOvo8a","client_id":"O6lYcMOwg1wl9OfhCrUDB_QTkKwa"}` 

2. Login to Entgra IoT management console from `https://{mgtURL}/carbon` and navigate to the **Service Providers** tab
   listed under the **Main** section. You will see the service provider you created above.

3. Expand the **OAuth/OpenID Connect Configuration** section inside **Inbound Authentication Configuration** section
   and check if the `OAuth Client Key` is same as the `client_id` you got from the above response.

4. Click on **Edit** button, and you will be redirected to Application Settings page.

   Make sure **Code** option in **Allowed Grant Types** and **Default** in **Token Issuer** are ticked. Provide a
   valid URL format ending with `/sdk/secure` for **Callback Url** section. 

   ![Screen Shot 2022-05-23 at 11.23.15 AM.png](https://user-images.githubusercontent.com/61885844/171582844-4ff662ea-039b-4d7f-b8d2-efa69ad7f7a2.png)

   ![Screen Shot 2022-05-23 at 11.22.28 AM.png](https://user-images.githubusercontent.com/61885844/171583041-cd088c67-fbb9-4d2a-a31d-b14e81855237.png)
   

5. To set up the WSO2 Identity Server as an External Identity Provider (IDP) with Entgra MDM using OpenID 
Connect (OIDC), follow the detailed instructions provided [here](https://entgra.io/tag/asgardeo/). Remember to 
adjust the endpoint URLs as needed to match your specific configuration.

[Back to Previous Page](../README.md)
