package com.devteria.identity_service.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
    @Builder.Default // Sử dụng @Builder.Default để đảm bảo giá trị mặc định luôn được giữ
    private int code = 1000;
    private String message;
    private T result;
}
