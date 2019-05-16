package com.workflowengine.workflowengine.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflowengine.workflowengine.domain.JWTToken;
import com.workflowengine.workflowengine.model.Credentials;
import com.workflowengine.workflowengine.services.CredentialsService;
import com.workflowengine.workflowengine.utils.Constants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.workflowengine.workflowengine.utils.Constants.KEY_HEADER_CONTENT_TYPE;

/**
 * Responsible for authenticating user and issuing JWT token.
 */

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private CredentialsService credentialsService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, String uri, CredentialsService credentialsService) {
        this.authenticationManager = authenticationManager;
        this.credentialsService = credentialsService;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(uri, "POST"));

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Credentials creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Credentials.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), Collections.emptyList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        JWTToken jwtToken = null;
        if (auth != null) {
            Credentials userByUsername = credentialsService.getUserByUsername(auth.getName());
            jwtToken = credentialsService.getNewJWTToken(userByUsername);
        }
        res.addHeader(KEY_HEADER_CONTENT_TYPE, Constants.VAL_HEADER_APPLICATION_JSON);
        String response = new ObjectMapper().writeValueAsString(jwtToken);
        res.getWriter().write(response);
    }
}
