package com.vita.back.common.util;

import com.vita.back.common.exception.VitaCode;
import com.vita.back.common.exception.VitaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES256Util {
    private static final String CHARSET = "UTF-8";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private final String key;
    private final String iv;

    public AES256Util(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }

    public String encrypt(String data) throws VitaException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(data.trim().getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new VitaException(VitaCode.AES_ERROR);
        }
    }

    public byte[] hmac256(byte[] secretKey, byte[] message) throws VitaException {
        byte[] hmac256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(sks);
            hmac256 = mac.doFinal(message);
            return hmac256;
        } catch (Exception e) {
            throw new VitaException(VitaCode.SYSTEM_ERROR);
        }
    }

    public String decrypt(String encryptedData) throws VitaException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(CHARSET));

            /* Use URL-safe Base64 decoder */
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);

            cipher.init(Cipher.DECRYPT_MODE, new javax.crypto.spec.SecretKeySpec(key.getBytes(CHARSET), "AES"),
                    ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(decodedData);

            return new String(decryptedBytes, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            throw new VitaException(VitaCode.SYSTEM_ERROR);
        }
    }
}