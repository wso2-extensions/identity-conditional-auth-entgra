package org.wso2.carbon.identity.conditional.auth.functions.entgra.exception;

/**
 * Custom Exception for Entgra Connector
 */
public class EntgraConnectorException extends Exception {

    public EntgraConnectorException(String msg) {

        super(msg);
    }

    public EntgraConnectorException(String msg, Throwable cause) {

        super(msg, cause);
    }
}
