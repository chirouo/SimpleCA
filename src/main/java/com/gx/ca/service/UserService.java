package com.gx.ca.service;

import com.gx.ca.mapper.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gx.ca.utils.Result;

/**
* @author 26274
* @description 针对表【user】的数据库操作Service
* @createDate 2024-12-24 09:15:16
*/
public interface UserService extends IService<User> {
    Result login(User user);

    Result register(User user);

    User searchByAccount(String account);

    Result userList();
}
