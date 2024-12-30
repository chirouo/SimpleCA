package com.gx.ca.controller;

import com.gx.ca.entity.CaRDTO;
import com.gx.ca.mapper.CaRequest;
import com.gx.ca.mapper.Certificate;
import com.gx.ca.mapper.User;
import com.gx.ca.service.CaRequestService;
import com.gx.ca.utils.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@RestController()
@RequestMapping("cr")
public class CaRequestController {
    @Resource
    private CaRequestService caRequestService;
    @PostMapping("register")
    public Result register(@RequestBody CaRDTO caRDTO) throws MessagingException {
        return caRequestService.register(caRDTO, true);
    }
    @PostMapping("list")
    public Result listCr()
    {
        return caRequestService.listCr();
    }
    @PostMapping("listUserCr")
    public Result listUserCr(@RequestBody User user) {
        return caRequestService.listUserCr(user);
    }
    @PostMapping("get_key")
    public Result getKey(){
        return caRequestService.getKey();
    }
    @PostMapping("upload")
    public Result upload(@RequestParam("crFile") MultipartFile crFile,
                         @RequestParam("user_account") String account,
                         @RequestParam("user_email_address") String emailAddress){
        return caRequestService.upload(crFile, account, emailAddress);
    }

}
