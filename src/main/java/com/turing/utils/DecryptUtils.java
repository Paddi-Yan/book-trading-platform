package com.turing.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.util.Arrays;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月24日 02:54:24
 */
public class DecryptUtils
{
    public static String decrypt(String sessionKey, String encryptedData, String iv) {
        try {
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(Base64.decodeBase64(iv)));
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(2, new SecretKeySpec(Base64.decodeBase64(sessionKey), "AES"), params);
            return new String(PKCS7Encoder.decode(cipher.doFinal(Base64.decodeBase64(encryptedData))), StandardCharsets.UTF_8);
        } catch (Exception var5) {
            throw new RuntimeException("AES解密失败", var5);
        }
    }
}

class PKCS7Encoder {
    private static final Charset CHARSET;
    private static final int BLOCK_SIZE = 32;

    public PKCS7Encoder() {
    }

    public static byte[] encode(int count) {
        int amountToPad = 32 - count % 32;
        char padChr = chr(amountToPad);
        StringBuilder tmp = new StringBuilder();

        for(int index = 0; index < amountToPad; ++index) {
            tmp.append(padChr);
        }

        return tmp.toString().getBytes(CHARSET);
    }

    public static byte[] decode(byte[] decrypted) {
        int pad = decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }

        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    private static char chr(int a) {
        byte target = (byte)(a & 255);
        return (char)target;
    }

    static {
        CHARSET = StandardCharsets.UTF_8;
    }
}
