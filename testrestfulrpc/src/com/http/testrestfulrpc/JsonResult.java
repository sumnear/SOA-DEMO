/**
 * 
 */
package com.http.testrestfulrpc;

import javax.xml.bind.annotation.XmlRootElement; 

/**
 * @Description: json结果集 
 * @Author chenkangxian   
 * @Date 2013-6-24 下午8:33:24 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
@XmlRootElement(name="JsonResult")
public class JsonResult {

	//结果状态码
	private int resultCode;
	//状态码解释消息
	private String message;
	//结果
	private Object result;
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	
}
