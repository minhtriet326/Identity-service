package com.devteria.identity_service.service;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.responses.UserResponse;
import com.devteria.identity_service.entities.User;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.exceptions.AppException;
import com.devteria.identity_service.mapper.UserMapper;
import com.devteria.identity_service.repositories.RoleRepository;
import com.devteria.identity_service.repositories.UserRepository;
import com.devteria.identity_service.services.UserService;
import lombok.With;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository; // chỉ là mock nên có thể user đc save ko có id
    @MockBean
    private RoleRepository roleRepository;

    private UserCreationRequest userRequest;
    private UserResponse userResponse;
    private User user, user1;
    private List<User> userList = new ArrayList<>();
//    private List<UserResponse> userResponseList= new ArrayList<>();
    private LocalDate dob;

    @BeforeEach
    void init() {
        // 1. create mock data
        dob = LocalDate.of(2002, 9, 2);

        userRequest = UserCreationRequest.builder()
                .username("john")
                .password("12345")
                .firstName("John")
                .lastName("Doe")
                .build();

        userResponse = UserResponse.builder()
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
        user1 = User.builder()
                .id("t5fgshjsi8")
                .username("john1")
                .firstName("John1")
                .lastName("Doe1")
                .dob(dob)
                .build();

        userList.add(user);
        userList.add(user1);
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
        var userResponse = userService.createUser(userRequest);

        // then
        // 4. assert the result
        Assertions.assertThat(userResponse.getId()).isEqualTo("2ebuye1uybyeq");
        Assertions.assertThat(userResponse.getUsername()).isEqualTo("john");
    }

    @Test
    void createUser_userExisted_fail() {
        // when
        userRequest.setUsername("Napoli");
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // then - assert exception
        // 2 tham số: loại ngoại lệ - đoạn code sẽ ném ra nó
        var exception = org.junit.jupiter.api.Assertions.assertThrows(AppException.class, () -> {
           userService.createUser(userRequest);
        });

        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_EXISTED);
    }

    @Test
    @WithMockUser(username = "john")
    void getMyInfo_validRequest_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getMyInfo();

        Assertions.assertThat(userResponse.getId()).isEqualTo("2ebuye1uybyeq");
        Assertions.assertThat(userResponse.getUsername()).isEqualTo("john");
    }

    @Test
    @WithMockUser(username = "john")
    void getMyInfo_invalidRequest_fail() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        var exception = org.junit.jupiter.api.Assertions.assertThrows(AppException.class,
                () -> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED);
    }

    @Test
    @WithMockUser
    void getAllUser_validRequest_success() {
        when(userRepository.findAll()).thenReturn(userList);

        List<UserResponse> userResponseList = userService.getAllUsers();

        Assertions.assertThat(userResponseList).isNotEmpty();
        org.junit.jupiter.api.Assertions.assertEquals(2, userResponseList.size());
    }

    @Test
    @WithMockUser
    void getAllUser_invalidRequest_emptyList() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        List<UserResponse> userResponseList = userService.getAllUsers();

        Assertions.assertThat(userResponseList).isEmpty();
        org.junit.jupiter.api.Assertions.assertEquals(0, userResponseList.size());
    }

    @Test
    void getUserById_validRequest_success() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        UserResponse userResponse1 = userService.getUserById("2ebuye1uybyeq");

        Assertions.assertThat(userResponse1).isNotNull();
        Assertions.assertThat(userResponse1.getId()).isEqualTo("2ebuye1uybyeq");
    }

    @Test
    void getUserById_invalidRequest_wrongId() {
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

        var exception = org.junit.jupiter.api.Assertions.assertThrows(AppException.class,
                () -> userService.getUserById(anyString()));

        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED);
    }
}
//@SpringBootTest
//@TestPropertySource("/application-test.properties")
//public class UserServiceTest {
//    //Given
//    private UserCreationRequest userRequest;
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
//        userRequest = UserCreationRequest.builder()
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
//        UserResponse response = userService.createUser(userRequest);
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
//            userService.createUser(userRequest);
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
//    void getMyInfo_invalidRequest_fail() {
//        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));
//
//        var exception = org.junit.jupiter.api.Assertions.assertThrows(AppException.class,
//                () -> userService.getMyInfo());
//
//        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED);
//    }
//}
