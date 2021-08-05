package com.spring.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JWTUsernameAndPassAndFilter extends UsernamePasswordAuthenticationFilter {

    public JWTUsernameAndPassAndFilter(AuthenticationManager authenticationManager, JWTConfig jwtConfig, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    private final AuthenticationManager authenticationManager;

    private final JWTConfig jwtConfig;

    private final SecretKey secretKey;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            UsernameAndPassAuthenticationrequest usernameAndPassAuthenticationrequest =
                    new ObjectMapper().readValue(request.getInputStream(),UsernameAndPassAuthenticationrequest.class);
                  Authentication authentication=  new UsernamePasswordAuthenticationToken(
                                usernameAndPassAuthenticationrequest.getUsername(), ///pricipal
                                usernameAndPassAuthenticationrequest.getPassword() //credential
                        );
         Authentication authenticate =  authenticationManager.authenticate(authentication);
         return authenticate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //It will excute after attempt authentication It is used create a JWT token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //String key = "securesecurebsnjfritoijbnnssecuresecurebsnjfritoijbnns";//it should very long nd secure

        //this token is called Jws when client send JWT in header we will extract jws form it
        String token = Jwts.builder().setSubject(authResult.getName())
                .claim("authorities",authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(secretKey)
                .compact();
        response.addHeader(jwtConfig.getAuthorizationHeaders(), jwtConfig.getTokenPrefix() +token);
    }

}
