/**
 * 
 */
package com.http.testprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description: 服务端
 * @Author chenkangxian   
 * @Date 2013-6-14 下午09:12:18 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class Server {


	public static void main(String[] args) throws IOException{
		
			ServerSocket server = new ServerSocket(4567);
			
			while(true){
				
				Socket client = server.accept();
				
				//读取响应数据
				InputStream input = client.getInputStream();
				Request request = ProtocolUtil.readRequest(input);
				
				OutputStream output = client.getOutputStream();
				
				//组装响应
				Response response = new Response();
				response.setEncode(Encode.UTF8.getValue());
				if(request.getCommand().equals("HELLO")){
					response.setResponse("hello!");
				}else{
					response.setResponse("bye bye!");
				}
				response.setResponseLength(response.getResponse().length());
				
				ProtocolUtil.writeResponse(output, response);
				
				client.shutdownOutput();
				
			}

	}
}
