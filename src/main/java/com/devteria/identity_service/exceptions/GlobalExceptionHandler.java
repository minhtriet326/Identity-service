package com.devteria.identity_service.exceptions;

import java.util.Map;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devteria.identity_service.dtos.responses.ApiResponse;
import com.devteria.identity_service.enums.ErrorCode;

// @ControllerAdvice
// public class GlobalExceptionHandler {
//    private final String MIN_ATTRIBUTE = "min";
//    @ExceptionHandler(AppException.class)// xử lý bất kì ex nào được ném ra
//    public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
//        ErrorCode errorCode = ex.getErrorCode();
//
//        ApiResponse apiResponse = new ApiResponse<>();
//
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(ex.getMessage());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)// exception chỗ @Valid
//    public ResponseEntity<ApiResponse>
// handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        String enumName = ex.getFieldError().getDefaultMessage();
//
//        // xử lý lỗi bắt 1 enum không tồn tại, có thể do sai chính tả
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//
//        Map<String, Object> attributes = null;
//
//        try {
//            // lấy cái message chỗ @Size đã set sẵn value là "USERNAME_INVALID"
//            errorCode = ErrorCode.valueOf(enumName);
//
//            var constraintViolation = ex.getBindingResult().getAllErrors()
//                    .getFirst().unwrap(ConstraintViolation.class);
//
//            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
//
//        } catch (IllegalArgumentException exception) {
//            // ko làm gì cả, xuống dưới nó tự lấy code và message trong errorCode
//        }
//
//        ApiResponse apiResponse = new ApiResponse<>();
//
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(attributes != null ? mapAttribute(errorCode.getMessage(),
// attributes)
//                : errorCode.getMessage());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
//    }
//
//    @ExceptionHandler(Exception.class)// handle các lỗi ngoài các ex đã bắt
//    public ResponseEntity<ApiResponse> handleRuntimeException(Exception ex) {
//        ApiResponse apiResponse = new ApiResponse<>();
//
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
//        ErrorCode enumName = ErrorCode.UNAUTHORIZED;
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                ApiResponse.builder()
//                        .code(enumName.getCode())
//                        .message(enumName.getMessage())
//                        .build()
//        );
//    }
//
//    private String mapAttribute(String message, Map<String, Object> attribute) {
//        String minValue = attribute.get(MIN_ATTRIBUTE).toString();
//
//        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
// {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
//                HttpStatus.BAD_REQUEST, "One or more fields are not valid!"
//        );
//
//        problemDetail.setProperty("List of errors", errors);
//
//        return problemDetail;
//    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getConstraintViolations().forEach(error -> {
//            String propertyPath = error.getPropertyPath().toString();
//            String errorMessage = error.getMessage();
//
//            errors.put(propertyPath, errorMessage);
//        });
//
//        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
//        problemDetail.setDetail("One or more fields violated the constraint!");
//
//        problemDetail.setProperty("List of constraint violations!", errors);
//
//        return problemDetail;
//    }
// }

@ControllerAdvice
public class GlobalExceptionHandler {
  private final String MIN_ATTRIBUTE = "min";

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
    ErrorCode errorCode = ex.getErrorCode();

    ApiResponse apiResponse = new ApiResponse<>();

    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());

    return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    // Chỉ lấy ra message của lỗi đầu tiên được tìm thấy
    // Nếu có nhiều field bị lỗi, bạn sẽ bỏ qua các lỗi còn lại
    String enumName = ex.getFieldError().getDefaultMessage();

    ErrorCode errorCode = ErrorCode.INVALID_KEY;

    Map<String, Object> attributes = null;

    try {
      errorCode = ErrorCode.valueOf(enumName);

      var constraintViolation =
          ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
      // chuyển đổi lỗi validation sang đối tượng ConstraintViolation vì nó có nhiều info hơn

      attributes =
          constraintViolation
              .getConstraintDescriptor()
              .getAttributes(); // trả về 1 map các attributes của constraint
      // @NotBlank(message = "Tên không được để trống", min = 2, max = 50)
      // -> message: "Tên không được để trống"
      // min: 2
      // max: 50
    } catch (IllegalArgumentException ignored) {

    }

    ApiResponse apiResponse = new ApiResponse<>();

    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(
        attributes != null
            ? mapAttribute(errorCode.getMessage(), attributes)
            : errorCode.getMessage());

    return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    return ResponseEntity.status(errorCode.getStatusCode())
        .body(
            ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> handleUncategorizedException(Exception ex) {
    ApiResponse apiResponse = new ApiResponse<>();

    apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

    return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
        .body(apiResponse);
  }

  private String mapAttribute(String message, Map<String, Object> attributes) {
    String min = String.valueOf(attributes.get(MIN_ATTRIBUTE)); // return a value of a key

    return message.replace("{" + MIN_ATTRIBUTE + "}", min);
  }
}
