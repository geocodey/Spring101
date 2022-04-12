package com.siku.storz.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SuccessfulAuthHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest httpServletRequest,
                                        final HttpServletResponse httpServletResponse,
                                        final Authentication authentication) {
        String loggedInUser = null;
        final Object authenticationPrincipal = authentication.getPrincipal();
        if (authenticationPrincipal instanceof UserDetails) {
            final UserDetails springSecurityUser = (UserDetails) authenticationPrincipal;
            loggedInUser = springSecurityUser.getUsername();
        } else if (authenticationPrincipal instanceof String) {
            loggedInUser = (String) authenticationPrincipal;
        }

        log.info("The current authenticated user is '{}'", loggedInUser);
    }
}