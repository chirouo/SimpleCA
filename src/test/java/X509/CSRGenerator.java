package X509;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.security.*;

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
}
