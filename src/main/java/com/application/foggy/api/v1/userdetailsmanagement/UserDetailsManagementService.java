package com.application.foggy.api.v1.userdetailsmanagement;

import com.application.foggy.support.websecurityconfig.TokenDetails;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsManagementService {
    private final UserDetailsManagementRepository repository;
    private final TokenDetails tokenDetails;
    private UserConversionPojo conversionPojo;

    public Optional<UserDetailsManagement> getUserByEmailAndId(String email, String id) {
        return repository.findByEmailAndId(email, id);
    }

    public UserDetailsManagement createUserThroughLogin(UserDetailsManagement user) {
        return repository.save(user);
    }

    public UserDetailsManagement convertToUserDetailsManagementObject(HttpServletRequest request) {
        try {
            String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            UserConversionPojo userConversionPojo = new Gson().fromJson(json, UserConversionPojo.class);
            setConversionPojo(userConversionPojo);
            return UserDetailsManagement.builder()
                    .id(userConversionPojo.getGoogleId())
                    .name(userConversionPojo.getName())
                    .email(userConversionPojo.getEmail())
                    .imageUrl(userConversionPojo.getImageUrl())
                    .userRole(UserRole.USER)
                    .createdOn(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<UserDetailsManagement> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public UserConversionPojo getConversionPojo() {
        return conversionPojo;
    }

    public void setConversionPojo(UserConversionPojo conversionPojo) {
        this.conversionPojo = conversionPojo;
    }

    public UserDetailsManagement getProfile() {
        return repository.findById(tokenDetails.getId()).get();
    }
}
