package com.apm.demo.flight;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyAuthFailureHandler implements AuthenticationFailureHandler {
    Logger logger = Logger.getLogger(MyAuthFailureHandler.class.getName());

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.log(Level.SEVERE, "Exception in Authentication", e);
        httpServletResponse.sendRedirect("/login?error=true");
    }
}
