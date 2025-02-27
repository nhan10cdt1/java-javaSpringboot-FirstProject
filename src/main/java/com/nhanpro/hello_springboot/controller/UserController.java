package com.nhanpro.hello_springboot.controller;

import com.nhanpro.hello_springboot.dto.request.UserCreationRequest;
import com.nhanpro.hello_springboot.dto.request.UserUpdateRequset;
import com.nhanpro.hello_springboot.entity.User;
import com.nhanpro.hello_springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    User createUser(@RequestBody  @Valid UserCreationRequest request){
        return  userService.createUser(request);
    }

    @GetMapping
    List<User> getUsers(){
        return  userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") String userId){
        return  userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequset request ){
        return userService.updateUser(userId,request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
