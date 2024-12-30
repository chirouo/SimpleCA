package X509;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

public class GeneratePrivateKey {
    public static void main(String[] args) throws Exception {
        // 创建一个密钥对生成器，用 RSA 算法生成密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  // 密钥长度为 2048 位
        KeyPair keyPair = keyPairGenerator.generateKeyPair();  // 生成密钥对

        // 获取私钥
        PrivateKey privateKey = keyPair.getPrivate();

        // 保存私钥到文件
        try (FileWriter fileWriter = new FileWriter("ca_private_key.pem");
             JcaPEMWriter pemWriter = new JcaPEMWriter(fileWriter)) {
            pemWriter.writeObject(privateKey);  // 写入 PEM 格式的私钥
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Private key generated and saved to private_key.pem");
    }
}

