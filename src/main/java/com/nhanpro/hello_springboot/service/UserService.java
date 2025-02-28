package com.nhanpro.hello_springboot.service;

import com.nhanpro.hello_springboot.dto.request.UserCreationRequest;
import com.nhanpro.hello_springboot.dto.request.UserUpdateRequset;
import com.nhanpro.hello_springboot.dto.response.UserResponse;
import com.nhanpro.hello_springboot.entity.User;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(UserCreationRequest request){
        if(userRepository.existsByUserName(request.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequset request){
        //tim kieu user update co hay khong
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        //Thuc hien update thong tin
        userMapper.updateUser(user,request);

        //Tra ve Response
        return  userMapper.toUserResponse(userRepository.save(user));
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public UserResponse getUser(String userId){
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));

    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}