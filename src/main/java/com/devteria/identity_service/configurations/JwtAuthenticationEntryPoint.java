package com.devteria.identity_service.configurations;

import com.devteria.identity_service.dtos.responses.ApiResponse;
import com.devteria.identity_service.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
//
//        response.setStatus(errorCode.getStatusCode().value());
//
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);// set kiểu trả về
//
//        ApiResponse<?> apiResponse = ApiResponse.builder()
//                .code(errorCode.getCode())
//                .message(errorCode.getMessage())
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
//        // ghi chuỗi JSON đại diện cho ApiResponse vào response
//
//        response.flushBuffer();
//        // tất cả dữ liệu trong buffer sẽ được đẩy đến client ngay lập tức,
//        // thay vì chờ đến khi buffer đầy mới được gửi
//    }
//}

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // khi 1 request dính lỗi UNAUTHENTICATED thì ta dùng class này để xử lý một cách rõ ràng và thống nhất
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));// ghi data vao response
        response.flushBuffer();// phan hoi cho client ngay lap tuc ma ko cho doi
    }
}
