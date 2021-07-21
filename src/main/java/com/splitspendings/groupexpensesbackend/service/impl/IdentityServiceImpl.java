package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserIdentityDto;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class IdentityServiceImpl implements IdentityService {

    private final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Override
    public AppUserIdentityDto currentUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
                AppUserIdentityDto user = new AppUserIdentityDto();
                user.setId(UUID.fromString(keycloakPrincipal.getName()));
                AccessToken token = keycloakPrincipal.getKeycloakSecurityContext().getToken();
                user.setEmail(token.getEmail());
                user.setFirstName(token.getGivenName());
                user.setLastName(token.getFamilyName());
                return user;
            } else {
                log.warn("Cannot get user data from principal");
            }
        } catch (Exception e) {
            log.warn("Cannot get principal: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public UUID currentUserID() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
                return UUID.fromString(keycloakPrincipal.getName());
            } else {
                log.warn("Cannot get user data from principal");
            }
        } catch (Exception e) {
            log.warn("Cannot get principal: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public boolean unauthorized(UUID checkedUserId) {
        return !currentUserID().equals(checkedUserId);
    }

    @Override
    public void verifyAuthorization(UUID checkedUserId) {
        if(unauthorized(checkedUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorised for access to the user data");
        }
    }

    @Override
    public boolean unauthorized(UUID currentUserId, UUID checkedUserId) {
        return !currentUserId.equals(checkedUserId);
    }

    @Override
    public void verifyAuthorization(UUID currentUserId, UUID checkedUserId) {
        if(unauthorized(currentUserId, checkedUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorised for access to the user data");
        }
    }
}
