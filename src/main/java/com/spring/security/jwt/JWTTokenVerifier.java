package com.spring.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JWTTokenVerifier extends OncePerRequestFilter {
    private final JWTConfig jwtConfig;

    private final SecretKey secretKey;

    public JWTTokenVerifier(JWTConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        //when client submits request we will get token form header
        String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeaders());
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            return;
        } else {
            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
            try {
                Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey)
                        .parseClaimsJws(token);

                Claims body = claimsJws.getBody();
                String username = body.getSubject();
                List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
                Set<SimpleGrantedAuthority> authoritySet = authorities.stream().map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authoritySet

                );
                SecurityContextHolder.getContext().setAuthentication(authentication); //client token is authenticated
                filterChain.doFilter(httpServletRequest,httpServletResponse); // passing it next filter
            } catch (JwtException exception) {
                System.out.println("Exception in token verfication " + token + "    " + exception);
            }
        }
    }
}
