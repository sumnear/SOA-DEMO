/**
 * 
 */
package com.http.testhttprpc;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @Description: 非对称加密帮助类 
 * @Author chenkangxian   
 * @Date 2013-8-30 下午4:06:27 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class AsymmetricalUtil {

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
	 * 
	 * @param keyPair
	 * @return
	 */
	public static byte[] getPublicKeyBytes(KeyPair keyPair){
		PublicKey publicKey = keyPair.getPublic();
		byte[] bytes = publicKey.getEncoded();
		return bytes;
		
	}
	
	public static byte[] getPrivateKeyBytes(KeyPair keyPair){
		PrivateKey privateKey = keyPair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		return bytes;
		
	}
	
	public static boolean compairBytes(byte[] byte1,byte[] byte2){
		if(byte1.length != byte1.length)return false;
		for(int i = 0; i < byte1.length; i ++){
			if(byte1[i] != byte2[i]){
				System.out.println("byte1 : " + byte1[i] + ", byte2 : " + byte2[i]);
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception{
		KeyPair keyPair = getKeyPair();
//		String publicKey =  getPublicKey(keyPair);
//		String privateKey = getPrivateKey(keyPair);
//		System.out.println("private key : " + privateKey );
//		System.out.println("size : " + privateKey.length());
//		System.out.println("public key : "  + publicKey );
//		System.out.println("size : " + publicKey.length());
//		
//		PublicKey pub = string2PublicKey(publicKey);
//		PrivateKey pri = string2PrivateKey(privateKey);
		
		//byte[] bytes = getPublicKeyBytes(keyPair);
		byte[] bytes = getPrivateKeyBytes(keyPair);
		//byte[] bytes = {-128};
		String hexStr = bytes2hex(bytes);
		System.out.println(hexStr);
		byte[] hexByte = hex2bytes(hexStr);
		System.out.println(compairBytes(bytes,hexByte));
		
//		String temp = Integer.toHexString(-121 & 0xFF);
//		System.out.println(temp);
		//byte b = Byte.parseByte("86", 16);
		//byte b = Byte.parseByte("80", 16);
		
		//String a = "30820156020100300d06092a864886f70d0101010500048201403082013c020100024100c6e103f8b5b0df4b38ff628e7efb34dc77634348c5d9b44a2ac8e864abc07acc8b39b906e0d48e217d6116db2613baad986b52882387b591e6041efd7faf7bd70203010001024100b110f3c54296c827a40694cc03b84ab72340684e6980cbe8e0b0e15f881f25f4570eff786a3eec7ba75493c9317cf3ccec20f7056db8c71b49c8fcc261f8f5f9022100e3959a415ec80b585152c3520206a879c4adc5a3068d49593249a677408d4f4d022100dfb5e306cd6c3c788f17715b9a55a446e0a2ae6ad55289171a98111dc672adb3022014525b7fd2d2d21084d27b166b8fe4a35ae89969414084782e815d17f0efad5102210096511d0f0110ed4104c4f7c2595d9a5895b03a0a46f4827127c96e16d7b54ec9022100c3f2b36557dadd6f78f731f3576f056b225c9ac5cdc0513a10021b00ced849e5";
//		String a = "zhong guo shi ge she hui zhu yi guo jia !";
//		System.out.println(bytes2hex(a.getBytes()));
		//System.out.println(new String());
//		byte[] bytes = hex2bytes(a);
//		String b = bytes2hex(bytes);
//		System.out.println(a);
//		System.out.println(b);
//		System.out.println(new String(hex2bytes(bytes2hex(a.getBytes()))));
//		
//		System.out.println(new Byte((byte)127));
//		System.out.println(-127 & 0x80);
		
		byte a = (byte)128;
		System.out.println(a);
		

	}
}
