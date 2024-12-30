package com.gx.ca.service;

import com.gx.ca.mapper.CaRequest;
import com.gx.ca.mapper.Certificate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gx.ca.mapper.User;
import com.gx.ca.utils.Result;

/**
* @author 26274
* @description 针对表【certificates】的数据库操作Service
* @createDate 2024-12-24 19:23:27
*/

public interface CertificateService extends IService<Certificate> {

    Result audit(CaRequest cr);

    Result update_ca(Certificate ca);

    Result deleteCa(Certificate ca);

    Result recoverCa(Certificate ca);

    Result listCa();

    Result revocation_list();

    Result isExpired();

    Result operationLogs();

    Result listUserCA(User user);
}
