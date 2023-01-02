package com.application.foggy.support.websecurityconfig;

import lombok.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenDetails {
    private String id;
    private String email;
    private String name;

}
