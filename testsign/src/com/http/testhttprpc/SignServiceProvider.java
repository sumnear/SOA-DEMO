/**
 * 
 */
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


/**
 * @Description: 服务提供者 
 * @Author chenkangxian   
 * @Date 2013-6-24 下午4:15:30 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class SignServiceProvider  extends HttpServlet{
	
	private Map<String,BaseService> serviceMap ;
	
	/**
	 * 服务消费者公钥
	 */
	private String consumerPublicKey = "305c300d06092afa48fa890d0101010500034b0030480241009aa7db774fab536bfa35a95837941fac23fc35f1ce5536fa5eeb05bf278c1d97672f61613f3274a1064fc8049ae1733a04e50462f5bc5f7965d2505361b2d6a30203010001";

	/**
	 * 服务提供者私钥
	 */
	private String providePrivateKey = "30fe0154020100300d06092afa48fa890d010101050004fe013e30fe013a020100024100b8e86ed2df351f375fd4b59a1b81560be89e9ba8b2c25461f63486741871c493d09792f5b930e2b5318b20e8fa75cc03960472109c9119b1c1d83aef88e6643102030100010240688de00d4c19102117ccc19bc04642b3bcf50a78860af887edd90e5709d910ee5a9dceb8b19d8e16a821843fdfb6bf82b50b24e1cd5c20d2d6a1593d77c3d77102210098b9a8284dbbdbe828968f8a3a1606ff2805fc755464f7ce3bb2ec317a1b2935022100a4e6c7f9a3dee84c9d6399cbaa2006506c0c5ac52ea5d86566f795f0cbe62af30221009ecba01c40befe28adef1ea2bc6bc9220b2c7479211c59b130a85d146294191102202b0d6f23a6a93ef3aea6b27fd8383bbd3a32890a0cb305fe34f2c61055c0d2f702204531febc04f26b9c9765713f5356e65a925971bc5b674216270a3d4edaf0c220";
	
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
			digest = getSign(json);
		} catch (Exception e) {}
		resp.setHeader("digest", digest);
		resp.getWriter().write(json);
	}
	
	private boolean validate(Map params, String digest) throws Exception{

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
		String hexStr = bytes2hex(getMD5(keyvalueStr));
		
		PublicKey publicKey = AsymmetricalUtil.string2PublicKey(consumerPublicKey);
		byte[] decryptBytes = AsymmetricalUtil.publicDecrypt(hex2bytes(digest), publicKey);
		String decryptDigest = bytes2hex(decryptBytes);
		
		if(hexStr.equals(decryptDigest)){
			return true;
		}else{
			return false;
		}
	}
	
//	private boolean validate(Map params, String sign) throws Exception{
//		
//		Set<String> keySet = params.keySet();
//		//使用treeset排序
//		TreeSet<String> sortSet = new TreeSet<String>();
//		sortSet.addAll(keySet);
//		String keyvalueStr = "";
//		Iterator<String> it = sortSet.iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			String[] values = (String[])params.get(key);
//			keyvalueStr += key + values[0];
//		}
//
//		PublicKey publicKey = AsymmetricalUtil.string2PublicKey(consumerPublicKey);
//		Signature signature = Signature.getInstance("MD5withRSA");
//		signature.initVerify(publicKey);
//		signature.update(keyvalueStr.getBytes());
//		return signature.verify(hex2bytes(sign));
//		
//	}
	
	/**
	 * 请求摘要
	 * @param content 请求参数
	 * @return
	 * @throws Exception
	 */
//	private String getSign(String content) throws Exception{
//		byte[] md5Bytes = getMD5(content);
//		PrivateKey privateKey = AsymmetricalUtil.string2PrivateKey(providePrivateKey);
//		byte[] encryptBytes = AsymmetricalUtil.privateEncrypt(md5Bytes, privateKey);
//		String hexStr = bytes2hex(encryptBytes);
//		return hexStr;
//	}
	
	private String getSign(String content) throws Exception{
		PrivateKey privateKey = AsymmetricalUtil.string2PrivateKey(providePrivateKey);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateKey);
		signature.update(content.getBytes());
		String hexStr = bytes2hex(signature.sign());
		return hexStr;
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
	
	public static byte[] getMD5(String content) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = md.digest(content.getBytes("utf8"));
		return bytes;
	}
}
