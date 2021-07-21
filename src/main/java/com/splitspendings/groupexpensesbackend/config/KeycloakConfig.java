package com.splitspendings.groupexpensesbackend.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("KeycloakConfigResolver")
class KeycloakConfig extends KeycloakSpringBootConfigResolver {

}
