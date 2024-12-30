package com.gx.ca.controller;

import com.gx.ca.mapper.CaRequest;
import com.gx.ca.mapper.Certificate;
import com.gx.ca.mapper.User;
import com.gx.ca.service.CertificateService;
import com.gx.ca.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@RestController()
@RequestMapping("ca")
public class CertificatesController {
    @Resource
    CertificateService caService;
    @PostMapping("list")
    public Result listCa(){
        return caService.listCa();
    }
    @PostMapping("expired_list")
    public  Result expired_list(){
        return caService.expired_list();
    }
    @PostMapping("revocation_list")
    public  Result revocation_list(){
        return caService.revocation_list();
    }
    @PostMapping("audit")
    public Result audit(@RequestBody CaRequest cr)
    {
        return caService.audit(cr);
    }
    @PostMapping("update_ca_expeirtime")
    public Result update_ca_expeirtime(@RequestBody Certificate ca)
    {
        return caService.update_ca(ca);
    }
    @PostMapping("delete_ca")
    public Result deleteCa(@RequestBody Certificate ca){
        return caService.deleteCa(ca);
    }
    @PostMapping("recover_ca")
    public Result recoverCa(@RequestBody Certificate ca){
        return caService.recoverCa(ca);
    }
    @PostMapping("log")
    public Result operationLogs(){
        return caService.operationLogs();
    }
    @PostMapping("listUserCA")
    public Result pastList(@RequestBody User user){
        return caService.listUserCA(user);
    }


}
