package com.gx.ca.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gx.ca.entity.CaRDTO;
import com.gx.ca.mapper.CaRequest;
import com.gx.ca.mapper.User;
import com.gx.ca.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

/**
 * @author 26274
 * @description 针对表【ca_requests】的数据库操作Service
 * @createDate 2024-12-24 16:00:12
 */
public interface CaRequestService extends IService<CaRequest> {

    Result register(CaRDTO caRDTO, boolean isSave) throws MessagingException;

    //合法的CR
    Result listCr();

    Result getKey();

    Result upload(MultipartFile crFile, String account,String emailAddress);

    Result listUserCr(User user);
}
