## Building and Integrating the Extensions into the Identity Server

### Option 1: Download Directly from WSO2 Store

1. **Download Connector and Artifacts**:
    - Download the connector and other required artifacts directly from the [WSO2 store](https://store.wso2.com/store/assets/isconnector/details/14a8ff5e-5db9-44f4-83cd-8a2534d5892f).

2. **Copy and Insert the JAR Files**:
    - After downloading, locate the necessary `.jar` files (usually named as `org.wso2.carbon.identity.<artifact-name>.jar`).
    - Copy these files and paste them into the `<IS_HOME>/repository/components/dropins/` directory in your WSO2 Identity Server.

### Option 2: Build and Integrate Manually

1. **Clone or Download the Project**:
    - Clone this project onto your computer or download it as a zip file.

2. **Build the OSGi Bundle**:
    - Build the OSGi bundle for the extension by running `mvn clean install` in the project directory.

3. **Copy and Insert the JAR Files**:
    - Locate the `org.wso2.carbon.identity.conditional.auth.config.entgra-<versionNumber>-SNAPSHOT.jar` file in the `<PROJECT_HOME>/components/org.wso2.carbon.identity.conditional.auth.config.entgra/target` directory.
    - Also, find the `org.wso2.carbon.identity.conditional.auth.functions.entgra-<versionNumber>-SNAPSHOT.jar` file in the `<PROJECT_HOME>/components/org.wso2.carbon.identity.conditional.auth.functions.entgra/target` directory.
    - Copy both files and paste them into the `<IS_HOME>/repository/components/dropins/` directory in your WSO2 Identity Server.

[Back to Getting started Page](../README.md)