package com.application.foggy.api.v1.userdetailsmanagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_details_management")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsManagement {
    @Id
    private String id;
    private String name;
    @Indexed
    @Unique
    private String email;
    @Indexed
    @Unique
    private String phoneNumber;

    private String imageUrl;
    private UserRole userRole;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime createdOn;

}
