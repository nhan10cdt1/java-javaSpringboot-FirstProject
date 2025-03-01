package com.nhanpro.hello_springboot.controller;

import com.nhanpro.hello_springboot.dto.request.ApiResponse;
import com.nhanpro.hello_springboot.dto.request.AuthenticationRequest;
import com.nhanpro.hello_springboot.dto.request.IntrospectRequest;
import com.nhanpro.hello_springboot.dto.response.AuthenticationResponse;
import com.nhanpro.hello_springboot.dto.response.IntrospectResponse;
import com.nhanpro.hello_springboot.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){

    // Get token
    var token = authenticationService.authentication(request);

    return  ApiResponse.<AuthenticationResponse>builder()
            .result(token)
            .build();

    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
    throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return  ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();

    }

}
