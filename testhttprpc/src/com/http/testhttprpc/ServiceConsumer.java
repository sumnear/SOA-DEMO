package com.http.testhttprpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


/**
 * 
 * @Description: 服务消费者 
 * @Author chenkangxian   
 * @Date 2013-6-24 下午4:18:52 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 *
 */
public class ServiceConsumer extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//参数
		String service = "com.http.sayhello";
		String format = "json";
		String arg1 = "hello";
		
		
		String url = "http://localhost:8080//testhttprpc/provider.do?"+"service=" + service + "&format=" + format + "&arg1=" + arg1;

		//组装请求
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);	

		//接收响应
		HttpResponse response = httpClient.execute(httpGet);

		HttpEntity entity = response.getEntity();
		byte[] bytes = EntityUtils.toByteArray(entity);
		String jsonresult = new String(bytes, "utf8");
		
		JsonResult result = (JsonResult)JsonUtil.jsonToObject(jsonresult, JsonResult.class);

		resp.getWriter().write(result.getResult().toString());
		
	}



}

