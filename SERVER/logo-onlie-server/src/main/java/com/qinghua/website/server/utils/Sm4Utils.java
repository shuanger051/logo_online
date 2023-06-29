package com.qinghua.website.server.utils;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

public class Sm4Utils {

    private static final String ENCODING = "UTF-8";
    public static final String ALGORIGTHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";
    public static final int DEFAULT_KEY_SIZE = 128;

    public static final String HEX_KEY = "967052bbdb38d5959c21498bcec3f6ff";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 自动生成密钥
     *
     * @return
     * @throws Exception
     */
    public static String generateKey() throws Exception {
        byte[] bytes = generateKey(DEFAULT_KEY_SIZE);
        return Hex.toHexString(bytes);
    }

    /**
     * 加密
     *
     * @param paramStr 需要加密字符串
     */
    public static String encrypt(String paramStr) {
        if (StringUtils.isBlank(paramStr)) {
            return paramStr;
        }
        return encrypt(HEX_KEY,paramStr);
    }

    /**
     * 加密
     *
     * @param hexKey   32位16进制密钥
     * @param paramStr 需要加密字符串
     * @return
     * @throws Exception
     */
    public static String encrypt(String hexKey, String paramStr)  {
        try {
            String cipherText = "";
            if (null != paramStr && !"".equals(paramStr)) {
                byte[] keyData = ByteUtils.fromHexString(hexKey);
                byte[] srcData = paramStr.getBytes(ENCODING);
                byte[] cipherArray = encryptEcbPadding(keyData, srcData);
                cipherText = ByteUtils.toHexString(cipherArray);
            }
            return cipherText;
        }catch (Exception e) {
            throw new BizException(SysConstant.ERROR_ENCODE_RSA_WRONG_10015);
        }
    }

    /**
     * 解密
     *
     * @param cipherText 需要解密字符串
     */
    public static String decrypt(String cipherText)  {
        if (StringUtils.isBlank(cipherText)) {
            return cipherText;
        }
        return decrypt(HEX_KEY,cipherText);
    }

    /**
     * 解密
     *
     * @param hexKey     32位16进制密钥
     * @param cipherText 需要解密字符串
     * @return
     * @throws Exception
     */
    public static String decrypt(String hexKey, String cipherText)  {
        try {
            String decryptStr = "";
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            byte[] srcData = decryptEcbPadding(keyData, cipherData);
            decryptStr = new String(srcData, ENCODING);
            return decryptStr;
        }catch (Exception e) {
            throw new BizException(SysConstant.ERROR_DECODE_RSA_WRONG_10016);
        }
    }

    /**
     * 密码校验
     *
     * @param hexKey     32位16进制密钥
     * @param cipherText 加密字符串
     * @param paramStr   未加密字符串
     * @return
     * @throws Exception
     */
    public static boolean verify(String hexKey, String cipherText, String paramStr) throws Exception {
        boolean flag = false;
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        byte[] decryptData = decryptEcbPadding(keyData, cipherData);
        byte[] srcData = paramStr.getBytes(ENCODING);
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }

    /**
     * 生成ecb秘钥
     *
     * @param algorithmName
     * @param mode
     * @param key
     * @return
     * @throws Exception
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORIGTHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     * 生成keySize长度的密钥数组
     *
     * @param keySize
     * @return
     * @throws Exception
     */
    private static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORIGTHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * 加密模式之ecb
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    private static byte[] encryptEcbPadding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        byte[] bs = cipher.doFinal(data);
        return bs;
    }

    /**
     * ecb解密
     *
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     */
    private static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    public static void main(String[] args) {
        try {
            String encrypt = encrypt("13323281110");
            System.out.println(encrypt);
            String decrypt = decrypt(encrypt);
            System.out.println(decrypt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
