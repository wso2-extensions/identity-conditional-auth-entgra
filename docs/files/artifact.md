## Building and Integrating the Extensions into the Identity Server.

1. Clone this project onto your computer or download it as a zip.
2. Build the OSGi bundle for the extension by running `mvn clean install`.
3. Copy the `org.wso2.carbon.identity.conditional.auth.config.entgra-<versionNumber>-SNAPSHOT.jar` file 
from the `<PROJECT_HOME>/components/org.wso2.carbon.identity.conditional.auth.config.entgra/target` directory 
and `org.wso2.carbon.identity.conditional.auth.functions.entgra-<versionNumber>-SNAPSHOT.jar` file from the
`<PROJECT_HOME>/components/org.wso2.carbon.identity.conditional.auth.functions.entgra/target` directory and  insert 
4. into the  `<IS_HOME>/repository/components/dropins/` directory in the WSO2 Identity Server.