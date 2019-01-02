/**
 * 
 */
package com.http.validatefile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: 判断文件类型 
 * @Author chenkangxian   
 * @Date 2013-8-25 下午10:04:43 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public final class FileTypeJudge {
		
	/**
	 * 二进制转化为16进制
	 */
	private static String bytes2hex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				hex.append("0");
			}
			hex.append(temp.toLowerCase());
		}
		return hex.toString();
	}
   
	/**
	 * 读取文件头
	 */
	private static String getFileHeader(String filePath) throws IOException {
		byte[] b = new byte[28];//这里需要注意的是,每个文件的magic word的长度都不相同,因此需要使用startwith
		InputStream inputStream = null;
		inputStream = new FileInputStream(filePath);
		inputStream.read(b, 0, 28);
		inputStream.close();
				
		return bytes2hex(b);
	}
	
	/**
	 * 判断文件类型
	 */
	public static FileType getType(String filePath) throws IOException {
		
		String fileHead = getFileHeader(filePath);
		if (fileHead == null || fileHead.length() == 0) {
			return null;
		}
		fileHead = fileHead.toUpperCase();
		FileType[] fileTypes = FileType.values();
		for (FileType type : fileTypes) {
			if (fileHead.startsWith(type.getValue())) {
				return type;
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception{
		 System.out.println(FileTypeJudge.getType("D:\\Download\\doc.rar"));
	}
}

