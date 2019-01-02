/**
 * 
 */
package com.http.testprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Description: 客户端
 * @Author chenkangxian   
 * @Date 2013-6-14 下午09:11:54 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		
			//请求
			Request request = new Request();
			request.setCommand("HELLO");
			request.setCommandLength(request.getCommand().length());
			request.setEncode(Encode.UTF8.getValue());
			
			System.out.println("commandlength : " + request.getCommandLength());
			System.out.println("command : " + request.getCommand());
			
			Socket client = new Socket("127.0.0.1",4567);
			
			OutputStream output = client.getOutputStream();
			
			//发送请求
			ProtocolUtil.writeRequest(output, request);
			
			//读取响应数据
			InputStream input = client.getInputStream();
			Response response = ProtocolUtil.readResponse(input);
			client.shutdownOutput();
			
			System.out.println("responselength : " + response.getResponseLength());
			System.out.println("response : " + response.getResponse());
			
	}

}
