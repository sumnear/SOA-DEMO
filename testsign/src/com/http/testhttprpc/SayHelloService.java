package com.http.testhttprpc;

import java.util.Map;

public class SayHelloService implements BaseService{

	public Object execute(Map<String, Object> args) {
		//request.getParameterMap() 取出来为array,此处需要注意
		String[] helloArg = (String[]) args.get("arg1");
		
		if("hello".equals(helloArg[0])){
			return "hello";
		}else{
			return "bye bye";
		}
	}

}
