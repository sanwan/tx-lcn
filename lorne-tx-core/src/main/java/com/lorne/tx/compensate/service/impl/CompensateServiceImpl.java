package com.lorne.tx.compensate.service.impl;

import com.lorne.tx.compensate.model.TransactionInvocation;
import com.lorne.tx.compensate.model.TransactionRecover;
import com.lorne.tx.compensate.service.CompensateOperationService;
import com.lorne.tx.compensate.service.CompensateService;
import com.lorne.tx.service.ModelNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by yuliang on 2017/7/11.
 */
@Service
public class CompensateServiceImpl implements CompensateService {


    //补偿事务标示 识别groupId （远程调用时传递的参数）
    public final static String COMPENSATE_KEY = "COMPENSATE";

    @Autowired
    private CompensateOperationService compensateOperationService;

    @Autowired
    private ModelNameService modelNameService;

    @Override
    public void start() {

        // TODO: 2017/7/11  数据库创建等操作
        compensateOperationService.init(modelNameService.getModelName());

        // TODO: 2017/7/11  查找补偿数据
        List<TransactionRecover> list =  compensateOperationService.findAll();

        if(list==null||list.size()==0){
            return;
        }

        // TODO: 2017/7/11  执行补偿业务 （只要业务执行未出现异常就算成功）
        for(TransactionRecover data:list){
            compensateOperationService.execute(data);
        }
    }


    @Override
    public String saveTransactionInfo(TransactionInvocation invocation, String groupId, String taskId) {
        // TODO: 2017/7/11  记录补偿数据
        return compensateOperationService.save(invocation,groupId,taskId);
    }

    @Override
    public boolean deleteTransactionInfo(String id) {
        //TODO: 2017/7/11  删除补偿数据
        return compensateOperationService.delete(id);
    }
}
