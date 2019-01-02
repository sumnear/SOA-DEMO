/**
 * 
 */
package com.http.testserialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @Description: java序列化 
 * @Author chenkangxian   
 * @Date 2013-6-25 下午3:43:08 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestJavaSerialization {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		
		Person zhansan = new Person();
		zhansan.setAddress("hangzhou");
		zhansan.setAge(30);
		zhansan.setBirth(new Date());
		zhansan.setName("zhansan");
		//定义一个字节数组输出流
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		//对象输出流
		ObjectOutputStream out = new ObjectOutputStream(os);
		//将对象写入到字节数组输出，进行序列化
		out.writeObject(zhansan);
		byte[] zhansanByte = os.toByteArray();
		
		//字节数组输入流
		ByteArrayInputStream is = new ByteArrayInputStream(zhansanByte);
		//执行反序列化，从流中读取对象
		ObjectInputStream in = new ObjectInputStream(is);
		Person person = (Person)in.readObject();
		
		System.out.println("name : " + person.getName() + ", age : " + person.getAge());
		os.close();
		is.close();
	}
	
	
}
