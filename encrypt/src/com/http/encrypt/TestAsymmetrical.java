/**
 * 
 */
package com.http.encrypt;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @Description: 非对称加密算法 
 * @Author chenkangxian   
 * @Date 2013-8-21 上午9:58:37 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestAsymmetrical {

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
		return bytes2hex(bytes);
		
	}
	
	public static String getPrivateKey(KeyPair keyPair){
		PrivateKey privateKey = keyPair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		return bytes2hex(bytes);
	}
	
	public static PublicKey string2PublicKey(String pubStr) 
			throws Exception{
		byte[] keyBytes = hex2bytes(pubStr);
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
		byte[] keyBytes = hex2bytes(priStr);
		/**
		 * This class represents the ASN.1 encoding of a private key,
		 * encoded according to the ASN.1 type
		 */
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes); 
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
	}
	
	/**公钥加密,私钥解密**/
	public static byte[] publicEncrypt(byte[] content, PublicKey publicKey)
			throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
	}
	public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey)
			throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
	}
	
	/**私钥加密,公钥解密    用来做数字签名**/
	public static byte[] privateEncrypt(byte[] content, PrivateKey privateKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
	}
	public static byte[] publicDecrypt(byte[] content, PublicKey publicKey)throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
	}
	
	
	/**
	 * 二进制转化为16进制
	 */
	private static String bytes2hex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			boolean negative = false;//是否为负数
			if(b < 0) negative = true;
			int inte = Math.abs(b);
			if(negative)inte = inte | 0x80;
			String temp = Integer.toHexString(inte & 0xFF);//负数会转成正数(最高位的负号变成数值计算),再转16进制
			if (temp.length() == 1) {
				hex.append("0");
			}
			hex.append(temp.toLowerCase());
		}
		return hex.toString();
	}
	
	/**
	 * 16进制转换成为2进制
	 */
	private static byte[] hex2bytes(String hex){
		byte[] bytes = new byte[hex.length()/2];
		for(int i = 0 ; i < hex.length(); i = i + 2){
			String subStr = hex.substring(i, i + 2);
			boolean negative = false;//是否为负数
			int inte = Integer.parseInt(subStr, 16);
			if(inte > 127) negative = true;
			if(inte == 128){
				inte = -128;
			}else if(negative){
				inte = 0 - (inte & 0x7F);
			}
			byte b = (byte)inte;
			bytes[i/2] = b;
		}
		return bytes;
	}
	
	
//	private static String byte2base64(byte[] bytes){
//		BASE64Encoder base64Encoder = new BASE64Encoder();
//		return base64Encoder.encode(bytes);
//	}
//	private static byte[] base642byte(String base64) throws IOException{
//		BASE64Decoder base64Decoder = new BASE64Decoder();
//		return base64Decoder.decodeBuffer(base64);
//	}
	
	public static void main(String[] args) throws Exception {
		KeyPair keyPair = getKeyPair();
		String publicKey =  getPublicKey(keyPair);
		String privateKey = getPrivateKey(keyPair);
		System.out.println("private key : " + privateKey );
		System.out.println("public key : "  + publicKey );
		
		PublicKey publics = string2PublicKey(publicKey);
		PrivateKey privates = string2PrivateKey(privateKey);
		
		String a = "hello,i am chenkangxian,good night!";
		byte[] publicBytes = publicEncrypt(a.getBytes(),publics);
		System.out.println("size : " + publicBytes.length);
		System.out.println(bytes2hex(publicBytes));
		byte[] aPublic = privateDecrypt(publicBytes,privates);
		System.out.println(new String(aPublic));
		
		byte[] privateBytes = privateEncrypt(a.getBytes(),privates);
		System.out.println("size : " + privateBytes.length);
		System.out.println(bytes2hex(privateBytes));
		byte[] aPrivate = publicDecrypt(privateBytes,publics);
		System.out.println(new String(aPrivate));
	}

}
