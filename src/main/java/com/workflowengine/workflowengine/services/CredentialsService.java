package com.workflowengine.workflowengine.services;

import com.workflowengine.workflowengine.domain.JWTToken;
import com.workflowengine.workflowengine.model.Credentials;
import com.workflowengine.workflowengine.security.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class CredentialsService implements UserDetailsService {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = getUserByUsername(username);
        if (credentials == null) {
            throw new UsernameNotFoundException(username);
        }

        return new org.springframework.security.core.userdetails.User(credentials.getUsername(), credentials.getPassword(), Collections.emptyList()/*grantedAuthorities*/);
    }

    //TODO: once the DB for the User is implemented just call the method from repo.
    public Credentials getUserByUsername(String username) {

        return sampleCreadentials();
//        return credentialsRepository.findByUsername(username);
    }

    public JWTToken getNewJWTToken(Credentials currentUser) {
        JWTToken jwtToken = new JWTToken();
//        jwtToken.setRefreshToken(getRefreshToken(currentUser));
        jwtToken.setAccessToken(generateToken(currentUser));
        jwtToken.setExpiration(System.currentTimeMillis() + jwtConfig.getExpiration() * 1000);
        return jwtToken;
    }

    private String generateToken(Credentials sysUser) {
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(String.valueOf(sysUser.getId()))
                .claim("userId", sysUser.getId())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();
    }


    //TODO: Complete these methods when tables are created in the db
//    private String getRefreshToken(SysUser sysUser) {
//        // Generate new RT and save to DB.
//        RefreshToken existingRefreshToken = refreshTokenRepository.findBySysUser(sysUser);
//        String newRefreshToken = UUID.randomUUID().toString();
//        RefreshToken refreshToken;
//        if (existingRefreshToken == null) {
//            refreshToken = new RefreshToken();
//            refreshToken.setSysUser(sysUser);
//        } else {
//            refreshToken = existingRefreshToken;
//        }
//        refreshToken.setRefreshToken(newRefreshToken);
//        refreshTokenRepository.save(refreshToken);
//        return newRefreshToken;
//    }
//
//    public JWTToken refreshJwtToken(RefreshTokenRequest tokenRequest) {
//        SysUser userByUserId = sysUserService.getUserByUserId(tokenRequest.getUserId());
//        RefreshToken existingRefreshToken = refreshTokenRepository.findBySysUser(userByUserId);
//        if (existingRefreshToken.getRefreshToken().equals(tokenRequest.getRefreshToken())) {
//            return getNewJWTToken(userByUserId);
//        } else {
//            throw new InvalidRefreshTokenException("Invalid refresh token received. - " + tokenRequest.getRefreshToken());
//        }
//    }


    private Credentials sampleCreadentials() {
        Credentials toReturn = new Credentials();
        toReturn.setId(1);
        toReturn.setUsername("admin");
        toReturn.setPassword(this.bCryptPasswordEncoder.encode("password"));
        return toReturn;
    }
}
