package com.wxf.springsecuritydemo.controller;

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
