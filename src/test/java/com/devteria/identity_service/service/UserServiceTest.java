package com.devteria.identity_service.service;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.responses.UserResponse;
import com.devteria.identity_service.entities.User;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.AppException;
import com.devteria.identity_service.repositories.UserRepository;
import com.devteria.identity_service.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository; // chỉ là mock nên có thể user đc save ko có id

    private UserCreationRequest request;
    private UserResponse response;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void init() {
        // 1. create mock data
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

        user = User.builder()
                .id("2ebuye1uybyeq")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        //Given
        // 2. define behavior of Repository
        // 2 cái repository, 1 cái return boolean(existsByUsername), 1 cái return user(.save) nên ta có 2 when
        when(userRepository.existsByUsername(anyString())).thenReturn(false);// success
        when(userRepository.save(any())).thenReturn(user);

        // when
        // 3. call service method
        var userResponse = userService.createUser(request);

        // then
        // 4. assert the result
        Assertions.assertThat(userResponse.getId()).isEqualTo("2ebuye1uybyeq");
        Assertions.assertThat(userResponse.getUsername()).isEqualTo("john");
    }

    @Test
    void createUser_userExisted_fail() {
        // when
        request.setUsername("Napoli");
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // then - assert exception
        // 2 tham số: loại ngoại lệ - đoạn code sẽ ném ra nó
        var exception = org.junit.jupiter.api.Assertions.assertThrows(AppException.class, () -> {
           userService.createUser(request);
        });

        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_EXISTED);
    }


}
//@SpringBootTest
//@TestPropertySource("/test.properties")
//public class UserServiceTest {
//    //Given
//    private UserCreationRequest request;
//    private UserResponse userResponse;
//    private User user;
//    private LocalDate dob;
//    @MockBean
//    private UserRepository userRepository;
//    @Autowired
//    private UserService userService;
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
//                .dob(dob)
//                .build();
//
//        userResponse = UserResponse.builder()
//                .id("rfw5hdudj890")
//                .username("john")
//                .firstName("John")
//                .lastName("Doe")
//                .dob(dob)
//                .build();
//
//        user = User.builder()
//                .id("rfw5hdudj890")
//                .username("john")
//                .firstName("John")
//                .lastName("Doe")
//                .dob(dob)
//                .build();
//    }
//
//    @Test
//    void createUser_requestValid_success() {
//        // define behavior of repo
//        when(userRepository.existsByUsername(anyString())).thenReturn(false);
//        when(userRepository.save(any())).thenReturn(user);
//
//        // call method service
//        UserResponse response = userService.createUser(request);
//
//        // assert the results
//        Assertions.assertThat(userResponse.getId()).isEqualTo("rfw5hdudj890");
//        Assertions.assertThat(userResponse.getUsername()).isEqualTo("john");
//    }
//
//    @Test
//    void createUser_requestInvalid_fail() {
//        // define behavior of repo
//        when(userRepository.existsByUsername(anyString())).thenReturn(true);
//
//        // assert the results
//        var exception = org.junit.jupiter.api.Assertions.assertThrows(AppException.class, () -> {
//            userService.createUser(request);
//        });
//
//        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_EXISTED);
//    }
//
//    @Test
//    @WithMockUser(username = "john", roles = {"USER"}) // dùng khi khi một user cụ thể hoặc một role cụ thể truy cập tài nguyên
//    void getMyInfo_requestValid_success() {
//        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
//
//        var userResponse = userService.getMyInfo();
//
//        Assertions.assertThat(userResponse.getUsername()).isEqualTo("john");
//        Assertions.assertThat(userResponse.getId()).isEqualTo("rfw5hdudj890");
//    }
//
//    @Test
//    @WithMockUser(username = "john")
//    void getMyInfo_invalidRequest_success() {
//        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));
//
//        var exception = org.junit.jupiter.api.Assertions.assertThrows(AppException.class,
//                () -> userService.getMyInfo());
//
//        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED);
//    }
//}
