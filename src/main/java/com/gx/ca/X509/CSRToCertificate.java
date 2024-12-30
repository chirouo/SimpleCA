package com.gx.ca.X509;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class CSRToCertificate {
    public static void main(String[] args) throws Exception {
        // 添加 BouncyCastle 提供程序
        Security.addProvider(new BouncyCastleProvider());

        // 从 CSR 文件读取内容
        try (PEMParser pemParser = new PEMParser(new FileReader("example_com.csr"))) {
            Object object = pemParser.readObject();
            if (object instanceof PKCS10CertificationRequest) {
                PKCS10CertificationRequest csr = (PKCS10CertificationRequest) object;

                // 获取公钥和主题信息
                PublicKey publicKey = getPublicKey(csr.getSubjectPublicKeyInfo());
                X500Name subject = csr.getSubject();

                // 获取 CA 证书签发者的信息（这里可以是一个固定值，或者通过配置指定）
                X500Name issuer = new X500Name("CN=My CA, O=MyOrganization, C=US");

                // 获取 CA 的私钥
                PrivateKey caPrivateKey = getPrivateKey("ca_private_key.pem");

                // 生成证书的有效期（从当前日期开始，持续 365 天）
                Date startDate = new Date();
                Date endDate = new Date(startDate.getTime() + 365L * 24 * 60 * 60 * 1000);

                // 生成证书序列号（使用随机数生成一个大整数）
                BigInteger serialNumber = new BigInteger(64, new SecureRandom());

                // 将 PublicKey 转换为 SubjectPublicKeyInfo
                SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());

                // 创建签名者（CA）内容签名器
                JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256withRSA");

                // 使用 X509v3CertificateBuilder 创建证书
                X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                        issuer, serialNumber, startDate, endDate, subject, publicKeyInfo);

                // 使用 CA 的私钥进行签名
                X509CertificateHolder certificateHolder = certBuilder.build(contentSignerBuilder.build(caPrivateKey));

                // 转换为 X509Certificate
                X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certificateHolder);

                // 保存生成的证书为文件
                try (FileOutputStream fos = new FileOutputStream("certificate.crt")) {
                    fos.write(certificate.getEncoded());
                }
                System.out.println("CA Certificate generated and saved to certificate.crt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据公钥信息生成 PublicKey
    private static PublicKey getPublicKey(SubjectPublicKeyInfo publicKeyInfo) throws Exception {
        byte[] encoded = publicKeyInfo.getEncoded();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return keyFactory.generatePublic(keySpec);
    }

    // 获取 CA 的私钥
    private static PrivateKey getPrivateKey(String privateKeyFile) throws Exception {
        // 使用 PEM 解析器从文件中加载 CA 私钥
        try (PEMParser pemParser = new PEMParser(new FileReader(privateKeyFile))) {
            PEMKeyPair pemKeyPair =  (PEMKeyPair)pemParser.readObject();
            PrivateKeyInfo privateKeyInfo =  pemKeyPair.getPrivateKeyInfo();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encoded = privateKeyInfo.getEncoded();
            PKCS8EncodedKeySpec  keySpec = new PKCS8EncodedKeySpec(encoded);
            return keyFactory.generatePrivate(keySpec);
        }
    }
}
