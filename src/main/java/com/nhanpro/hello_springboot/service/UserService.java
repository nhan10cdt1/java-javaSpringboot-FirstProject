package com.nhanpro.hello_springboot.service;

import com.nhanpro.hello_springboot.dto.request.ApiResponse;
import com.nhanpro.hello_springboot.dto.request.UserCreationRequest;
import com.nhanpro.hello_springboot.dto.request.UserUpdateRequset;
import com.nhanpro.hello_springboot.dto.response.UserResponse;
import com.nhanpro.hello_springboot.entity.User;
import com.nhanpro.hello_springboot.enums.Role;
import com.nhanpro.hello_springboot.exception.AppException;
import com.nhanpro.hello_springboot.exception.ErrorCode;
import com.nhanpro.hello_springboot.mapper.UserMapper;
import com.nhanpro.hello_springboot.repository.UserRepository;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private  final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        // duplicate confirm
        if (userRepository.existsByUserName(request.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

        //Mapper UserCreationRequest -> User
        User user = userMapper.toUser(request);

        //Encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);
        //Save to DB
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequset request) {
        //Check update user in DB.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        //Update
        userMapper.updateUser(user, request);

        //Return apiResponse
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
      User user  = userRepository.
                findByUserName(name).
                orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return  userRepository.findAll().stream()
                .map(userMapper:: toUserResponse).toList();
    }

    @PostAuthorize("returnObject.userName == authentication.name")
    public UserResponse getUser(String userId) {
        log.info("In method get user by Id");
        return userMapper.toUserResponse(
          userRepository.findById(userId)
                  .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_EXISTED))
        );

    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}