package com.nhanpro.hello_springboot.service;

import com.nhanpro.hello_springboot.dto.request.AuthenticationRequest;
import com.nhanpro.hello_springboot.dto.request.IntrospectRequest;
import com.nhanpro.hello_springboot.dto.response.AuthenticationResponse;
import com.nhanpro.hello_springboot.dto.response.IntrospectResponse;
import com.nhanpro.hello_springboot.exception.AppException;
import com.nhanpro.hello_springboot.exception.ErrorCode;
import com.nhanpro.hello_springboot.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private final UserRepository userRepository;


    @NonFinal
    @Value("${jwt.signerKey}")
    protected static String SIGNER_KEY ;


    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier  = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        return  IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest ){

        var user = userRepository.findByUserName(authenticationRequest.getUserName())
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_EXISTED));

        //Chek xem User va password da dung hay chua
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(),
                user.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        //Tao token
        var token = generateToken(authenticationRequest.getUserName());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    private String generateToken(String userName){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .issuer("nhannguyen.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().
                        plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("customClain", "Custom")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload );

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return  jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create  token" ,e);
            throw new RuntimeException(e);

        }
    }
}
