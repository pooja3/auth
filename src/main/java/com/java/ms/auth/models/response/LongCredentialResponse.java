package com.java.ms.auth.models.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LongCredentialResponse {

    String emailId;

    String userRole;

    String jsonWebToken;
}
