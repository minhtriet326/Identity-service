package com.devteria.identity_service.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
// public class SwaggerConfig {
//    @Bean
//    public OpenAPI openAPI() {
//        Info info = new Info()
//                .title("Identity service API")
//                .version("1.0")
//                .description("This is Identity service app endpoints");
//
//        Components components = new Components()
//                .addSecuritySchemes("bearer-token", createSecuritySchema());
//                // Thêm scheme bảo mật với key là "bearer-token"
//
//        return new OpenAPI()
//                .info(info)
//                .components(components)
//                .addSecurityItem(new SecurityRequirement().addList("bearer-token"));
//                // Thêm yêu cầu bảo mật mặc định cho tất cả endpoints
//                //Tham chiếu đến scheme "bearer-token" đã định nghĩa ở trên
//    }
//
////    private SecurityScheme createSecuritySchema() { // method tạo cấu hình chi tiết scheme
////        return new SecurityScheme()
////                // name có thể khác với key ở trên, đây là tên hiển thị trên UI
////                .name("bearer-token") // tên schema
////                .bearerFormat("JWT") // (Optional) format token là JWT
////                .type(SecurityScheme.Type.HTTP) // loại xác thực là HTTP
////                .scheme("bearer") // sử dụng bearer schema
////                // ---> Cái này phải phải giữ nguyên là "bearer" vì đây là chuẩn của Bearer
// token authentication
////                .description("Please enter JWT token"); // Mô tả hiển thị trên UI
////    }
//    private SecurityScheme createSecuritySchema() {
//        return new SecurityScheme()
//                .name("bearer-token")
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//                .description("Please enter JWT token");
//    }
// }

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info =
                new Info()
                        .title("Identity service API")
                        .version("1.0")
                        .description("This is Identity service app endpoints");
        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes("bearer-token", createSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("bearer-token"));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("Bearer authentication token")
                .scheme("bearer")
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("jwt")
                .description("Please enter JWT token");
    }
}
