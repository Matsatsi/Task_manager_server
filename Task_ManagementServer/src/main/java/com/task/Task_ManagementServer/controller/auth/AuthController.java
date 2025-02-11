package com.task.Task_ManagementServer.controller.auth;

import com.task.Task_ManagementServer.Service.auth.AuthService;
import com.task.Task_ManagementServer.Service.jwt.UserService;
import com.task.Task_ManagementServer.dto.AuthenticationRequest;
import com.task.Task_ManagementServer.dto.AuthenticatorResponse;
import com.task.Task_ManagementServer.dto.SignUpRequest;
import com.task.Task_ManagementServer.dto.UserDto;
import com.task.Task_ManagementServer.entities.User;
import com.task.Task_ManagementServer.repositories.UserRepository;
import com.task.Task_ManagementServer.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, UserRepository userRepository, JwtUtil jwtUtil, UserService userService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> sighUpUser(@RequestBody SignUpRequest signUpRequest){
        if(authService.hasUserEmail(signUpRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exits with this email");
        UserDto createUserDto = authService.signUpUser(signUpRequest);
        if(createUserDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
        return  ResponseEntity.status(HttpStatus.CREATED).body(createUserDto);
    }

    @PostMapping("/login")
    public AuthenticatorResponse login(@RequestBody AuthenticationRequest authenticationRequest){
       try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
            ));
       }
       catch (BadCredentialsException e){
           throw new BadCredentialsException("Incorrect username or password");
       }

       final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        AuthenticatorResponse authenticatorResponse = new AuthenticatorResponse();

        if(optionalUser.isPresent()){

            authenticatorResponse.setJwt(jwtToken);
            authenticatorResponse.setUserId(optionalUser.get().getId());
            authenticatorResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticatorResponse;
    }
}
