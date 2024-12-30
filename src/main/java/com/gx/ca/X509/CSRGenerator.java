package com.gx.ca.X509;

import com.gx.ca.mapper.CaRequest;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class CSRGenerator {
    public static void main(String[] args) throws Exception {
        // 添加 BouncyCastle 提供程
        Security.addProvider(new BouncyCastleProvider());

        // 生成密钥对（可以选择 RSA、ECC 等算法）
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 构建 X500Name（主题信息）
        String cn = "example.com";  // CN是一个变量
        String subjectString = "CN=" + cn + ", O=North University of China, L=TaiYuan, ST=ShanXi, C=China";
        X500Name subject = new X500Name(subjectString);

        // 创建 CSR
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());
        PKCS10CertificationRequest csr = new JcaPKCS10CertificationRequestBuilder(subject, keyPair.getPublic())
                .build(contentSigner);

        // 替换 CN 中的 . 为 _ 或其他字符，以便生成合法的文件名
        String filename = cn.replace(".", "_") + ".csr";

        // 将 CSR 保存到文件
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(csr.getEncoded());
        }
        // 将 CSR 保存为 PEM 格式
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(filename))) {
            pemWriter.writeObject(csr);
        }

        System.out.println("CSR Generated and saved to " + filename);
    }
    @Resource
    MyRSA myRSA;
    public String saveCSRToFile(CaRequest cr) {
        // 添加 BouncyCastle 提供程
        Security.addProvider(new BouncyCastleProvider());

        String filename = null;
        try {
            // 直接使用 CaRequest 中传入的公钥
            System.out.println(cr);
            String publicKeyString = cr.getPublicKey();
            String privateKeyString = cr.getPrivateKey();
            // 将公钥字符串解码为 PublicKey 对象
            PublicKey publicKey = myRSA.stringToPublic(publicKeyString);

            PrivateKey privateKey = myRSA.stringToPrivate(privateKeyString); // 生成 PrivateKey 对象


            // 构建 X500Name（主题信息）
            String subjectString = "CN=" + cr.getCommonName() + ", O=" + cr.getOrganization() + ", L=" + cr.getLocality() + ", ST=" + cr.getProvince() + ", C=" + cr.getCountry();
            X500Name subject = new X500Name(subjectString);

            // 创建 CSR
            ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey); // 假设已经有私钥
            PKCS10CertificationRequest csr = new JcaPKCS10CertificationRequestBuilder(subject, publicKey)
                    .build(contentSigner);

            // 替换 CN 中的 . 为 _ 或其他字符，以便生成合法的文件名
            filename = cr.getCommonName().replace(".", "_") + ".csr";

            // 将 CSR 保存到文件
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                fos.write(csr.getEncoded());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 将 CSR 保存为 PEM 格式
            try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(filename))) {
                pemWriter.writeObject(csr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("CSR Generated and saved to " + filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return filename;
    }

}
