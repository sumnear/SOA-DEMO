/**
 * 
 */
package com.http.testprotocol;

import java.util.Arrays;

/**
 * @Description: 字节转换
 * @Author chenkangxian   
 * @Date 2013-6-14 下午10:07:48 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class ByteUtil {

	/**
	 * java为Big Endian字节序
	 * 低地址 ------> 高地址
	 *  高位           低位
	 * @param bytes
	 * @return
	 */
	public static int bytes2Int(byte[] bytes) {  
		int num = bytes[3] & 0xFF;  
		num |= ((bytes[2] << 8) & 0xFF00);  
		num |= ((bytes[1] << 16) & 0xFF0000);  
		num |= ((bytes[0] << 24) & 0xFF000000);  
		return num;  
	} 

	public static byte[] int2ByteArray(int i) {   
		byte[] result = new byte[4];   
		result[0] = (byte)((i >> 24) & 0xFF);
		result[1] = (byte)((i >> 16) & 0xFF);
		result[2] = (byte)((i >> 8) & 0xFF); 
		result[3] = (byte)(i & 0xFF);
		return result;
	}


}
