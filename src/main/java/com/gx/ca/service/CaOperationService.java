package com.gx.ca.service;

import com.gx.ca.mapper.CaOperation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gx.ca.mapper.Certificate;

/**
* @author 26274
* @description 针对表【ca_operation】的数据库操作Service
* @createDate 2024-12-25 16:26:30
*/
public interface CaOperationService extends IService<CaOperation> {

    boolean saveOperationOnCA(Certificate ca, Integer operation);
}
