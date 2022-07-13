package com.fx.smsclient.modules.service;

import cn.hutool.json.JSONUtil;
import com.fx.smsclient.util.FxPushCheckSum;
import com.fx.smsclient.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class SendMsgService {
    @Value("${openApi.appKey}")
    String appKey ;
    @Value("${openApi.appSecret}")
    String AppSecret;
    @Value("${openApi.entityId}")
    String entityId;
    @Value("${openApi.sendSmsUrl}")
    String url;

    @Autowired
    HttpClientUtil httpClientUtil;

    public String sendMsg(String phone, String msg){
        HashMap<String,String> map = new HashMap(){{
            put("entityId",entityId);
            put("phone",phone);
            put("msg",msg);
        }};
        String json =JSONUtil.toJsonStr(map);
        long time = System.currentTimeMillis()/1000;
        String checkSum = FxPushCheckSum.encode(AppSecret,json,String.valueOf(time));
        String path =url+"/?appKey="+appKey+"&checksum="+checkSum+"&time="+time;
        log.debug(path);
        log.debug(json);
        String ret = httpClientUtil.post(path,json);
        return ret;
    }
}

