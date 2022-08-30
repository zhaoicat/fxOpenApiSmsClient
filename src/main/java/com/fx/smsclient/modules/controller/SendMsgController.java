package com.fx.smsclient.modules.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fx.smsclient.modules.service.SendMsgService;
import com.fx.smsclient.util.FxPushCheckSum;
import com.fx.smsclient.util.response.ErrorResponseData;
import com.fx.smsclient.util.response.SuccessResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@Slf4j
public class SendMsgController  {

    @Value("${openApi.appSecret}")
    String appSecret;

    @Autowired
    SendMsgService sendMsgService;

    @PostMapping(value = "/sendMsg",produces= MediaType.APPLICATION_JSON_VALUE)
    Object sendMsg(@RequestBody HashMap paraMap) throws Exception {
        String phone = (String)paraMap.get("phone");
        String context =(String) paraMap.get("context");
        String byPass =(String) paraMap.get("byPass");
        String ret =sendMsgService.sendMsg(phone,context,byPass);
        log.info(ret);
        JSONObject jsonObject = JSONUtil.parseObj(ret);
        return jsonObject;
    }

    @PostMapping(value = "/callback",produces= MediaType.APPLICATION_JSON_VALUE)
    Object callback(@RequestParam(value="appKey") String appKey,
                    @RequestParam(value="checksum") String checksum,
                    @RequestParam(value="time") Long time,
                    @RequestBody String json) throws Exception {

        String myCheckSum =  FxPushCheckSum.encode(appSecret, json,String.valueOf(time));
        if (!myCheckSum.equals(checksum)){
            return ErrorResponseData.error("");
        }
        log.info(json);
        return SuccessResponseData.success("ok");
    }

    @GetMapping("/hi")
    String hi(){
        log.info("{}","hi");
        return "hi";
    }
}
