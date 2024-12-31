package com.devteria.identity_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

// @Entity
// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Builder
// @FieldDefaults(level = AccessLevel.PRIVATE)
// public class InvalidatedToken {
//    @Id
//    String id;
//
//    Date expiryTime;
// }
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken {
    @Id
    String id;

    Date expiryTime;
}
