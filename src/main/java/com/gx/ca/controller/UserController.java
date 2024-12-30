package com.gx.ca.controller;

import com.gx.ca.mapper.User;
import com.gx.ca.service.UserService;
import com.gx.ca.utils.MyMail;
import com.gx.ca.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService usersService;
    @PostMapping("login")
    public Result login(@RequestBody User user)
    {
        return usersService.login(user);
    }
    @PostMapping("register")
    public Result register(@RequestBody User user)
    {
        return usersService.register(user);
    }
    @PostMapping("list")
    public Result userList()
    {
        return usersService.userList();
    }
}
