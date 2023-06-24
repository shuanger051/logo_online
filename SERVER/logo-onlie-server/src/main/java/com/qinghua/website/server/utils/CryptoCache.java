package com.qinghua.website.server.utils;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;

import java.util.HashMap;
import java.util.Map;

public final class CryptoCache {

    private static Map<String, Object> RSAKeys = new HashMap<>();

    public static  Map<String, Object> getRSAKeys() {
        return RSAKeys;
    }

    private static void setRSAKeys(Map<String, Object> RSAKeys) {
        CryptoCache.RSAKeys.clear();
        CryptoCache.RSAKeys.putAll(RSAKeys);
    }

    public static synchronized String getRASPublicKey(){
        Map<String, Object> RSAKeys = CryptoCache.getRSAKeys();
        if(RSAKeys.isEmpty()){
           initRSAKeys();
        }
        try {
            return RSACoder.getPublicKey(RSAKeys);
        } catch (Exception e) {
            throw new BizException(SysConstant.RSA_ERROR_10003.getMsg(),SysConstant.RSA_ERROR_10003.getCode());
        }
    }

    public static String getRSAPrivateKey(){
        Map<String, Object> RSAKeys = CryptoCache.getRSAKeys();
        if(RSAKeys.isEmpty()){
            initRSAKeys();
        }
        try {
            return RSACoder.getPrivateKey(RSAKeys);
        } catch (Exception e) {
            throw new BizException(SysConstant.RSA_ERROR_10003.getMsg(),SysConstant.RSA_ERROR_10003.getCode());
        }
    }


    private static void initRSAKeys(){
        try {
            synchronized (CryptoCache.getRSAKeys()) {
                if(CryptoCache.getRSAKeys().isEmpty()) {
                    CryptoCache.setRSAKeys(RSACoder.initKey());
                }
            }
        } catch (Exception e) {
            throw new BizException(SysConstant.RSA_ERROR_10003.getMsg(),SysConstant.RSA_ERROR_10003.getCode());
        }
    }
}
