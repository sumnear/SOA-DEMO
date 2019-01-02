/**
 * 
 */
package com.http.testserialization;

import java.util.Date;

/**
 * @Description: 人的属性集合 
 * @Author chenkangxian   
 * @Date 2013-6-25 下午4:45:59 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
class Person implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private int age;
	private String address;
	private Date birth;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
}
