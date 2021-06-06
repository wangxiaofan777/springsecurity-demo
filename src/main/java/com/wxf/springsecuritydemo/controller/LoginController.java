package com.wxf.springsecuritydemo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录
 */
@Controller
public class LoginController {

    /*@GetMapping("/login")
    public String login() {
        return "/login.html";
    }*/

    // 开启角色访问控制
//    @Secured("ROLE_abc")
    // 用于权限校验，PreAuthorize与PostAuthorize区别是PreAuthorize调用前判断
    @PreAuthorize("hasRole('ROLE_abc')")
    @PostMapping("/toMain")
    public String main() {
        return "redirect:main.html";
    }

    @PostMapping("/toErr")
    public String error() {
        return "redirect:error.html";
    }

    @GetMapping(value = "/demo")
    @ResponseBody
    public String demo() {
        return "demo";
    }
}
