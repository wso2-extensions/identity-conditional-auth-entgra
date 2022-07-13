package org.wso2.carbon.identity.conditional.auth.config.entgra.internal;

import org.wso2.carbon.identity.application.authentication.framework.JsFunctionRegistry;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

public class EntgraConnectorServiceHolder {

    public static EntgraConnectorServiceHolder instance = new EntgraConnectorServiceHolder();

    private RealmService realmService;
    private RegistryService registryService;
    private JsFunctionRegistry jsFunctionRegistry;
    private IdentityGovernanceService identityGovernanceService;

    private EntgraConnectorServiceHolder() {

    }

    public static EntgraConnectorServiceHolder getInstance() {

        return instance;
    }

    public RealmService getRealmService() {

        return realmService;
    }

    public void setRealmService(RealmService realmService) {

        this.realmService = realmService;
    }

    public RegistryService getRegistryService() {

        return registryService;
    }

    public void setRegistryService(RegistryService registryService) {

        this.registryService = registryService;
    }

    public void setJsFunctionRegistry(JsFunctionRegistry jsFunctionRegistry) {

        this.jsFunctionRegistry = jsFunctionRegistry;
    }

    public IdentityGovernanceService getIdentityGovernanceService() {

        return identityGovernanceService;
    }

    public void setIdentityGovernanceService(IdentityGovernanceService identityGovernanceService) {

        this.identityGovernanceService = identityGovernanceService;
    }
}
