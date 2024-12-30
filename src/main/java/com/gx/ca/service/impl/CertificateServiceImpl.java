package com.gx.ca.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gx.ca.X509.CaCertificateGenerator;
import com.gx.ca.mapper.*;
import com.gx.ca.service.CaOperationService;
import com.gx.ca.service.CaRequestService;
import com.gx.ca.service.CertificateService;
import com.gx.ca.service.CrlService;
import com.gx.ca.utils.MyMail;
import com.gx.ca.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author 26274
* @description 针对表【certificates】的数据库操作Service实现
* @createDate 2024-12-24 19:23:26
*/
@Service
public class CertificateServiceImpl extends ServiceImpl<CertificateMapper, Certificate>
    implements CertificateService{

    @Resource
    CaCertificateGenerator caCertificateGenerator;
    @Resource
    CaRequestService caRequestService;
    @Resource
    CertificateService certificateService;
    @Resource
    CrlService crlService;
    @Resource
    CaOperationService caOperationService;
    @Resource
    MyMail myMail;

    private String subject = "";

    private String text = "";


    @Override
    public Result audit(CaRequest cr) {
        subject = "";
        text = "";
        //cr state0审核中，1审核成功，2审核失败
        if(cr.getState() == 1) {
            //成功
            //delete cr in car 不用删除了 方便渲染是否通过审核
//            caRequestService.removeById(cr.getId());
            //add cr in ca
            Certificate ca = new Certificate();
            BeanUtil.copyProperties(cr, ca,"id", "created_at", "updated_at", "deleted_at", "state");
            //setTime
            ca.setCreatedAt(new Date());
            ca.setUpdatedAt(new Date());
            ca.setState(0);//状态（0 代表在使用中，1代表已撤销/删除2代表过期）
//            ca.setRequestId(2222);
            //默认证书的有效期是7天
            ca.setExpireTime((long)7 * 24 * 60 * 60);
            long expireTimeInMillis = ca.getExpireTime() * 1000; // 转换为毫秒
            ca.setDeletedAt(new Date(ca.getUpdatedAt().getTime() + expireTimeInMillis));
            boolean success = certificateService.save(ca);
            if(success){
                caCertificateGenerator.saveAsCAFile(ca);
                //记录日志
                caOperationService.saveOperationOnCA(ca,0);
                subject += "尊敬的用户，您的CA证书已下发，请登录客户端查收";
                text += "感谢您对我们平台的信任";
                myMail.sendSimpleMail(ca.getEmailAddress(), subject, text);
            }
        }else if(cr.getState() == 2) {
            //失败
            //不做任何操作，更新一下数据库即可
            subject += "尊敬的用户，您的CA证书申请失败！";
            text += "感谢您对我们平台的信任";
            myMail.sendSimpleMail(cr.getEmailAddress(), subject, text);
            caRequestService.updateById(cr);
//            caRequestService.removeById(cr.getId());
        }
        return Result.ok();
    }

    //更新ca的基本信息，但是不能删除和撤销删除（这两个有单独的api）
    @Override
    public Result update_ca(Certificate ca) {
        long expireTimeInMillis = ca.getExpireTime() * 1000; // 转换为毫秒
        ca.setDeletedAt(new Date(ca.getUpdatedAt().getTime() + expireTimeInMillis));
        boolean success = certificateService.updateById(ca);
        if(success){
            caOperationService.saveOperationOnCA(ca, 1);
        }
        return Result.ok(ca);
    }
    @Override
    public Result deleteCa(Certificate ca) {
        ca.setState(1);
        boolean b = certificateService.updateById(ca);
        if(b) {
            Crl crl = new Crl();
            crl.setCreatedAt(new Date());
            crl.setUpdatedAt(new Date());
            crl.setCertificateId(ca.getId());
            crlService.save(crl);
            caOperationService.saveOperationOnCA(ca, 2);
            return Result.ok("删除成功");
        }else {
            return Result.fail("删除失败");
        }
    }
    @Override
    public Result recoverCa(Certificate ca) {
        ca.setState(0);
        // 假设 expireTime 是以秒为单位
        long expireTimeInSeconds = 7 * 24 * 60 * 60;  // 1天的秒数
        ca.setExpireTime(expireTimeInSeconds);
        // 获取当前时间
        Date now = new Date();
        // 将 expireTime 转换为毫秒并加到当前时间上
        long expireTimeInMillis = expireTimeInSeconds * 1000;
        Date expireDate = new Date(now.getTime() + expireTimeInMillis);
        // 设置到 ca 的 deletedAt 字段
        ca.setDeletedAt(expireDate);
        boolean b = certificateService.updateById(ca);
        if(b) {
            LambdaQueryWrapper<Crl> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Crl::getCertificateId, ca.getId());
            boolean success = crlService.remove(queryWrapper);
            if(success){
                caOperationService.saveOperationOnCA(ca, 3);
            }
            return Result.ok("撤销成功");
        }else {
            return Result.fail("撤销失败");
        }
    }

    //合法的列表
    @Override
    public Result listCa() {
        //判断是否过期
        isExpired();
        List<Certificate> certificates = new ArrayList<>();
        List<Certificate> list = certificateService.list();
        for (Certificate certificate : list) {
            if(certificate.getState() == 0){
                certificates.add(certificate);
            }
        }
        return Result.ok(certificates);
    }

    //撤销 删除的列表
    @Override
    public Result revocation_list() {
        //判断是否过期
        isExpired();
        List<Certificate> certificates = new ArrayList<>();
        List<Certificate> list = certificateService.list();
        for (Certificate certificate : list) {
            if(certificate.getState() == 1){
                certificates.add(certificate);
            }
        }
        return Result.ok(certificates);
    }
    //过期的列表
    public Result isExpired(){
        List<Certificate> certificates = new ArrayList<>();
        List<Certificate> list = certificateService.list();
        Date now = new Date();
        for (Certificate certificate : list) {
            Date deletedAt = certificate.getDeletedAt();
            if(deletedAt.before(now)){
                //0有效 1撤销/删除 2过期
                certificate.setState(2);
                certificates.add(certificate);
            }
        }
        certificateService.updateBatchById(certificates);
        return Result.ok(certificates);
    }

    @Override
    public Result operationLogs() {
        List<CaOperation> operations = caOperationService.list();
        return Result.ok(operations);
    }

    @Override
    public Result listUserCA(User user) {
        isExpired();
        LambdaQueryWrapper<Certificate> CAQueryWrapper = new LambdaQueryWrapper<>();
        CAQueryWrapper.eq(Certificate::getUserAccount, user.getAccount());
        //返回所有的 让前端渲染 state0 1 2 （合法 被撤回 过期）
        List<Certificate> userCAs = list(CAQueryWrapper);
        return Result.ok(userCAs);
    }
}




