package com.http.testhttprpc;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

import sun.misc.BASE64Encoder;


/**
 * 
 * @Description: 服务消费者 
 * @Author chenkangxian   
 * @Date 2013-6-24 下午4:18:52 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 *
 */
public class DigestServiceConsumer extends HttpServlet{

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
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("service", service);
		params.put("format", format);
		params.put("arg1", arg1);
		String digest = "";
		try{
			digest = this.getDigest(params);
		}catch(Exception e){}
		
		String url = "http://localhost:8080//testsign/provider.do?"+"service=" + service + "&format=" + format + "&arg1=" + arg1;

		//组装请求
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("digest", digest);

		//接收响应
		HttpResponse response = httpClient.execute(httpGet);

		HttpEntity entity = response.getEntity();
		byte[] bytes = EntityUtils.toByteArray(entity);
		String jsonresult = new String(bytes);
		
		//从header里边接收摘要
		String serverResponseDigest = response.getLastHeader("digest").getValue();
		boolean validateResult = false;
		try {
			validateResult = validate(jsonresult,serverResponseDigest);
		} catch (Exception e) {}
		
		if(validateResult){
			JsonResult result = (JsonResult)JsonUtil.jsonToObject(jsonresult, JsonResult.class);
			resp.getWriter().write(result.getResult().toString());
		}else{
			resp.getWriter().write("validate fail!");
		}
		
	}
	
	/**
	 * 响应验证
	 * @param content 响应内容
	 * @param digest 摘要
	 * @return
	 * @throws Exception 
	 */
	private boolean validate(String responseContent, String digest) throws Exception{
		String secret = "abcdefjhijklmn";
		byte[] bytes = getMD5(responseContent + secret);
		String responseDigest = byte2base64(bytes);
		if(responseDigest.equals(digest)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 请求摘要
	 * @param params 请求参数
	 * @return
	 * @throws Exception
	 */
	private String getDigest(Map<String,String> params) throws Exception{
		
		String secret = "abcdefjhijklmn";
		
		Set<String> keySet = params.keySet();
		//使用treeset排序
		TreeSet<String> sortSet = new TreeSet<String>();
		sortSet.addAll(keySet);
		String keyvalueStr = "";
		Iterator<String> it = sortSet.iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = params.get(key);
			keyvalueStr += key + value;
		}
		
		keyvalueStr += secret;
		String base64Str = byte2base64(getMD5(keyvalueStr));
		return base64Str;
	}
	
	private static String byte2base64(byte[] bytes){
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(bytes);
	}
	
	public static byte[] getMD5(String content) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = md.digest(content.getBytes("utf8"));
		return bytes;
	}

}

