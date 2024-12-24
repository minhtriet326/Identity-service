package com.devteria.identity_service.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

//@Getter
//@AllArgsConstructor
//public enum ErrorCode {
//    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
//    INVALID_KEY(1001, "Invalid enum key"),
//    USER_EXISTED(1002, "Username existed"),
//    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
//    PASSWORD_INVALID(1004, "Password must be at least 3 characters and maximum 10 characters");
//
//    private final int code;
//    private final String message;
//}

@AllArgsConstructor
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid enum key", HttpStatus.BAD_REQUEST), // để cái message sai
    USER_EXISTED(1002, "Username already exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters and maximum 10 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Username not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You don't have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    SESSION_EXPIRED(1009, "Session expired", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
