package com.splitspendings.groupexpensesbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenAPIConfig {

    private final GroupExpensesProperties props;
    private final KeycloakSpringBootProperties keycloakProps;

    public OpenAPIConfig(GroupExpensesProperties props, KeycloakSpringBootProperties keycloakProps) {
        this.props = props;
        this.keycloakProps = keycloakProps;
    }

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Splitting Group Expenses API").version("1"))
                .servers(Collections.singletonList(new Server().url(props.getApiUrl())))
                .components(new Components()
                        .addSecuritySchemes("identity", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows().implicit(new OAuthFlow()
                                        .authorizationUrl( keycloakProps.getAuthServerUrl()
                                                +"/realms/"+keycloakProps.getRealm() +
                                                "/protocol/openid-connect/auth")))));
    }
}
