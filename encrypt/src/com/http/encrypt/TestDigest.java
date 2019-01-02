/**
 * 
 */
package com.http.encrypt;

import java.io.IOException;
import java.security.MessageDigest;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @Description: 摘要算法 
 * @Author chenkangxian   
 * @Date 2013-8-19 下午6:35:02 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestDigest {

	public static byte[] testMD5(String content) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = md.digest(content.getBytes("utf8"));
		return bytes;
	}
	
	public static byte[] testSHA1(String content)throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] bytes = md.digest(content.getBytes("utf8"));
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
		String hex = bytes2hex(a.getBytes());
		byte[] hexBytes = hex2bytes(hex);
		String base64 = byte2base64(hexBytes);
		byte[] base64Byte = base642byte(base64);
		
		System.out.println(new String(base64Byte));
		
		byte[] md5Bytes = testMD5(a);
		String md5Hex = bytes2hex(md5Bytes);
		
		System.out.println(md5Hex);
		
		byte[] sha1Bytes = testSHA1(a);
		String sha1Hex = bytes2hex(sha1Bytes);
		System.out.println(sha1Hex);

		
		
	}

}
