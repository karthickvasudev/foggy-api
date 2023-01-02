package com.application.foggy.api.v1.login;

import com.application.foggy.api.v1.userdetailsmanagement.UserDetailsManagement;
import com.application.foggy.api.v1.userdetailsmanagement.UserDetailsManagementService;
import com.application.foggy.api.v1.userdetailsmanagement.UserRole;
import com.application.foggy.support.websecurityconfig.SecretConfig;
import com.application.foggy.support.websecurityconfig.TokenDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class SecurityTokenVerifier extends OncePerRequestFilter {
    private final SecretConfig secretConfig;
    private final TokenDetails tokenDetails;
    private String token;
    private UserDetailsManagementService userDetailsManagementService;

    public SecurityTokenVerifier(UserDetailsManagementService userDetailsManagementService, SecretConfig secretConfig, TokenDetails tokenDetails) {
        this.userDetailsManagementService = userDetailsManagementService;
        this.secretConfig = secretConfig;
        this.tokenDetails = tokenDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            token = request.getHeader("authorization");
            if (!Strings.isNullOrEmpty(token)) {
                Claims jsonToken = Jwts.parserBuilder().setSigningKey(secretConfig.getSecretKey()).build()
                        .parseClaimsJws(token).getBody();
                String id = jsonToken.getSubject();
                String email = jsonToken.get("email", String.class);
                UserRole type = UserRole.valueOf(jsonToken.get("type", String.class));
                Optional<UserDetailsManagement> user = userDetailsManagementService.findByEmail(email);
                tokenDetails.setId(id);
                tokenDetails.setEmail(email);
                if (user.isPresent()) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(email, null, type.getGrantedAuthority());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        } catch (Exception e) {
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "Session Timeout");
        response.setHeader("error", "Session Timeout");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
