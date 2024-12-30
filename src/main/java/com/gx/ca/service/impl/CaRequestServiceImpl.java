package com.gx.ca.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gx.ca.X509.CSRGenerator;
import com.gx.ca.X509.CSRParser;
import com.gx.ca.X509.MyRSA;
import com.gx.ca.entity.CaRDTO;
import com.gx.ca.mapper.CaRequest;
import com.gx.ca.mapper.CaRequestMapper;
import com.gx.ca.mapper.User;
import com.gx.ca.service.CaRequestService;
import com.gx.ca.utils.MyMail;
import com.gx.ca.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author 26274
 * @description 针对表【ca_requests】的数据库操作Service实现
 * @createDate 2024-12-24 16:00:12
 */
@Service
public class CaRequestServiceImpl extends ServiceImpl<CaRequestMapper, CaRequest>
        implements CaRequestService{
    @Resource
    CaRequestService caRequestService;
    @Resource
    CSRParser csrParser;
    @Resource
    MyRSA myRSA;
    @Resource
    CSRGenerator csrGenerator;
    @Resource
    MyMail myMail;
    private String subject = "";
    private String text = "";
    @Override
    public Result register(CaRDTO caRDTO, boolean isSave) {
        subject = "";
        text = "";
        if (caRDTO.getAutoGenerate() == 1) {
            try {
                caRDTO.setPublicKey(myRSA.generateKeys().get("publicKey"));
                caRDTO.setPrivateKey(myRSA.generateKeys().get("privateKey"));
            } catch (Exception e) {
                e.printStackTrace();
                // 处理异常
            }
        }
        CaRequest cr = new CaRequest();
        BeanUtil.copyProperties(caRDTO, cr, "autoGenerate", "pemFile");
        String account = cr.getUserAccount();
        LambdaQueryWrapper<CaRequest> caRequestWrapper = new LambdaQueryWrapper<>();
        caRequestWrapper.eq(CaRequest::getUserAccount, account);
        List<CaRequest> caRequestList = caRequestService.list(caRequestWrapper);
        if(caRequestList.size() > 3) {
            subject += "用户您好，您的CA申请被驳回!";
            text += "原因：CA申请已达上限";
            myMail.sendSimpleMail(caRDTO.getEmailAddress(), subject, text);
            return Result.fail("已经有三个CA证书正在申请受理中，请耐心等待！");
        }else{
            boolean flag = true;
            //公钥到这一步 已经有了
            if(isSave){
                //含有publickey字符串
                flag = saveDataAndGenerateFile(cr);
            }else{
                //提前上传了csr文件， 这里存入数据库即可
                flag = save(cr);
            }
            if(flag) {
//                myMail.sendSimpleMail();
                subject += "用户您好，您的CA申请已通过！";
                text += "您的申请已通过，您的证书已发放，请查看！";
                myMail.sendSimpleMail(caRDTO.getEmailAddress(), subject, text);
                return Result.ok(cr);
            } else return Result.fail("出现异常");
        }
    }
    //合法的CR
    @Override
    public Result listCr() {
        List<CaRequest> caRequests = new ArrayList<>();
        List<CaRequest> list = caRequestService.list();
        for (CaRequest caRequest : list) {
            if(caRequest.getState() == 0){
                caRequests.add(caRequest);
            }
        }
        return Result.ok(caRequests);
    }

    @Override
    public Result getKey() {
        return Result.ok(myRSA.generateKeys());
    }





    @Value("${files.upload.cr-path}")
    private  String crPath;
    @Override
    public Result upload(MultipartFile crFile, String account, String emailAddress) {
        // 获取上传文件的原始文件名
        String originalFileName = crFile.getOriginalFilename();

        // 创建目标文件对象
        File destFile = new File(crPath + originalFileName);

        try {
            // 将文件保存到指定目录
            crFile.transferTo(destFile);

            // 如果保存成功，可以返回成功的响应
            CaRDTO caRDTO = csrParser.parserCrFile(destFile.getPath());
            caRDTO.setUserAccount(account);
            caRDTO.setAutoGenerate(0);
            caRDTO.setState(1);
            caRDTO.setEmailAddress(emailAddress);
            return register(caRDTO, false);
        } catch (IOException e) {
            // 处理文件保存过程中可能发生的错误
            e.printStackTrace();
            return Result.fail("File upload failed");
        }
    }

    @Override
    public Result listUserCr(User user) {
        LambdaQueryWrapper<CaRequest> CRQueryWrapper = new LambdaQueryWrapper<>();
        CRQueryWrapper.eq(CaRequest::getUserAccount, user.getAccount());
        //前端自己渲染（cr state0审核中，1审核成功（变成CA证书），2申请被撤回，需要改进）
        return Result.ok(list(CRQueryWrapper));
    }


    private boolean saveDataAndGenerateFile(CaRequest cr) {
        save(cr);
        String savePath = csrGenerator.saveCSRToFile(cr);
        return savePath != null;
    }
}




