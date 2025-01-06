package com.gx.ca.X509;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyRSA {

    // CA 路径配置
//    @Value("${ca.private.key.file:CA_private_key.pem}")
    private String caPrivateKeyFile = "CA_private_key.pem";

//    @Value("${ca.public.key.file:CA_public_key.pem}")
    private String caPublicKeyFile = "CA_public_key.pem";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // 生成 RSA 公私钥对
    public Map<String, String> generateKeys() {
        Map<String, String> keyPairMap = new HashMap<>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 设置 RSA 密钥长度
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 获取公钥和私钥
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 将公钥和私钥转换为 Base64 编码的字符串
            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            keyPairMap.put("publicKey", publicKeyString);
            keyPairMap.put("privateKey", privateKeyString);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA key pair", e);
        }
        return keyPairMap;
    }


    // 公钥字符串转换为 PublicKey 对象
    public PublicKey stringToPublic(String publicKeyString) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 私钥字符串转换为 PrivateKey 对象
    public PrivateKey stringToPrivate(String privateKeyString) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }




    //CA generator

    public static String  CA_PUBLIC_KEY = "CA_publicKey.pem";
    public static String CA_PRIVATE_KEY = "CA_privateKey.pem";
    // 将密钥保存到磁盘（PEM 格式）
    public static void saveKeyToDisk(String fileName, Key key) throws IOException {
        String keyBase64 = Base64.getEncoder().encodeToString(key.getEncoded());
        StringBuilder keyContent = new StringBuilder();

        if (key instanceof PrivateKey) {
            keyContent.append("-----BEGIN PRIVATE KEY-----\n");
        } else if (key instanceof PublicKey) {
            keyContent.append("-----BEGIN PUBLIC KEY-----\n");
        }

        keyContent.append(keyBase64);

        if (key instanceof PrivateKey) {
            keyContent.append("\n-----END PRIVATE KEY-----");
        } else if (key instanceof PublicKey) {
            keyContent.append("\n-----END PUBLIC KEY-----");
        }

        // 写入到文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(keyContent.toString());
        }
    }

    // 从文件读取私钥并返回 PrivateKey 对象
    public static PrivateKey loadPrivateKeyFromDisk(String fileName) throws Exception {
        // 从文件中读取私钥
        String privateKeyContent = readFile(fileName);

        // 去掉 PEM 格式中的标识符
        privateKeyContent = privateKeyContent.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "").trim();

        // Base64 解码
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyContent);

        // 使用 KeyFactory 解析私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    // 从文件读取公钥并返回 PublicKey 对象
    public static PublicKey loadPublicKeyFromDisk(String fileName) throws Exception {
        // 从文件中读取公钥
        String publicKeyContent = readFile(fileName);

        // 去掉 PEM 格式中的标识符
        publicKeyContent = publicKeyContent.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "").trim();

        // Base64 解码
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyContent);

        // 使用 KeyFactory 解析公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 辅助方法：从文件中读取内容
    private static String readFile(String fileName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        try {
            // 1. 生成密钥对（公钥和私钥）
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 设置密钥长度
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // 2. 将公钥和私钥保存到文件
            saveKeyToDisk(CA_PRIVATE_KEY, privateKey);
            saveKeyToDisk(CA_PUBLIC_KEY, publicKey);

            System.out.println("公钥和私钥已经成功生成并保存到磁盘。");

            // 3. 从磁盘读取并解析公钥和私钥
            PrivateKey loadedPrivateKey = loadPrivateKeyFromDisk(CA_PRIVATE_KEY);
            PublicKey loadedPublicKey = loadPublicKeyFromDisk(CA_PUBLIC_KEY);

            // 输出读取的公钥和私钥信息
            System.out.println("成功从磁盘加载私钥和公钥。");
            System.out.println("私钥: " + loadedPrivateKey);
            System.out.println("公钥: " + loadedPublicKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
