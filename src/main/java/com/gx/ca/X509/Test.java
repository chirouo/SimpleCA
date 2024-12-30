package com.gx.ca.X509;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Test {
    public static void main(String[] args) {
        try {
            // 生成 RSA 密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 设置密钥长度
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 获取公钥
            PublicKey publicKey = keyPair.getPublic();

            // 将公钥转换为字节数组
            byte[] publicKeyBytes = publicKey.getEncoded();

            // 将公钥字节数组保存为 PEM 文件
            String publicKeyFileName = "publicKey.pem";
            savePublicKeyToPEMFile(publicKeyBytes, publicKeyFileName);

            // 输出提示信息
            System.out.println("公钥已成功保存到文件: " + publicKeyFileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 提取公钥的方法
    public static void savePublicKeyToPEMFile(byte[] publicKeyBytes, String fileName) throws IOException {
        // 使用 PEM 格式保存公钥
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n"
                + Base64.getEncoder().encodeToString(publicKeyBytes)
                + "\n-----END PUBLIC KEY-----";

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(publicKeyPEM);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
