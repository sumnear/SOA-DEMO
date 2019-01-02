package com.http.testhttprpc;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
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


/**
 * 
 * @Description: 服务消费者 
 * @Author chenkangxian   
 * @Date 2013-6-24 下午4:18:52 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 *
 */
public class SignServiceConsumer extends HttpServlet{
	
	/**
	 * 服务消费者私钥
	 */
	private String consumerPrivateKey = "30fe0154020100300d06092afa48fa890d010101050004fe013e30fe013a0201000241009aa7db774fab536bfa35a95837941fac23fc35f1ce5536fa5eeb05bf278c1d97672f61613f3274a1064fc8049ae1733a04e50462f5bc5f7965d2505361b2d6a30203010001024100c103b0e6f300636a513f33aca95116751e7e4323f9a862d7fcefdeeb554d8b80e32ac11d68b30a63115b76871c6d08fedd20f3abb6bb59f46e0b9ffb44ca893102210083f373a6c77a4047a979ed71efbeb37268ec54f14fe4608573fe01fccdfad7f5022100971418336a97db442c6851014ae722ca77182636417c3f9b250fa654fdc47a37022005127251a6b7824531dd458ab4ded98974921ad29139bee69b19dfd4249939c902201d300e03b65cd384faf11dd3b6c8c8e91e84a0342529391b298d83445e26972b02202abc4ed8b7118dac167bc0ca56a00c0b697e7b25b2274eaf311b431077297941";
	
	/**
	 * 服务提供者公钥
	 */
	private String providePublicKey = "305c300d06092afa48fa890d0101010500034b003048024100b8e86ed2df351f375fd4b59a1b81560be89e9ba8b2c25461f63486741871c493d09792f5b930e2b5318b20e8fa75cc03960472109c9119b1c1d83aef88e664310203010001";

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
			digest = this.getSign(params);
		}catch(Exception e){}
		
		String url = "http://127.0.0.1:8080/testsign/signprovider.do?"+"service=" + service + "&format=" + format + "&arg1=" + arg1;

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
	 * @param digest 待验证摘要
	 * @return
	 * @throws Exception 
	 */
	private boolean validate(String responseContent, String digest) throws Exception{
		byte[] bytes = getMD5(responseContent );
		String responseDigest = bytes2hex(bytes);
		
		PublicKey publicKey = AsymmetricalUtil.string2PublicKey(providePublicKey);
		byte[] decryptBytes = AsymmetricalUtil.publicDecrypt(hex2bytes(digest), publicKey);
		String decryptDigest = bytes2hex(decryptBytes);
		
		if(responseDigest.equals(decryptDigest)){
			return true;
		}else{
			return false;
		}
	}
	
//	private boolean validate(String responseContent, String sign) throws Exception{
//		PublicKey publicKey = AsymmetricalUtil.string2PublicKey(providePublicKey);
//		Signature signature = Signature.getInstance("MD5withRSA");
//		signature.initVerify(publicKey);
//		signature.update(responseContent.getBytes());
//		return signature.verify(hex2bytes(sign));
//	}

	/**
	 * 请求摘要
	 * @param params 请求参数
	 * @return
	 * @throws Exception
	 */
	private String getSign(Map<String,String> params) throws Exception{
		
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
		
		byte[] md5Bytes = getMD5(keyvalueStr);
		PrivateKey privateKey = AsymmetricalUtil.string2PrivateKey(consumerPrivateKey);
		byte[] encryptBytes = AsymmetricalUtil.privateEncrypt(md5Bytes, privateKey);
		String hexStr = bytes2hex(encryptBytes);
		
		return hexStr;
	}
	
//	private String getSign(Map<String,String> params) throws Exception{
//		
//		Set<String> keySet = params.keySet();
//		//使用treeset排序
//		TreeSet<String> sortSet = new TreeSet<String>();
//		sortSet.addAll(keySet);
//		String keyvalueStr = "";
//		Iterator<String> it = sortSet.iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			String value = params.get(key);
//			keyvalueStr += key + value;
//		}
//		
//		PrivateKey privateKey = AsymmetricalUtil.string2PrivateKey(consumerPrivateKey);
//		Signature signature = Signature.getInstance("MD5withRSA");
//		signature.initSign(privateKey);
//		signature.update(keyvalueStr.getBytes());
//		
//		return bytes2hex(signature.sign());
//	}
	
	public static byte[] getMD5(String content) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = md.digest(content.getBytes("utf8"));
		return bytes;
	}
	
	/**
	 * 二进制转化为16进制
	 */
	private static String bytes2hex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			boolean negative = false;//是否为负数
			if(b < 0) negative = true;
			int inte = Math.abs(b);
			if(negative)inte = inte | 0x80;
			String temp = Integer.toHexString(inte & 0xFF);//负数会转成正数(最高位的负号变成数值计算),再转16进制
			if (temp.length() == 1) {
				hex.append("0");
			}
			hex.append(temp.toLowerCase());
		}
		return hex.toString();
	}
	
	/**
	 * 16进制转换成为2进制
	 */
	private static byte[] hex2bytes(String hex){
		byte[] bytes = new byte[hex.length()/2];
		for(int i = 0 ; i < hex.length(); i = i + 2){
			String subStr = hex.substring(i, i + 2);
			boolean negative = false;//是否为负数
			int inte = Integer.parseInt(subStr, 16);
			if(inte > 127) negative = true;
			if(inte == 128){
				inte = -128;
			}else if(negative){
				inte = 0 - (inte & 0x7F);
			}
			byte b = (byte)inte;
			bytes[i/2] = b;
		}
		return bytes;
	}
	


}

