/**
 * 
 */
package com.http.testserialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * @Description: hessian序列化 
 * @Author chenkangxian   
 * @Date 2013-6-25 下午3:57:53 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestHessianSerialization {


	public static void main(String[] args) throws IOException {
	
		Person zhansan = new Person();
		zhansan.setAddress("hangzhou");
		zhansan.setAge(30);
		zhansan.setBirth(new Date());
		zhansan.setName("zhansan");
		
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		//hessian的序列化输出
		HessianOutput ho = new HessianOutput(os);
		ho.writeObject(zhansan);
		byte[] zhansanByte = os.toByteArray();
		
		ByteArrayInputStream is = new ByteArrayInputStream(zhansanByte);
		//hessian的反序列化读取对象
		HessianInput hi = new HessianInput(is);
		Person person = (Person)hi.readObject();
		
		System.out.println("name : " + person.getName() + ", age : " + person.getAge());
		
	}
	
	

}
