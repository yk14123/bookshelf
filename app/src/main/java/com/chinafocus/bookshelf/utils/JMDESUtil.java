package com.chinafocus.bookshelf.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加密工具类
 */
public class JMDESUtil {
    // 密钥 长度不得小于24
//       private final static String secretKey = "aDFrAffxAGCRtdrDxvFvbhEvg" ;
    // 向量 可有可无 终端后台也要约定
//       private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    private final static String secretKey = "inIG;'N8:vJ<lh.]^4mXZ1D5";
    private final static String iv = "45079632";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return JMBase64.encode(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(JMBase64.decode(encryptText));

        return new String(decryptData, encoding);
    }

    public static void main(String args[]) throws Exception {
        String str = "123456789";
        System.out.println("----加密前-----：" + str);
        String encodeStr = JMDESUtil.encode(str);
        System.out.println("----加密后-----：" + encodeStr);
        System.out.println("----解密后-----：" + JMDESUtil.decode(encodeStr));
    }
}
