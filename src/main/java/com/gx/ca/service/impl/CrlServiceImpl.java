package com.gx.ca.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gx.ca.mapper.Crl;
import com.gx.ca.service.CrlService;
import com.gx.ca.mapper.CrlMapper;
import com.gx.ca.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 26274
* @description 针对表【crls(证书吊销列表)】的数据库操作Service实现
* @createDate 2024-12-24 20:02:07
*/
@Service
public class CrlServiceImpl extends ServiceImpl<CrlMapper, Crl>
    implements CrlService{
    @Resource
    CrlService crlService;
    public Result listCrl()
    {
        return Result.ok(crlService.list());
    }
}




