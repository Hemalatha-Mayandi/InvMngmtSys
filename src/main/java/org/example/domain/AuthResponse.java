package org.example.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    String token;
    String userId;
    String message;
}
