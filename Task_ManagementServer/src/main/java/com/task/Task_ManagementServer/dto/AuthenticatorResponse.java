package com.task.Task_ManagementServer.dto;

import com.task.Task_ManagementServer.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticatorResponse {
    private String jwt;

    private Long userId;

    private UserRole userRole;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
