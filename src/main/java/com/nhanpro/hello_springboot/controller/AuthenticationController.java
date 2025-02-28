package com.nhanpro.hello_springboot.controller;

import com.nhanpro.hello_springboot.dto.request.ApiResponse;
import com.nhanpro.hello_springboot.dto.request.AuthenticationRequest;
import com.nhanpro.hello_springboot.dto.response.AuthenticationResponse;
import com.nhanpro.hello_springboot.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){

    boolean result = authenticationService.authentication(request);

    return  ApiResponse.<AuthenticationResponse>builder()
            .result(AuthenticationResponse.builder()
                    .authenticated((result))
                    .build())
            .build();

    }

}
