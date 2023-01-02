package com.application.foggy.api.v1.userdetailsmanagement;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginUserService implements UserDetailsService {
    private UserDetailsManagementService userDetailsManagementService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserDetailsManagement> user = userDetailsManagementService.findByEmail(email);
        if (user.isPresent()) {
            return new LoginUser(passwordEncoder.encode(user.get().getId()), user.get().getEmail(), user.get().getUserRole());
        }
        throw new UsernameNotFoundException("username not found");
    }


}
