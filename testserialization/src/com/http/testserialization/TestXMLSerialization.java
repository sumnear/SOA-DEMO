/**
 * 
 */
package com.http.testserialization;

import java.util.Date;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @Description: 使用xml进行对象序列化 
 * @Author chenkangxian   
 * @Date 2013-7-3 下午10:10:18 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class TestXMLSerialization {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Person person = new Person();
		person.setAddress("hangzhou,china");
		person.setAge(18);
		person.setBirth(new Date());
		person.setName("zhangsan");
		
		//将person对象序列化为XML
		XStream xStream = new XStream(new DomDriver());
		//设置Person类的别名
		xStream.alias("person", Person.class);
		String personXML = xStream.toXML(person);
		
		//将XML反序列化还原为person对象
		Person zhangsan = (Person)xStream.fromXML(personXML);
		
		System.out.println(personXML);
		System.out.println(zhangsan.getBirth());

	}

}
