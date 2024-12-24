package com.devteria.identity_service.services;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.requests.UserUpdateRequest;
import com.devteria.identity_service.dtos.responses.UserResponse;
import com.devteria.identity_service.entities.Role;
import com.devteria.identity_service.entities.User;
import com.devteria.identity_service.exceptions.AppException;
import com.devteria.identity_service.enums.ErrorCode;
import com.devteria.identity_service.mapper.UserMapper;
import com.devteria.identity_service.repositories.RoleRepository;
import com.devteria.identity_service.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // tạm tắt để test
//        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoles()));
//        user.setRoles(roles);

        Set<String> roles = new HashSet<>();
        roles.add(com.devteria.identity_service.enums.Role.USER.name());

//        userRepository.save(user);
        // lưu xong lập tức user có Id
        // System.out.println(user.getId()); // Kết quả: UUID đã được sinh ra

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority ->
                log.info(grantedAuthority.getAuthority()));

        return userRepository.findAll().stream().map(
                userMapper::toUserResponse
        ).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(String id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return userMapper.toUserResponse(existingUser);
    }

    @Override
    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(existingUser);
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userMapper.updateUser(existingUser, request);
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoles()));
        existingUser.setRoles(roles);

        userRepository.save(existingUser);

        return userMapper.toUserResponse(existingUser);
    }

    @Override
    public String deleteUser(String id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userRepository.deleteById(id);

        return "User with id: " + id + " has been deleted successfully!";
    }
}
