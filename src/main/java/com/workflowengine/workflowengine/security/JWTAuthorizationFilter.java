package com.workflowengine.workflowengine.security;
import com.workflowengine.workflowengine.domain.JWTToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtConfig jwtConfig;

    public JWTAuthorizationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
        super(authManager);
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(jwtConfig.getHeader());

        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(req, res);
            return;
        }

        String token = req.getHeader(jwtConfig.getHeader());
        token = token.replace(jwtConfig.getPrefix(), "");

        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private JWTToken getToken(String accessToken) {
        JWTToken jwtToken = new JWTToken();
        jwtToken.setAccessToken(accessToken);
        return jwtToken;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
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