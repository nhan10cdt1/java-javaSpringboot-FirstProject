package com.nhanpro.hello_springboot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    private String id ;

    private String userName;
    //its do not return
    //private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    Set<String> roles;
}
