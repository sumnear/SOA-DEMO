/**
 * 
 */
package com.http.testprotocol;

/**
 * @Description: 协议响应
 * @Author chenkangxian   
 * @Date 2013-6-14 下午09:11:28 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class Response {
	
	/**
	 * 编码
	 */
	private byte encode;

	/**
	 * 响应长度
	 */
	private int responseLength;
	
	/**
	 * 响应
	 */
	private String response;

	public int getResponseLength() {
		return responseLength;
	}

	public void setResponseLength(int responseLength) {
		this.responseLength = responseLength;
	}


	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public byte getEncode() {
		return encode;
	}

	public void setEncode(byte encode) {
		this.encode = encode;
	}
	
}
