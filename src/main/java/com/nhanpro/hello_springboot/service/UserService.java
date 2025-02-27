package com.nhanpro.hello_springboot.service;

import com.nhanpro.hello_springboot.dto.request.UserCreationRequest;
import com.nhanpro.hello_springboot.dto.request.UserUpdateRequset;
import com.nhanpro.hello_springboot.entity.User;
import com.nhanpro.hello_springboot.exception.AppException;
import com.nhanpro.hello_springboot.exception.ErrorCode;
import com.nhanpro.hello_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request){
        User user = new User();

        if(userRepository.existsByUserName(request.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

        user.setUserName (request.getUserName());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);

    }

    public User updateUser(String userId, UserUpdateRequset request){
        User user = getUser(userId);

        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return  userRepository.save(user);
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(String userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));


    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
