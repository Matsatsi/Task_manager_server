package com.task.Task_ManagementServer.Service.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


public interface UserService {

    UserDetailsService userDetailsService();

    UserDetails loadUserByUsername(String userEmail);
}
