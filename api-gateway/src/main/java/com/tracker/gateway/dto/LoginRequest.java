package com.tracker.gateway.dto;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class LoginRequest {
    private String email;
    private String password;
}
