package com.task.Task_ManagementServer.Service.auth;

import com.task.Task_ManagementServer.dto.SignUpRequest;
import com.task.Task_ManagementServer.dto.UserDto;
import com.task.Task_ManagementServer.entities.User;
import com.task.Task_ManagementServer.enums.UserRole;
import com.task.Task_ManagementServer.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public  void  createAdminAccount(){
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
        if(optionalUser.isEmpty()){
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            userRepository.save(user);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));//hash the password
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);
            System.out.println("Admin account created successfully");
        }else{
            System.out.println("Admin account already exists");
        }

    }

    @Override
    public UserDto signUpUser(SignUpRequest signUpRequest) {

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        user.setUserRole(UserRole.EMPLOYEE);
        User createUser =userRepository.save(user);

        return  createUser.getUserDto();

    }

    @Override
    public boolean hasUserEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
