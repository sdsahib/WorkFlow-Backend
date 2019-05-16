package com.workflowengine.workflowengine.security;

import com.workflowengine.workflowengine.domain.JWTToken;
import com.workflowengine.workflowengine.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ajaiswal on 4/17/2019.
 */
public class GHFilter extends BasicAuthenticationFilter {

    private JwtConfig jwtConfig;

    public GHFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        super(authenticationManager);
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader(Constants.KEY_HEADER_AUTHORIZATION);
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }
        authHeader = authHeader.replace(jwtConfig.getPrefix(), "").trim();
        Authentication authentication = getAuthentication(authHeader);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private JWTToken getToken(String accessToken) {
        JWTToken jwtToken = new JWTToken();
        jwtToken.setAccessToken(accessToken);
        return jwtToken;
    }

    private Authentication getAuthentication(String token) {
        if (token != null) {
            try {
                // parse the token.
                Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token).getBody();
                String user = claims.getSubject();
                if (user != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

                    AuthHolder.setCurrentToken(getToken(token));

                    AuthHolder.getCurrentToken().setAdditionalAttributes(new HashMap<>());
                    AuthHolder.getCurrentToken().getAdditionalAttributes().put("userId", String.valueOf(claims.get("userId")));
                    AuthHolder.getCurrentToken().setExpiration(claims.getExpiration().getTime());

                    return usernamePasswordAuthenticationToken;
                }
            } catch (Exception e) {
                logger.error("Token expired.", e);
            }
            return null;
        }
        return null;


    }

}