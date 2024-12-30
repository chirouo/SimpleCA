package com.gx.ca.X509;

import com.gx.ca.mapper.CaRequest;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

@Component
public class CaCertificateGenerator {

    @Value("${files.upload.ca-path}")
    String outputPath; // 替换为实际输出路径
    public Certificate generateCACertificate(String commonName, String organization, String country, String outputPath, long expireTime) {
        Security.addProvider(new BouncyCastleProvider());
        try {
            // 1. 生成密钥对（公钥和私钥）
            PrivateKey privateKey = MyRSA.loadPrivateKeyFromDisk(MyRSA.CA_PRIVATE_KEY);
            PublicKey publicKey = MyRSA.loadPublicKeyFromDisk(MyRSA.CA_PUBLIC_KEY);
            // 2. 定义 CA 的主体信息
            X500Name subject = new X500Name("CN=" + commonName + ", O=" + organization + ", C=" + country);

            // 3. 定义证书有效期
            Date startDate = new Date(); // 生效时间
            Date endDate = new Date(startDate.getTime() + expireTime); // 有效期 1 年

            // 4. 创建证书构造器
            BigInteger serialNumber = new BigInteger(64, new SecureRandom()); // 随机生成序列号
            JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                    subject, // 颁发者和接收者（自签名）
                    serialNumber,
                    startDate,
                    endDate,
                    subject,
                    publicKey
            );

            // 5. 使用私钥签名证书
            ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
            X509CertificateHolder certHolder = certBuilder.build(contentSigner);

            // 6. 转换为标准 X509Certificate 对象
            X509Certificate certificate = new JcaX509CertificateConverter()
                    .setProvider("BC")
                    .getCertificate(certHolder);

            // 7. 保存证书到文件（PEM 格式）
            try (FileWriter writer = new FileWriter(outputPath + commonName.replace(".", "_") + "_ca_certifacate.pem");
                 JcaPEMWriter pemWriter = new JcaPEMWriter(writer)) {
                pemWriter.writeObject(certificate);
            }


            System.out.println("CA Certificate saved to disk at: " + outputPath);
            return certificate;

        } catch (Exception e) {
            throw new RuntimeException("Error generating CA certificate", e);
        }
    }

    public static void main(String[] args) {
        CaCertificateGenerator generator = new CaCertificateGenerator();
        String outputPath = "./"; // 替换为实际输出路径
        Certificate caCertificate = generator.generateCACertificate("MyCA", "MyOrganization", "US", outputPath, 1000000);

        System.out.println("CA Certificate Generated and Saved:");
        System.out.println(caCertificate);
    }
    public void saveAsCAFile(com.gx.ca.mapper.Certificate ca)
    {
        CaCertificateGenerator generator = new CaCertificateGenerator();
        Certificate caCertificate = generator.generateCACertificate(ca.getCommonName(), ca.getCountry(), ca.getCountry(), outputPath, ca.getExpireTime() * 1000);
        System.out.println("CA Certificate Generated and Saved:");
        System.out.println(caCertificate);
    }
}

