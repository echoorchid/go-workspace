package com.example.demo;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncrypt {
    private static final int KEY_LENGTH = 1024;
    private static final String KEY_FACTORY_INSTANCE = "RSA";
    private static final String CIPHER_RSA_ALG = "RSA/ECB/OAEPwithSHA-256andMGF1Padding";
    private static final String PATH_PUBLIC = "D:\\public.dat";
    private static final String PATH_PRIVATE = "D:\\private.dat";

    private static volatile org.bouncycastle.jce.provider.BouncyCastleProvider  bcProvider = null;
    private static org.bouncycastle.jce.provider.BouncyCastleProvider getBcProvider(){
        if(null == bcProvider){
            synchronized (org.bouncycastle.jce.provider.BouncyCastleProvider.class){
                if(null == bcProvider){
                    bcProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                }
            }
        }
        return bcProvider;
    }

    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        //genKeyPair();
        readKeyPair();
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        String message = "Hello,World";
        String messageEn = "";
        String messageDe = "";
        System.out.println("公钥为:" + keyMap.get(0).length() + ":" + keyMap.get(0));
        System.out.println("私钥为:" + keyMap.get(1).length() + ":" + keyMap.get(1));
        messageEn = encryptByPublicKey(message);
        System.out.println("公钥加密后的字符串为:" + messageEn);
        messageDe = decryptByPrivateKey(messageEn);
        System.out.println("私钥解密后的字符串为:" + messageDe);

        messageEn = encryptByPrivateKey(message);
        System.out.println("私钥加密后的字符串为:" + messageEn);
        messageDe = decryptByPublicKey(messageEn);
        System.out.println("公钥解密后的字符串为:" + messageDe);

        System.out.println("==============================");
        String encryptByPubKeyStr = "XarXXtv66ZgwKjJQSiZ520IyA0pirilkMBPJNXML2bDyujvyCj3fiQZwjfplRqDXNZncggcwrIS28k9vP/djArpQTEOoARzBl1qLwUvowq1a5hezvxI52DYVPKWgHjcIaiYn0UOdUHrbAcuZlDzBu7ohbWYMr+xESiZahAlbVrEo2L2p8Sb95eh4v5lD8MNShxIy2GCbRidbe07DZZFwF1/4Ep0BLfKZsYg6Y20Db3Psjj15Ij2VPlOaiXaocC9KSraStsr7WFbL849QR+eieESRFrQz/lSkFH//zVK9REV1+L+FYg0ofSEBd3lBTe3N9WAkEkB1XPjoXcVVPZaA9g==";
        System.out.println("私钥解密:用go语言实现的公钥加密后的密文：" + decryptByPrivateKey(encryptByPubKeyStr));

        String encryptByPriKeyStr = "NGukntQ8MrZBvkRaJFv3+ojPPYydZJPNY2T2aj22HsDAAKxJEhDZfpfagbwEQMOaViRWdAJujGVv5r9A7OL0wXVde1wyUUzxLQSnwMEmRWxZrI3A5oTM/rxSaYarDLXYrXuVAtMGC8/v3ooR7dIQMRVgpB61llM+Bhc318fYbdDagblMoGaST65ZPOksGGYVU5SIwBYSsaLzXSDAsXj0ad/yeLgZALuLEzEQnhvJ2Jrhr6Zi/wQNrN3Jt5PCHwohUHifRlU4PMh1KM8QILvwzVYFtD1mhAbe5Ksfj6osbVM+oDfCyOaYQnM8pmZjYXaHHToDeGEs5hMAPhMniTs3Qw==";
        System.out.println("公钥解密:用go语言实现的私钥加密后的密文：" + decryptByPublicKey(encryptByPriKeyStr));

    }

    /**
     * RSA公钥加密
     * @param str
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String str) throws Exception{
        //Cipher cipher = Cipher.getInstance(CIPHER_RSA_ALG, getBcProvider());
        Cipher cipher = Cipher.getInstance(KEY_FACTORY_INSTANCE);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     * @param str
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String str) throws Exception{
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //Cipher cipher = Cipher.getInstance(CIPHER_RSA_ALG,getBcProvider());
        Cipher cipher = Cipher.getInstance(KEY_FACTORY_INSTANCE);
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * RSA私钥加密
     * @param str
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String str) throws Exception{
        //Cipher cipher = Cipher.getInstance(CIPHER_RSA_ALG,getBcProvider());
        Cipher cipher = Cipher.getInstance(KEY_FACTORY_INSTANCE);
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey());
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA 公钥解密
     * @param str
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String str) throws Exception{
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //Cipher cipher = Cipher.getInstance(CIPHER_RSA_ALG,getBcProvider());
        Cipher cipher = Cipher.getInstance(KEY_FACTORY_INSTANCE);
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey());
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_FACTORY_INSTANCE);
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(KEY_LENGTH, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
        saveKeyPair();
    }

    private static void saveKeyPair(){
        try{
            String publicKeyString = keyMap.get(0);
            String privateKeyString = keyMap.get(1);
            FileUtils.writeStringToFile(new File(PATH_PUBLIC), publicKeyString, StandardCharsets.UTF_8.name());
            FileUtils.writeStringToFile(new File(PATH_PRIVATE), privateKeyString, StandardCharsets.UTF_8.name());

        }catch (IOException e){
            System.out.print(e.getStackTrace());
        }
    }

    private static void readKeyPair(){
        try{
            String publicKeyString = FileUtils.readFileToString(new File(PATH_PUBLIC), StandardCharsets.UTF_8.name());
            String privateKeyString = FileUtils.readFileToString(new File(PATH_PRIVATE), StandardCharsets.UTF_8.name());
            // 将公钥和私钥保存到Map
            keyMap.put(0,publicKeyString);  //0表示公钥
            keyMap.put(1,privateKeyString);  //1表示私钥
        }catch (IOException e){
            System.out.print(e.getStackTrace());
        }
    }

    private static RSAPublicKey getPublicKey() throws Exception{
        try {
            String publicKey = keyMap.get(0);
            byte[] buffer = Base64.decodeBase64(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_INSTANCE);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    private static RSAPrivateKey getPrivateKey() throws Exception{
        try {
            String privateKey = keyMap.get(1);
            byte[] buffer = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_INSTANCE);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

}
