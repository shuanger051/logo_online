package com.qinghua.website.server.utils;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class RSACryptoHelper {

    /**
     * RSA解密函數
     * @param content
     * @return
     */
    public static final String decrypt(String content){
        byte[] decodedData = new byte[0];
        try {
            decodedData = RSACoder.decryptByPrivateKey(RSACoder.decryptBASE64(content),CryptoCache.getRSAPrivateKey());
        } catch (Exception e) {
            throw new BizException(SysConstant.RSA_ERROR_10001.getMsg(), SysConstant.RSA_ERROR_10001.getCode());
        }
        try {
            return URLDecoder.decode(new String(decodedData),"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new BizException(SysConstant.RSA_ERROR_10001.getMsg(), SysConstant.RSA_ERROR_10001.getCode());
        }
    }

    /**
     * RSA解密函數
     * @param content
     * @return
     */
    public static final String encrypt(String content){
        byte[] decodedData = new byte[0];
        try {
            decodedData = RSACoder.encryptByPublicKey(content.getBytes("utf-8"),CryptoCache.getRASPublicKey());
        } catch (Exception e) {
            throw new BizException(SysConstant.RSA_ERROR_10001.getMsg(), SysConstant.RSA_ERROR_10001.getCode());
        }
        try {
            return RSACoder.encryptBASE64(decodedData);
        } catch (UnsupportedEncodingException e) {
            throw new BizException(SysConstant.RSA_ERROR_10001.getMsg(), SysConstant.RSA_ERROR_10001.getCode());
        } catch (Exception e) {
            throw new BizException(SysConstant.RSA_ERROR_10001.getMsg(), SysConstant.RSA_ERROR_10001.getCode());
        }
    }
}
