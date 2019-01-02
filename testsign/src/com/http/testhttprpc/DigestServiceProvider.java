/**
 * 
 */
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

import sun.misc.BASE64Encoder;


/**
 * @Description: 服务提供者 
 * @Author chenkangxian   
 * @Date 2013-6-24 下午4:15:30 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class DigestServiceProvider  extends HttpServlet{
	
	private Map<String,BaseService> serviceMap ;
	

	@Override
	public void init() throws ServletException {
		//服务map初始化
		serviceMap = new HashMap<String,BaseService>();
		serviceMap.put("com.http.sayhello", new SayHelloService());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Map params = req.getParameterMap();
		String requestDigest = req.getHeader("digest");
		boolean validateResult = false;
		try {
			validateResult = validate(params, requestDigest);
		} catch (Exception e) {e.printStackTrace();}
		if(!validateResult){
			resp.getWriter().write("validate fail!");
			return ;
		}
		
		//基本参数
		String servicename = req.getParameter("service");
		String format = req.getParameter("format");
		
		Map parameters =  req.getParameterMap();
		
		BaseService service = serviceMap.get(servicename);
		Object result = service.execute(parameters);
		
		//生成json结果集
		JsonResult jsonResult = new JsonResult();
		jsonResult.setResult(result);
		jsonResult.setMessage("success");
		jsonResult.setResultCode(200);
		
		String json = JsonUtil.getJson(jsonResult);
		String digest = "";
		try {
			digest = getDigest(json);
		} catch (Exception e) {}
		resp.setHeader("digest", digest);
		resp.getWriter().write(json);
	}
	
	private boolean validate(Map params, String digest) throws Exception{
		
		String secret = "abcdefjhijklmn";
		Set<String> keySet = params.keySet();
		//使用treeset排序
		TreeSet<String> sortSet = new TreeSet<String>();
		sortSet.addAll(keySet);
		String keyvalueStr = "";
		Iterator<String> it = sortSet.iterator();
		while(it.hasNext()){
			String key = it.next();
			String[] values = (String[])params.get(key);
			keyvalueStr += key + values[0];
		}
		keyvalueStr += secret;
		String base64Str = byte2base64(getMD5(keyvalueStr));
		if(base64Str.equals(digest)){
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
	private String getDigest(String content) throws Exception{
		String secret = "abcdefjhijklmn";
		content += secret;
		String base64Str = byte2base64(getMD5(content));
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
