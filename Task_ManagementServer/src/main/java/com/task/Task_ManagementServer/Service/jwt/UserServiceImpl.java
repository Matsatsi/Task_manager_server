package com.task.Task_ManagementServer.Service.jwt;

import com.task.Task_ManagementServer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//class that helps with user authentication by fetching user details from a database using UserRepository
//helps with login Authentication

@Service
@RequiredArgsConstructor
//Automatically creates a constructor for userRepository.
public class UserServiceImpl implements UserService{
//UserDetailsService: A built-in Spring Security interface to load user details.

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            //UsernameNotFoundException: Thrown when a user is not found in the database.
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findFirstByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            }
        };
    }
}
