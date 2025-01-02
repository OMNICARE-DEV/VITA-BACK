package com.hops.hops_new_api.common.model.util;

import com.hops.hops_new_api.common.exception.HopsCode;
import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.service.impl.NiceCertificationServiceImpl;
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
    // key, iv 조회
    public static String encrypt(String key, String iv, String reqJsonString) throws HopsException {

        String enc_data = "";
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encrypted = cipher.doFinal(reqJsonString.trim().getBytes());
            enc_data = Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            logger.warn("암호화 중 에러", e);
            throw new HopsException(HopsCode.AES_ERROR); // 암호화 실패
        }

        return enc_data;
    }

    public static byte[] hmac256(byte[] secretKey, byte[] message) throws HopsException {
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

    public static String decrypt(String alg, String key, String iv, String cipherText) throws HopsException {

        String decryptedStr = "";
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            decryptedStr = new String(decrypted, "UTF-8");
        } catch (Exception e) {
            logger.warn("복호화 중 에러", e);
            throw new HopsException(HopsCode.SYSTEM_ERROR); // 내부 에러
        }

        return decryptedStr;
    }
}