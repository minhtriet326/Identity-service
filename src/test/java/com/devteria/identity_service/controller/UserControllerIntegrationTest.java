package com.devteria.identity_service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.responses.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// @Slf4j
// @SpringBootTest // để tạo test context
// @AutoConfigureMockMvc
// @Testcontainers
// public class UserControllerIntegrationTest {
//    // taoj 1 MySQL container và connect tới
//    @Container
//    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
//
//    @DynamicPropertySource
//    static void configureDatasource(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
//        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
//        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
//        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
//    }
//
//    @Autowired
//    private MockMvc mvc;
//    private UserCreationRequest request;
//    private UserResponse response;
//    private LocalDate dob;
//
//    @BeforeEach// hàm này sẽ run trước so với tất cả @Test
//    void initData() {
//        dob = LocalDate.of(1990, 1, 21);
//
//        request = UserCreationRequest.builder()
//                .username("john")
//                .firstName("John")
//                .lastName("Doe")
//                .password("12345678")
//                .dob(dob)
//                .build();
//
//        response = UserResponse.builder()
//                .id("a24577va56789")
//                .username("john")
//                .firstName("John")
//                .lastName("Doe")
//                .dob(dob)
//                .build();
//    }
//
//    @Test
//    void createUser_validRequest_success() throws Exception {
//        log.info("Hello test");
//        // GIVEN - input
//        ObjectMapper mapper = new ObjectMapper();
//
//        // "Này, đây là JavaTimeModule, nó sẽ giúp bạn xử lý các kiểu dữ liệu thời gian trong Java
// 8"
//        mapper.registerModule(new JavaTimeModule());
//
//        String content = mapper.writeValueAsString(request);
//
//        // WHEN - conduct // THEN - output
//        var result = mvc.perform(post("/api/v1/user/createUser")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(content))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
//                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("john"));
//    }
// }
@SpringBootTest
@AutoConfigureMockMvc // test controller
@Testcontainers
public class UserControllerIntegrationTest {
  @Container static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

  // properties of MY_SQL_CONTAINER
  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    registry.add("spring.datasource.driverClassName", MY_SQL_CONTAINER::getDriverClassName);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
  }

  @Autowired private MockMvc mvc;

  private UserCreationRequest request;
  private UserResponse response;
  private LocalDate dob;

  @BeforeEach
  void init() {
    dob = LocalDate.of(2002, 9, 2);

    request =
        UserCreationRequest.builder()
            .username("john")
            .password("12345")
            .firstName("John")
            .lastName("Doe")
            .build();

    response =
        UserResponse.builder()
            .id("2ebuye1uybyeq")
            .username("john")
            .firstName("John")
            .lastName("Doe")
            .dob(dob)
            .build();
  }

  @Test
  void createUser_validRequest_success() throws Exception {
    // GIVEN - input
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String content = mapper.writeValueAsString(request);

    // Then - output
    var result =
        mvc.perform(
                post("/api/v1/user/createUser")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("john"));
  }
}
// @SpringBootTest
// @AutoConfigureMockMvc
// @Slf4j
// @Testcontainers
// public class UserControllerIntegrationTest {
//    @Container
//    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
//    @DynamicPropertySource
//    static void configureDatasource(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
//        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
//        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
//        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
//    }
//    @Autowired
//    private MockMvc mvc;
//    private UserCreationRequest request;
//    private UserResponse response;
//    private LocalDate dob;
//
//    @BeforeEach
//    void initData() {
//        dob = LocalDate.of(2002, 9, 2);
//
//        request = UserCreationRequest.builder()
//                .username("john")
//                .password("12345")
//                .firstName("John")
//                .lastName("Doe")
//                .build();
//
//        response = UserResponse.builder()
//                .id("2ebuye1uybyeq")
//                .username("john")
//                .firstName("John")
//                .lastName("Doe")
//                .dob(dob)
//                .build();
//    }
//
//    @Test
//    void createUser_validRequest_success() throws Exception {
//        // when
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        String content = mapper.writeValueAsString(request);
//
//        // then
//        var result = mvc.perform(post("/api/v1/user/createUser")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE) // Cho server biết dữ liệu
// (mà request sắp) gửi
// lên là định dạng JSON
//                        .content(content)) // body của request
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
//                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("john"))
//                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("John"))
//                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value("Doe"));
//
//        log.info("Result: {}", result.andReturn().getResponse().getContentAsString());
//    }
// }
