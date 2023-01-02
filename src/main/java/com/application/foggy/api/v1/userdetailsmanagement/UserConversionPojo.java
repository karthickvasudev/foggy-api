package com.application.foggy.api.v1.userdetailsmanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserConversionPojo {
    private String googleId;
    private String imageUrl;
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String username;
    private String password;
}
