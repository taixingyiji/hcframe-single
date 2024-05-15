package com.taixingyiji.single.common.utils.vo;

import com.taixingyiji.base.common.ServiceException;
//import com.rcjava.util.CertUtil;
//import com.rcjava.util.KeyUtil;
//import com.rcjava.util.PemUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
//import org.bouncycastle.openssl.PEMParser;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * @author lhc
 * @version 1.0
 * @className CertVo
 * @date 2021年07月06日 1:30 下午
 * @description 证书处理类
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CertVo {

    private String privateKeyPem;
    private String certificatePem;
    private PrivateKey privateKey;
    private X509Certificate certificate;

    // * @author lhc
    // * @description //构造方法将privateKey和cert转换成字符串
    // * @date 2:03 下午 2021/7/6
    // * @params [privateKey, x509Certificate]
    // * @return
    // *
    public CertVo(PrivateKey privateKey, X509Certificate x509Certificate) {
//        this.privateKey = privateKey;
//        this.certificate = x509Certificate;
//        try {
//            this.privateKeyPem = PemUtil.toPemString(privateKey);
//            this.certificatePem = PemUtil.toPemString("CERTIFICATE", x509Certificate.getEncoded());
//        } catch (IOException | CertificateEncodingException e) {
//            throw new ServiceException(e);
//        }
    }

    // * @author lhc
    // * @description // 构造方法将privateKey和cert转为对象
    // * @date 2:04 下午 2021/7/6
    // * @params [privateKeyPem, certificatePem]
    // * @return
    // *
    public CertVo(String privateKeyPem, String certificatePem) {
//        this.privateKeyPem = privateKeyPem;
//        this.certificatePem = certificatePem;
//        PEMParser parser = new PEMParser(new StringReader(this.privateKeyPem));
//        try {
//            this.privateKey = KeyUtil.generatePrivateKey(parser,  "");
//            this.certificate = CertUtil.generateX509Cert(this.certificatePem);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
