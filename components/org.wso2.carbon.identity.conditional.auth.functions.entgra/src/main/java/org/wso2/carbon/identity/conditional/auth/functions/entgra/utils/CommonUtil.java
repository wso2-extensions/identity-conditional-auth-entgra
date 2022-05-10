package org.wso2.carbon.identity.conditional.auth.functions.entgra.utils;

import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticatedUser;
import org.wso2.carbon.identity.application.authentication.framework.config.model.graph.js.JsAuthenticationContext;

public abstract class CommonUtil {

    /**
     * Function that is used get the user from the context.
     *
     * @param context Context from authentication flow.
     * @return User object respect to the authentication.
     */
    public static JsAuthenticatedUser getUser(JsAuthenticationContext context) {

        return (JsAuthenticatedUser) context.getMember("currentKnownSubject");
    }



}
