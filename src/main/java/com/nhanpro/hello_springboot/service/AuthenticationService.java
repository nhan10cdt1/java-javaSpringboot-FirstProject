package com.nhanpro.hello_springboot.service;

import com.nhanpro.hello_springboot.dto.request.AuthenticationRequest;
import com.nhanpro.hello_springboot.exception.AppException;
import com.nhanpro.hello_springboot.exception.ErrorCode;
import com.nhanpro.hello_springboot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private final UserRepository userRepository;
    public boolean authentication(AuthenticationRequest authenticationRequest ){
        var user = userRepository.findByUserName(authenticationRequest.getUserName())
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        return  passwordEncoder.matches(authenticationRequest.getPassword(),user.getPassword());
    }
}
