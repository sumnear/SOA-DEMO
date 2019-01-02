/**
 * 
 */
package com.http.testcertificate;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @Description: 数字证书的使用 
 * @Author chenkangxian   
 * @Date 2013-10-2 下午02:31:29 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestCertificate {

	public static void main(String[] args) throws Exception {
		
		//1.加载密钥库
		String keyStorePath = "/home/longlong/ca_dir/client.p12";
		String password = "123456";
		KeyStore keystore = KeyStore.getInstance("pkcs12");
		FileInputStream keystoreFis = new FileInputStream(keyStorePath);
		keystore.load(keystoreFis, password.toCharArray());

		//2.获得私钥
		String alias = "chenkangxian";
		PrivateKey privateKey = (PrivateKey)keystore.getKey(alias, password.toCharArray());
		System.out.println(privateKey.getAlgorithm());

		//3.密钥库加载证书
		X509Certificate x509Certificate = (X509Certificate)
			keystore.getCertificate(alias);

		//4.获得公钥
		PublicKey publicKey = x509Certificate.getPublicKey();
		System.out.println(publicKey.getAlgorithm());
		
		
		//5.文件加载证书,目前java6只支持x509格式的证书
		String certPath = "/home/longlong/ca_dir/certs/client.cer";
		CertificateFactory certificateFactory = CertificateFactory.
												getInstance("X.509");
		FileInputStream certFis = new FileInputStream(certPath);
		X509Certificate certificate = (X509Certificate)certificateFactory
		 								.generateCertificate(certFis);
		System.out.println(certificate.getSigAlgName());
		

		//6.构建数字签名
		Signature signature = Signature.getInstance
				(x509Certificate.getSigAlgName());
		//使用证书校验数字签名,事实上用的是证书中的公钥
		signature.initVerify(x509Certificate);

	}

}
