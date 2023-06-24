package com.qinghua.website.api.controller;

import com.qinghua.website.api.controller.io.RSAEncryptIO;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.utils.CryptoCache;
import com.qinghua.website.server.utils.RSACryptoHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    /**
     * 获取RSA密钥
     * @return
     */
    @RequestMapping(value = "/getPublicKey",method = RequestMethod.GET)
    public ResponseResult<Object> getKey(){
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("publicKey",CryptoCache.getRASPublicKey());
        return ResponseResult.success(resMap);
    }

    /**
     * 加密参数
     * @return
     */
    @RequestMapping(value = "/encrypt",method = RequestMethod.POST)
    public ResponseResult<Object> encrypt(@Validated @RequestBody RSAEncryptIO encryptIO){
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("sign",RSACryptoHelper.encrypt(encryptIO.getParam(),encryptIO.getPublicKey()));
        return ResponseResult.success(resMap);
    }

}
