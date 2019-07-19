package com.pinyougou.sms.controller;


import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.pinyougou.sms.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms")
public class sendMenssageController {

    @Autowired
    private SmsUtil smsUtil;//使用工具类来 发送短信


    /*String phoneNumbers,String signName,String templateCode,String param*/
    @RequestMapping("/sendSms")
    public Map  sendSms(String phoneNumbers,String signName,String templateCode,String param){

        try {
            SendSmsResponse response  = smsUtil.sendSms(phoneNumbers, signName, templateCode, param);
            //将发送消息分装到Map中  返回给调用方
            System.out.println("短信接口返回的数据----------------");
            System.out.println("Code=" + response.getCode());
            System.out.println("Message=" + response.getMessage());
            System.out.println("RequestId=" + response.getRequestId());
            System.out.println("BizId=" + response.getBizId());
            Map<String,String> map = new HashMap();
            map.put("Code",response.getCode());
            map.put("Message",response.getMessage());
            map.put("RequestId",response.getRequestId());
            map.put("BizId",response.getBizId());
            return map;

        } catch (ClientException e) {
            e.printStackTrace();

            return new HashMap();
        }



    }
}
