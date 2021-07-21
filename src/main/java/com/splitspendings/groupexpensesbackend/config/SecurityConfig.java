package com.splitspendings.groupexpensesbackend.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.QueryParamPresenceRequestMatcher;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = {KeycloakSecurityComponents.class})
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    private final KeycloakClientRequestFactory keycloakClientRequestFactory;

    public SecurityConfig(KeycloakClientRequestFactory keycloakClientRequestFactory) {
        this.keycloakClientRequestFactory = keycloakClientRequestFactory;
    }

    @Bean
    @ConditionalOnMissingBean(HttpSessionManager.class)
    @Override
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Override
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        RequestMatcher requestMatcher = new OrRequestMatcher(
                new QueryParamPresenceRequestMatcher(OAuth2Constants.ACCESS_TOKEN),
                new AuthorizationRequestMatcher()
        );
        KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(authenticationManagerBean(), requestMatcher);
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        return filter;
    }

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate() {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.cors().and().csrf().disable()
                .headers().frameOptions().disable()
                .and().authorizeRequests()
                /*.antMatchers(
                        "/api/common/**",
                        "/img/**"
                ).permitAll()*/
                .antMatchers(
                        "/api/admin/**"
                ).hasRole("se_admin")
                .antMatchers(
                        "/api/**"
                ).authenticated();
    }
}
