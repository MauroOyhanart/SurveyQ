package com.maurooyhanart.surveyq.backend.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    @NotNull(message = "Email cannot be null")
    private String email;
}
