package org.wso2.carbon.identity.conditional.auth.functions.entgra.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.application.authentication.framework.JsFunctionRegistry;

import org.wso2.carbon.identity.conditional.auth.functions.entgra.GetDeviceInfoEntgraFunction;
import org.wso2.carbon.identity.conditional.auth.functions.entgra.GetDeviceInfoEntgraFunctionImpl;

/**
 * OSGI declarative services component which handle Entgra Functions
 */
@Component(
        name = "identity.conditional.auth.functions.entgra",
        immediate = true
)
public class EntgraFunctionsServiceComponent {

    private static final Log LOG = LogFactory.getLog(EntgraFunctionsServiceComponent.class);
    public static final String FUNC_GET_DEVICE_INFO_ENTGRA = "getDeviceInfoEntgra";

    @Activate
    protected void activate(ComponentContext ctxt) {

        try {
            GetDeviceInfoEntgraFunction getDeviceInfoEntgraFunction = new GetDeviceInfoEntgraFunctionImpl();

            JsFunctionRegistry jsFunctionRegistry = EntgraFunctionsServiceHolder.getInstance().getJsFunctionRegistry();
            jsFunctionRegistry.register(JsFunctionRegistry.Subsystem.SEQUENCE_HANDLER, FUNC_GET_DEVICE_INFO_ENTGRA,
                    getDeviceInfoEntgraFunction);
        } catch (Throwable e) {
            LOG.error("Error occurred during conditional authentication user functions bundle activation. ", e);
        }

    }

    @Deactivate
    protected void deactivate(ComponentContext ctxt) {

        JsFunctionRegistry jsFunctionRegistry = EntgraFunctionsServiceHolder.getInstance().getJsFunctionRegistry();
        if (jsFunctionRegistry != null) {
            jsFunctionRegistry.deRegister(JsFunctionRegistry.Subsystem.SEQUENCE_HANDLER, FUNC_GET_DEVICE_INFO_ENTGRA);
        }
    }

    @Reference(
            service = JsFunctionRegistry.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetJsFunctionRegistry"
    )
    public void setJsFunctionRegistry(JsFunctionRegistry jsFunctionRegistry) {

        EntgraFunctionsServiceHolder.getInstance().setJsFunctionRegistry(jsFunctionRegistry);
    }

    public void unsetJsFunctionRegistry(JsFunctionRegistry jsFunctionRegistry) {

        EntgraFunctionsServiceHolder.getInstance().setJsFunctionRegistry(null);
    }

}
