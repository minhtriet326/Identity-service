package com.devteria.identity_service.exceptions;

import com.devteria.identity_service.enums.ErrorCode;
import lombok.Getter;

// @Getter
// public class AppException extends RuntimeException {
//    private ErrorCode errorCode;// exception này nó có 1 thuộc tính về chi tiết lỗi
//
//    public AppException(ErrorCode errorCode) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//    }
// }

@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
