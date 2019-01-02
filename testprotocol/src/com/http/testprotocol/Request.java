/**
 * 
 */
package com.http.testprotocol;

/**
 * 
* @Description: 协议请求 
* @Author chenkangxian   
* @Date 2013-6-14 下午09:10:52 
* @Copyright: 2012 chenkangxian, All rights reserved.
*
 */
public class Request {

	/**
	 * 协议编码
	 */
	private byte encode;

	/**
	 * 命令
	 */
	private String command;
	
	/**
	 * 命令长度
	 */
	private int commandLength;
	

	public byte getEncode() {
		return encode;
	}

	public void setEncode(byte encode) {
		this.encode = encode;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getCommandLength() {
		return commandLength;
	}

	public void setCommandLength(int commandLength) {
		this.commandLength = commandLength;
	}

}
