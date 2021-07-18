package com.splitspendings.groupexpensesbackend.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authorizationHeaderValue != null && !authorizationHeaderValue.startsWith("Basic ");
    }
}
