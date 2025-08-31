package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.entity.CreditScoreRequest;
import com.JinRui.fengkong4.entity.PersonalInfo;
import com.JinRui.fengkong4.generator.CreditDataGenerator;
import com.JinRui.fengkong4.service.UserInfoGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息生成服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class UserInfoGeneratorServiceImpl implements UserInfoGeneratorService {

    @Autowired
    private CreditDataGenerator creditDataGenerator;

    @Override
    public List<CreditScoreRequest> generateUserInfoBatch(int count, String batchId) {
        log.info("开始批量生成用户信息，数量：{}，批次ID：{}", count, batchId);
        
        List<CreditScoreRequest> requests = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            CreditScoreRequest request = generateSingleUserInfo(batchId);
            requests.add(request);
        }
        
        log.info("批量生成用户信息完成，数量：{}，批次ID：{}", requests.size(), batchId);
        return requests;
    }

    @Override
    public CreditScoreRequest generateSingleUserInfo(String batchId) {
        // 使用现有的征信数据生成器生成个人信息
        PersonalInfo personalInfo = creditDataGenerator.generatePersonalInfo();
        
        String idCard = personalInfo.getIdNumber();
        String name = personalInfo.getName();
        
        log.debug("生成用户信息：身份证：{}，姓名：{}", 
                 idCard.substring(0, 6) + "****" + idCard.substring(14), name);
        
        return new CreditScoreRequest(idCard, name, batchId);
    }
}
