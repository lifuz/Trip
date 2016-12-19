package com.lifuz.trip.utils.security;


import android.util.Base64;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * RSA 加解密
 * <p>
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/12/13 11:58
 */
public class RSAEncryptor {

    private static RSAEncryptor ourInstance;

    private static final String PRIVATE_KEY_PATH = "F:/RSA/pkcs8_rsa_private_key.pem";
    private static final String PUBLIC_KEY_PATH = "F:/RSA/rsa_public_key.pem";

    private PrivateKey privateKey;

    private PublicKey publicKey;

    private RSAEncryptor() {

    }

    public static RSAEncryptor getInstance() {
        if (ourInstance == null) {
            ourInstance = new RSAEncryptor();
        }

        return ourInstance;
    }

    /**
     * 解密
     * @param cipherText 密文
     * @param mode 解密模式（1：为公钥解密，2：私钥解密）
     * @return 明文
     * @throws Exception 抛出异常
     */
    public String decryptWithBase64(String cipherText,int mode) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");

        if (mode == 1) {
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
        } else {

            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        }

        byte[] result = cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT));

        String text = new String(result);

        System.out.println("解密 :　" + text);

        return text;

    }

    /**
     * 加密
     * @param text 明文
     * @param mode 加密模式（1：为公钥加密；2：为私钥加密）
     * @return 密文
     * @throws Exception 抛出异常
     */
    public String encryptWithBase64(String text, int mode) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        if (mode == 1) {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } else {

            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        }


        byte[] result = cipher.doFinal(text.getBytes());
        String cipherText =  new String(Base64.encode(result, Base64.DEFAULT));
        System.out.println("加密 :" + cipherText);

        return cipherText;

    }

    /**
     * 根据默认路径加载密钥
     *
     * @throws Exception 抛出异常
     */
    public void loadKey() throws Exception {
        String privatePath = getKeyFromFile(PRIVATE_KEY_PATH);
        String publicPath = getKeyFromFile(PUBLIC_KEY_PATH);

        loadPrivateKey(privatePath);

        loadPublicKey(publicPath);
    }

    /**
     * 加载公钥
     *
     * @param publicKeyStr 公钥字符串
     * @throws Exception 抛出异常
     */
    public void loadPublicKey(String publicKeyStr) throws Exception {

        byte[] buffer = Base64.decode(publicKeyStr, Base64.DEFAULT);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
    }

    /**
     * 加载私钥
     *
     * @param privateKeyStr 私钥字符串
     * @throws Exception 抛出异常
     */
    public void loadPrivateKey(String privateKeyStr) throws Exception {

        byte[] buffer =Base64.decode(privateKeyStr, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        privateKey = keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从数据流获取密钥
     *
     * @param in 数据流
     * @return 密钥
     * @throws IOException
     */
    public String getKeyFromIO(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }
        br.close();
        br = null;

        return sb.toString();
    }

    /**
     * 从文件读取密钥
     *
     * @param filePath 文件路径
     * @return 密钥字符串
     * @throws IOException
     */
    public String getKeyFromFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line = null;

        List<String> list = new ArrayList<>();

        while (null != (line = br.readLine())) {
            list.add(line);
        }

        if (br != null) {
            //关闭文件流
            br.close();
            //使对象的没有引用，以便回收
            br = null;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < list.size() - 1; i++) {
            sb.append(list.get(i)).append("\r");
        }

        return sb.toString();

    }


}
