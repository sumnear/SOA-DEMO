/**
 * 
 */
package com.http.encrypt;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;

/**
 * @Description: 算法的性能测试
 * @Author chenkangxian   
 * @Date 2013-9-3 上午10:09:09 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestPerformance {

	public static byte[] testMD5(byte[] content) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = md.digest(content);
		return bytes;
	}

	/*********************DES加密算法***************************/
	public static SecretKey getKeyDES() throws Exception{
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(56);
		SecretKey key = keyGen.generateKey(); 
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
	/********************************************************************/

	/**********************RSA非对称加密算法***********************************/
	public static KeyPair getKeyPair() throws Exception{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator
				.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}

	public static PublicKey getPublicKey(KeyPair keyPair){
		PublicKey publicKey = keyPair.getPublic();
		return publicKey;
	}

	public static PrivateKey getPrivateKey(KeyPair keyPair){
		PrivateKey privateKey = keyPair.getPrivate();
		return privateKey;
	}

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
	/************************************************************************/

	
	/*由于RSA加密复杂度跟密钥的长度相关,而密钥的长度限制能加密数据的长度,因此无法进行性能比较*/
	public static void main(String[] args) throws Exception {
		File file = new File("c:\\aaa.txt");
		byte[] bytes = FileUtils.readFileToByteArray(file);
		int filesize = bytes.length /1024;//单位k
		System.out.println("filesize : " + filesize);

		//md5摘要
		long md5Start = System.currentTimeMillis();
		testMD5(bytes);
		long md5End = System.currentTimeMillis();
		System.out.println("md5 time cost : " + (md5End - md5Start) + " ms");


		//des对称加密
		SecretKey desKey = getKeyDES();
		long desEncryptStart = System.currentTimeMillis();
		byte[] desEnBytes = encryptDES(bytes,desKey);
		long desEncryptEnd = System.currentTimeMillis();
		System.out.println("des encrypt cost : " + (desEncryptEnd - desEncryptStart) + " ms");

		long desDecryptStart = System.currentTimeMillis();
		decryptDES(desEnBytes,desKey);
		long desDecryptEnd = System.currentTimeMillis();
		System.out.println("des decrypt cost : " + (desDecryptEnd - desDecryptStart) + " ms");


		//rsa非对称加密
		KeyPair keyPair = getKeyPair();
		PublicKey publicKey = getPublicKey(keyPair);
		PrivateKey privateKey = getPrivateKey(keyPair);

		long rsaEncryptStart = System.currentTimeMillis();
		byte[] rsaEnBytes =  publicEncrypt(bytes, publicKey);
		long rsaEncryptEnd = System.currentTimeMillis();
		System.out.println("rsa public key encrypt cost : " + (rsaEncryptEnd - rsaEncryptStart) + " ms");

		long rsaDecryptStart = System.currentTimeMillis();
		privateDecrypt(rsaEnBytes, privateKey);
		long rsaDecryptEnd = System.currentTimeMillis();
		System.out.println("rsa private key decrypt cost : " + (rsaDecryptEnd - rsaDecryptStart) + " ms");

	}

}
