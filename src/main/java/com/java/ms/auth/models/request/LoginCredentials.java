package com.java.ms.auth.models.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginCredentials {

    private String email;
    private String password;
    private String userRole;

}