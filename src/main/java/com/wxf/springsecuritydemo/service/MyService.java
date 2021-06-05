package com.wxf.springsecuritydemo.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface MyService {

    public boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
