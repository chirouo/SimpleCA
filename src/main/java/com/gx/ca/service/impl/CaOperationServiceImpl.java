package com.gx.ca.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gx.ca.mapper.CaOperation;
import com.gx.ca.mapper.Certificate;
import com.gx.ca.service.CaOperationService;
import com.gx.ca.mapper.CaOperationMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 26274
* @description 针对表【ca_operation】的数据库操作Service实现
* @createDate 2024-12-25 16:26:30
*/
@Service
public class CaOperationServiceImpl extends ServiceImpl<CaOperationMapper, CaOperation>
    implements CaOperationService{
    // 静态的 Integer 常量
    private static final int AUDIT = 0;
    private static final int UPDATE_EXPIRE = 1;
    private static final int DELETE = 2;
    private static final int REOVERY = 3;
    @Override
    public boolean saveOperationOnCA(Certificate ca, Integer operation){
        CaOperation caOperation = new CaOperation();
        caOperation.setCaCn(ca.getCommonName());
        caOperation.setCaId(ca.getId());
        caOperation.setTime(new Date());
        caOperation.setUserAccount(ca.getUserAccount());
        caOperation.setOperation(operation);
        String content = null;
        switch (operation) {
            case AUDIT:
                 content = "网站"+caOperation.getCaCn()+"在"+caOperation.getTime()+"时"+"通过审核";
                 break;
            case UPDATE_EXPIRE:
                content = "网站"+caOperation.getCaCn()+"在"+caOperation.getTime()+"时"+"延长有效时间至"+ca.getDeletedAt().toString();
                break;
            case DELETE:
                content = "网站"+caOperation.getCaCn()+"在"+caOperation.getTime()+"时"+"被吊销";
                break;
            case REOVERY:
                content = "网站"+caOperation.getCaCn()+"在"+caOperation.getTime()+"时"+"被恢复，有效期限至" + ca.getDeletedAt().toString();
                break;
            default:
                content = "无效操作";
                break;

        }

        caOperation.setContent(content);
        boolean success = save(caOperation);
        return success;
    }
}




