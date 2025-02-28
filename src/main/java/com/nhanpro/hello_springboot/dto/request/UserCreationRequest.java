package com.nhanpro.hello_springboot.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class UserCreationRequest {
    @Size(min = 3 , message = "USERNAME_INVALID")
    private String userName;

    @Size(min = 6 , message = "PASSWORD_INVALID")
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;


}