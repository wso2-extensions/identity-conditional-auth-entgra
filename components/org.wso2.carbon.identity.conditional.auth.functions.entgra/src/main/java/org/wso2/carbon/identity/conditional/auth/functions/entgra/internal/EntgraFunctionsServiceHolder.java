package org.wso2.carbon.identity.conditional.auth.functions.entgra.internal;

import org.wso2.carbon.identity.application.authentication.framework.JsFunctionRegistry;

public class EntgraFunctionsServiceHolder {

    private static EntgraFunctionsServiceHolder instance = new EntgraFunctionsServiceHolder();

    private JsFunctionRegistry jsFunctionRegistry;

    public static EntgraFunctionsServiceHolder getInstance() {

        return instance;
    }

    private EntgraFunctionsServiceHolder() {

    }

    public JsFunctionRegistry getJsFunctionRegistry() {

        return jsFunctionRegistry;
    }

    public void setJsFunctionRegistry(JsFunctionRegistry jsFunctionRegistry) {

        this.jsFunctionRegistry = jsFunctionRegistry;
    }

}
