/**
 * 
 */
package com.http.encrypt;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @Description: 数字签名API使用 
 * @Author chenkangxian   
 * @Date 2013-9-4 下午1:54:21 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestSingature {

	public static KeyPair getKeyPair() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
        		.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
	}
	
	public static String getPublicKey(KeyPair keyPair){
		PublicKey publicKey = keyPair.getPublic();
		byte[] bytes = publicKey.getEncoded();
		return byte2base64(bytes);
		
	}
	
	public static String getPrivateKey(KeyPair keyPair){
		PrivateKey privateKey = keyPair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		return byte2base64(bytes);
	}
	
	public static PublicKey string2PublicKey(String pubStr) 
			throws Exception{
		byte[] keyBytes = base642byte(pubStr);
		/**
		 * This class represents the ASN.1 encoding of a public key,
		 * encoded according to the ASN.1 type
		 */
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
	}
	
	public static PrivateKey string2PrivateKey(String priStr) 
			throws Exception{
		byte[] keyBytes = base642byte(priStr);
		/**
		 * This class represents the ASN.1 encoding of a private key,
		 * encoded according to the ASN.1 type
		 */
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes); 
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
	}
	
	private static String byte2base64(byte[] bytes){
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(bytes);
	}
	private static byte[] base642byte(String base64) throws IOException{
		BASE64Decoder base64Decoder = new BASE64Decoder();
		return base64Decoder.decodeBuffer(base64);
	}
	
//	private static byte[] sign(byte[] content, PrivateKey privateKey ) throws Exception{
//		Signature signature = Signature.getInstance("SHA1withRSA");
//		signature.initSign(privateKey);
//		signature.update(content);
//		return signature.sign();
//	}
//	
//	private static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) throws Exception{
//		Signature signature = Signature.getInstance("SHA1withRSA");
//		signature.initVerify(publicKey);
//		signature.update(content);
//		return signature.verify(sign);
//	}
	
	private static byte[] sign(byte[] content, PrivateKey privateKey ) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] bytes = md.digest(content);
		Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptBytes = cipher.doFinal(bytes);
		return encryptBytes;
	}
	
	private static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] bytes = md.digest(content);
		Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptBytes = cipher.doFinal(sign);
		if(byte2base64(decryptBytes).equals(byte2base64(bytes))){
			return true;
		}else{
			return false;
		}
	}
	
	

	public static void main(String[] args) throws Exception {
		
		KeyPair keyPair = getKeyPair();
		String publicKey =  getPublicKey(keyPair);
		String privateKey = getPrivateKey(keyPair);
		
		PublicKey publics = string2PublicKey(publicKey);
		PrivateKey privates = string2PrivateKey(privateKey);
		
		String a = "hello,i am chenkangxian,good night!";
		
		
		byte[] sign = sign(a.getBytes(),privates);
		boolean verify = verify(a.getBytes(),sign,publics);
		
		System.out.println(verify);

	}

}
