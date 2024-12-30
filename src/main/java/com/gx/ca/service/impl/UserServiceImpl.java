package com.gx.ca.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gx.ca.mapper.User;
import com.gx.ca.service.UserService;
import com.gx.ca.mapper.UserMapper;
import com.gx.ca.utils.Result;
import org.springframework.stereotype.Service;

/**
* @author 26274
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-12-24 09:15:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public Result login(User user) {
//        System.out.println(user);
        String account = user.getAccount();
        String password = user.getPassword();
        //password用MD5加密进入的数据库,保护个人隐私,因此这里需要用MD5加密后的password去比较
        password = SecureUtil.md5(password);

        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getAccount, account);
        User getByAccount = getOne(userWrapper);
//        System.out.println("账号:" + account);
//        System.out.println("密码:" + password);
        if (getByAccount == null) return Result.fail("用户不存在");
        if (!password.equals(getByAccount.getPassword())) return Result.fail("密码错误");
        return Result.ok(getByAccount);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public Result register(User user) {
        String account = user.getAccount();
        User userByAccount= searchByAccount(account);
        if (userByAccount != null)
        {
            return Result.fail("用户 " + user.getUsername()+ " 账号已存在!");
        }
        //将用户的密码用MD5加密
        user.setPassword(SecureUtil.md5(user.getPassword()));
        save(user);
        return Result.ok("注册成功!");
    }
    /**
     * 精准搜索用户(通过账号)
     * @param account
     * @return
     */
    @Override
    public User searchByAccount(String account) {
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getAccount, account);
        User user = getOne(userWrapper);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public Result userList() {
        return Result.ok(list());
    }
}




