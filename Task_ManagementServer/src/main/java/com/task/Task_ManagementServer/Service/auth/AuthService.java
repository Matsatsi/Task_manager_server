package com.task.Task_ManagementServer.Service.auth;

import com.task.Task_ManagementServer.dto.SignUpRequest;
import com.task.Task_ManagementServer.dto.UserDto;

public interface AuthService {

   UserDto signUpUser(SignUpRequest signUpRequest);

   boolean hasUserEmail(String email);
}
