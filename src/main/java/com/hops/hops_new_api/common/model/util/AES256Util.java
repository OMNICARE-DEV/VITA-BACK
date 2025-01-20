package com.hops.hops_new_api.common.model.util;

import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES256Util {

    private static final Logger logger = LoggerFactory.getLogger(AES256Util.class);

    private static final String CHARSET = "UTF-8";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private final String key;
    private final String iv;

    public AES256Util(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }

    public String encrypt(String data) throws HopsException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(data.trim().getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            logger.warn("암호화 중 에러", e);
            throw new HopsException(HopsCode.AES_ERROR); // 암호화 실패
        }

    }

//    public static String encrypt(String key, String iv, String reqJsonString) throws HopsException {
//
//        String enc_data = "";
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
//            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
//            byte[] encrypted = cipher.doFinal(reqJsonString.trim().getBytes());
//            enc_data = Base64.getEncoder().encodeToString(encrypted);
//        } catch (Exception e) {
//            logger.warn("암호화 중 에러", e);
//            throw new HopsException(HopsCode.AES_ERROR); // 암호화 실패
//        }
//
//        return enc_data;
//    }

    public byte[] hmac256(byte[] secretKey, byte[] message) throws HopsException {
        byte[] hmac256 = null;
        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(sks);
            hmac256 = mac.doFinal(message);
            return hmac256;
        } catch(Exception e){
            logger.warn("hmac 생성 중 에러", e);
            throw new HopsException(HopsCode.SYSTEM_ERROR); // 내부에러
        }
    }

    public String decrypt(String encryptedData) throws HopsException {

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(CHARSET));

            // Use URL-safe Base64 decoder
            byte[] decodedData = Base64.getUrlDecoder().decode(encryptedData);

            cipher.init(Cipher.DECRYPT_MODE, new javax.crypto.spec.SecretKeySpec(key.getBytes(CHARSET), "AES"), ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(decodedData);

            return new String(decryptedBytes, CHARSET);
        } catch (Exception e) {
            logger.warn("복호화 중 에러", e);
            throw new HopsException(HopsCode.SYSTEM_ERROR); // 내부 에러
        }
    }
}