package org.wso2.carbon.identity.auth.connector.entgra.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.auth.connector.entgra.EntgraConfigImpl;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.governance.common.IdentityConnectorConfig;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * OSGI declarative services component which handle Entgra Connector
 */

@Component(
    name = "identity.auth.connector.entgra",
    immediate = true
)

public class EntgraConnectorServiceComponent {

    private static final Log LOG = LogFactory.getLog(EntgraConnectorServiceComponent.class);

    @Activate
    protected void activate(ComponentContext ctxt) {
        try {
            BundleContext bundleContext = ctxt.getBundleContext();
            EntgraConfigImpl entgraFunctionConfig = new EntgraConfigImpl();
            bundleContext.registerService(IdentityConnectorConfig.class.getName(), entgraFunctionConfig, null);

        } catch (Throwable e) {
            LOG.error("Error while activating Entgra Connector", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext ctxt) {

        LOG.info("Entgra connector is deactivated");
    }

    @Reference(
            name = "user.realmservice.default",
            service = RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService"
    )
    protected void setRealmService(RealmService realmService) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("RealmService is set in the custom conditional authentication user functions bundle");
        }
        EntgraConnectorServiceHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("RealmService is unset in the custom conditional authentication user functions bundle");
        }
        EntgraConnectorServiceHolder.getInstance().setRealmService(null);
    }

    @Reference(
            name = "registry.service",
            service = RegistryService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRegistryService"
    )
    protected void setRegistryService(RegistryService registryService) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("RegistryService is set in the custom conditional authentication user functions bundle");
        }
        EntgraConnectorServiceHolder.getInstance().setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("RegistryService is unset in the custom conditional authentication user functions bundle");
        }
        EntgraConnectorServiceHolder.getInstance().setRegistryService(null);
    }

    @Reference(
            name = "identity.governance.service",
            service = IdentityGovernanceService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetIdentityGovernanceService"
    )
    protected void setIdentityGovernanceService(IdentityGovernanceService identityGovernanceService) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Identity Governance service is set form functions");
        }
        EntgraConnectorServiceHolder.getInstance().setIdentityGovernanceService(identityGovernanceService);
    }

    protected void unsetIdentityGovernanceService(IdentityGovernanceService identityGovernanceService) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Identity Governance service is unset from functions");
        }
        EntgraConnectorServiceHolder.getInstance().setIdentityGovernanceService(null);
    }

}
