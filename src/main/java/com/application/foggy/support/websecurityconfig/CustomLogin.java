package com.application.foggy.support.websecurityconfig;

import com.application.foggy.api.v1.userdetailsmanagement.UserDetailsManagement;
import com.application.foggy.api.v1.userdetailsmanagement.UserDetailsManagementService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CustomLogin extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsManagementService userDetailsManagementService;
    private final SecretConfig secretConfig;

    public CustomLogin(AuthenticationManager authenticationManager, UserDetailsManagementService userDetailsManagementService, SecretConfig secretConfig) {
        this.authenticationManager = authenticationManager;
        this.userDetailsManagementService = userDetailsManagementService;
        this.secretConfig = secretConfig;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserDetailsManagement user = verifyGetUser(request);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getId());
        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = GenerateToken();
        response.addHeader("authorization", token);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Session Timeout");
        response.setHeader("error", "Session Timeout");
        response.setContentType("application/json");
    }

    private String GenerateToken() {
        Optional<UserDetailsManagement> userOpt = userDetailsManagementService.getUserByEmailAndId(userDetailsManagementService.getConversionPojo().getEmail(), userDetailsManagementService.getConversionPojo().getGoogleId());
        UserDetailsManagement user = userOpt.get();
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("type", user.getUserRole().name())
                .setSubject(user.getId())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(secretConfig.getSecretKey())
                .compact();
    }

    private UserDetailsManagement verifyGetUser(HttpServletRequest request) {
        UserDetailsManagement userObject = userDetailsManagementService.convertToUserDetailsManagementObject(request);
        Optional<UserDetailsManagement> userOpt = userDetailsManagementService.getUserByEmailAndId(userObject.getEmail(), userObject.getId());
        return userOpt.orElseGet(() -> createCustomer(userObject));
    }


    private UserDetailsManagement createCustomer(UserDetailsManagement user) {
        return userDetailsManagementService.createUserThroughLogin(user);
    }
}
