package com.gx.ca.service;

import com.gx.ca.mapper.Crl;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gx.ca.utils.Result;

/**
* @author 26274
* @description 针对表【crls(证书吊销列表)】的数据库操作Service
* @createDate 2024-12-24 20:02:07
*/

public interface CrlService extends IService<Crl> {
    public Result listCrl();
}
