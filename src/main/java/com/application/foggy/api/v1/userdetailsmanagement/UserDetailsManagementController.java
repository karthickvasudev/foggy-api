package com.application.foggy.api.v1.userdetailsmanagement;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1")
@AllArgsConstructor
public class UserDetailsManagementController {
    private UserDetailsManagementService userDetailsManagementService;

    @GetMapping("profile")
    public UserDetailsManagement getProfile() {
        return userDetailsManagementService.getProfile();
    }
}
