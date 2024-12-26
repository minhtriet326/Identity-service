package com.devteria.identity_service.controller;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.responses.UserResponse;
import com.devteria.identity_service.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@Slf4j
//@SpringBootTest // để tạo test context
//@AutoConfigureMockMvc
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mvc;
//    @MockBean // để đưa mock vào context
//    private UserService userService;
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
//        // "Này, đây là JavaTimeModule, nó sẽ giúp bạn xử lý các kiểu dữ liệu thời gian trong Java 8"
//        mapper.registerModule(new JavaTimeModule());
//
//        String content = mapper.writeValueAsString(request);
//
//        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);
//
//        // WHEN - conduct // THEN - output
//        mvc.perform(
//                post("/api/v1/user/createUser")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(content))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
//    }
//}
//@SpringBootTest
//@AutoConfigureMockMvc // test controller
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mvc;
//    private UserCreationRequest request;
//    private UserResponse response;
//    private LocalDate dob;
//    @MockBean
//    private UserService userService;
//
//    @BeforeEach
//    void init() {
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
//        // GIVEN - input
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        String content = mapper.writeValueAsString(request);
//
//        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);
//
//        // Then - output
//        mvc.perform(post("/api/v1/user/createUser")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(content)
//                ).andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
//    }
//
//    @Test
//    void createUser_usernameInvalid_fail() throws Exception {
//        request.setUsername("jo");
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        String content = mapper.writeValueAsString(request);
//
//        // bỏ vì validation diễn ra trước nên ko xuống service
//        // Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);
//
//        mvc.perform(post("/api/v1/user/createUser")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(content))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003))
//                .andExpect(MockMvcResultMatchers.jsonPath("message")
//                        .value("Username must be at least 3 characters"));
//    }
//}
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties") // những cái properties dùng trong test
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2002, 9, 2);

        request = UserCreationRequest.builder()
                .username("john")
                .password("12345")
                .firstName("John")
                .lastName("Doe")
                .build();

        response = UserResponse.builder()
                .id("2ebuye1uybyeq")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // when
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String content = mapper.writeValueAsString(request);

        Mockito.when(userService.createUser(any())).thenReturn(response);

        // then
        mvc.perform(post("/api/v1/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON_VALUE) // Cho server biết dữ liệu (mà request sắp) gửi lên là định dạng JSON
                        .content(content)) // body của request
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("2ebuye1uybyeq"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value("Doe"));
    }

    @Test
    void createUser_invalidRequest_fail() throws Exception {
        request.setUsername("12");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String content = mapper.writeValueAsString(request);

        mvc.perform(post("/api/v1/user/createUser")
                .contentType("application/json")
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1003"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 3 characters"));
    }
}
