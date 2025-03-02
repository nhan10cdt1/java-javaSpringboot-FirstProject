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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

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

    public List<UserResponse> getUsers() {
        return userMapper.toListUserResponse(userRepository.findAll());
    }

    public ApiResponse<UserResponse> getUser(String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))));
        return apiResponse;

    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}