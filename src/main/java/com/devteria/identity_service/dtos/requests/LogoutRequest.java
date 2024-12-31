package com.devteria.identity_service.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Builder
// @FieldDefaults(level = AccessLevel.PRIVATE)
// public class LogoutRequest {
//    String token;
// }

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogoutRequest {
    String token;
}
