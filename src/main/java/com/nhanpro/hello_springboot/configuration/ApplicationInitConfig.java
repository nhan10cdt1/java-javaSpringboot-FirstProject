package com.nhanpro.hello_springboot.configuration;

import com.nhanpro.hello_springboot.dto.response.UserResponse;
import com.nhanpro.hello_springboot.entity.User;
import com.nhanpro.hello_springboot.enums.Role;
import com.nhanpro.hello_springboot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
         return args -> {
             if(userRepository.findByUserName("admin").isEmpty()){
                 var roles = new HashSet<String>();
                 roles.add(Role.ADMIN.name());

                 User user = User.builder()
                         .userName("admin")
                         .roles(roles)
                         .password(passwordEncoder.encode("admin"))
                         .build();

                 userRepository.save(user);
                 log.warn("admin user has been created with default password : admin, please change it ");
             }
         }   ;
    }


}
