package com.gx.ca.X509;

import com.gx.ca.entity.CaRDTO;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

@Component
public class CSRParser {
    public static void main(String[] args) throws Exception {
//        // 读取 PEM 格式的 CSR 文件
//        try (PEMParser pemParser = new PEMParser(new FileReader("server.csr"))) {
//            // 读取 PEM 文件中的 CSR 内容
//            Object object = pemParser.readObject();
//
//            // 如果对象是 PKCS10CertificationRequest 类型
//            if (object instanceof PKCS10CertificationRequest) {
//                PKCS10CertificationRequest csr = (PKCS10CertificationRequest) object;
//
//                // 提取主题信息
//                X500Name subject = csr.getSubject();
//                RDN[] rdns = subject.getRDNs();
//                System.out.println("Subject:");
//                for (RDN rdn : rdns) {
//                    System.out.println(" - " + BCStyle.INSTANCE.oidToDisplayName(rdn.getFirst().getType()) + ": " + rdn.getFirst().getValue());
//                }
//
//                // 提取公钥信息
//                SubjectPublicKeyInfo publicKeyInfo = csr.getSubjectPublicKeyInfo();
//                System.out.println("Public Key: " + publicKeyInfo);
//
//                // 通过 KeyFactory 将 SubjectPublicKeyInfo 解码为 PublicKey
//                PublicKey publicKey = getPublicKey(publicKeyInfo);
//
//                // 使用 JcaContentVerifierProviderBuilder 创建验证提供者
//                if (csr.isSignatureValid(new JcaContentVerifierProviderBuilder().build(publicKey))) {
//                    System.out.println("CSR 签名验证成功！");
//                } else {
//                    System.out.println("CSR 签名验证失败！");
//                }
//
//                // 打印解析成功信息
//                System.out.println("CSR 文件读取成功！");
//            } else {
//                System.out.println("文件内容不是有效的 CSR 文件。");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        CaRDTO caRDTO = parserCrFile("C:\\Users\\26274\\IdeaProjects\\CA\\www_example_com.csr");
//        System.out.println(caRDTO);
    }

    // 根据公钥信息生成 PublicKey
    private static PublicKey getPublicKey(SubjectPublicKeyInfo publicKeyInfo) throws Exception {
        // 获取公钥的字节编码
        byte[] encoded = publicKeyInfo.getEncoded();

        // 使用 KeyFactory 来生成 PublicKey 对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // 或者使用 "EC" 对于椭圆曲线密钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);

        // 这里假设我们使用 RSA 公钥， 如果是 EC 公钥则使用 ECKeySpec
        return keyFactory.generatePublic(keySpec);
    }
    // 提取公钥的方法
    public PublicKey extractPublicKey(byte[] pemFileByte) throws Exception {
        // 使用 X509EncodedKeySpec 从字节数组中恢复公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemFileByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // 选择相应的算法
        return keyFactory.generatePublic(keySpec);
    }
    public  CaRDTO parserCrFile(String path){
        CaRDTO caRDTO = new CaRDTO();

        try (PEMParser pemParser = new PEMParser(new FileReader(path))) {
            // 读取 PEM 文件中的 CSR 内容
            Object object = pemParser.readObject();

            // 如果对象是 PKCS10CertificationRequest 类型
            if (object instanceof PKCS10CertificationRequest) {
                PKCS10CertificationRequest csr = (PKCS10CertificationRequest) object;

                // 提取主题信息
                X500Name subject = csr.getSubject();
                RDN[] rdns = subject.getRDNs();
                System.out.println("Subject:");
                // 遍历 RDN,提取信息
                for (RDN rdn : rdns) {
                    String type = BCStyle.INSTANCE.oidToDisplayName(rdn.getFirst().getType());
                    String value = rdn.getFirst().getValue().toString();

                    if ("CN".equals(type)) {
                        caRDTO.setCommonName(value);
                    } else if ("O".equals(type)) {
                        caRDTO.setOrganization(value);
                    } else if ("L".equals(type)) {
                        caRDTO.setLocality(value);
                    } else if ("ST".equals(type)) {
                        caRDTO.setProvince(value);
                    } else if ("C".equals(type)) {
                        caRDTO.setCountry(value);
                    }
                    // 打印每个 RDN 信息
                    System.out.println(" - " + type + ": " + value);
                }

                // 提取公钥信息
                SubjectPublicKeyInfo publicKeyInfo = csr.getSubjectPublicKeyInfo();
                System.out.println("Public Key: " + publicKeyInfo);

                // 将公钥编码为 Base64 并存储到 CaRDTO
                String base64PublicKey = Base64.toBase64String(publicKeyInfo.getEncoded());
                caRDTO.setPublicKey(base64PublicKey);

                // 打印解析成功信息
                System.out.println("CSR 文件读取成功！");
            } else {
                System.out.println("文件内容不是有效的 CSR 文件。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回 CaRDTO 对象
        return caRDTO;
    }
}
