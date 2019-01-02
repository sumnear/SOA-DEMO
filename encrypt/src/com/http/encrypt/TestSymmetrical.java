/**
 * 
 */
package com.http.encrypt;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @Description: 对称加密的使用 
 * @Author chenkangxian   
 * @Date 2013-8-20 下午3:08:20 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestSymmetrical {

	/*******DES加密算法*********/

	/**
	 * 每次生成的key都不相同,因此生成的key需要保存下来,否则下次无法解密
	 * @return
	 * @throws Exception
	 */
	public static String genKeyDES() throws Exception{
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(56);
        SecretKey key = keyGen.generateKey(); 
        String base64Str = byte2base64(key.getEncoded());
        return base64Str;
	}
	
	public static SecretKey loadKeyDES(String base64Key) throws Exception{
		byte[] bytes = base642byte(base64Key);
		SecretKey key = new SecretKeySpec(bytes, "DES");
		return key;
	}
	
	public static byte[] encryptDES(byte[] source,SecretKey key)throws Exception{
		Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(source);
        return bytes;
	}
	
	public static byte[] decryptDES(byte[] source,SecretKey key) throws Exception{
		Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(source);
        return bytes;
	}
	
	/****AES算法加密****/
	//默认为128,AES算法也支持 192 和 256,但是jdk由于美国对于加密软件出口的控制,如果使用256位的密钥,则需要下载无政策和司法限制的文件,解决办法可以可以参照http://stackoverflow.com/questions/6481627/java-security-illegal-key-size-or-default-parameters
	public static String genKeyAES() throws Exception{
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
        SecretKey key = keyGen.generateKey(); 
        String base64Str = byte2base64(key.getEncoded());
        return base64Str;
	}
	
	public static SecretKey loadKeyAES(String base64Key) throws Exception{
		byte[] bytes = base642byte(base64Key);
		SecretKey key = new SecretKeySpec(bytes, "AES");
		return key;
	}
	
	public static byte[] encryptAES(byte[] source,SecretKey key)throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(source);
        return bytes;
	}
	
	public static byte[] decryptAES(byte[] source,SecretKey key) throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(source);
        return bytes;
	}
	
	
	private static String byte2base64(byte[] bytes){
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(bytes);
	}
	private static byte[] base642byte(String base64) throws IOException{
		BASE64Decoder base64Decoder = new BASE64Decoder();
		return base64Decoder.decodeBuffer(base64);
	}
	
	public static void main(String[] args) throws Exception {
		String a = "hello,i am chenkangxian,good night!";
		//DES加密
		String desKeyStr = genKeyDES();
		System.out.println(desKeyStr);
		SecretKey desKey = loadKeyDES(desKeyStr);
		byte[]desEnBytes = encryptDES(a.getBytes(),desKey);
		System.out.println(byte2base64(desEnBytes));
		byte[] desDeBytes = decryptDES(desEnBytes,desKey);
		System.out.println(new String(desDeBytes));
		
		System.out.println("======================================");
		
		//AES加密
		String aesKeyStr = genKeyAES();
		System.out.println(aesKeyStr);
		SecretKey aesKey = loadKeyAES(aesKeyStr);
		byte[] aesEnBytes = encryptAES(a.getBytes(),aesKey);
		System.out.println(byte2base64(aesEnBytes));
		byte[] aesDeBytes = decryptAES(aesEnBytes,aesKey);
		System.out.println(new String(aesDeBytes));
	}

}
