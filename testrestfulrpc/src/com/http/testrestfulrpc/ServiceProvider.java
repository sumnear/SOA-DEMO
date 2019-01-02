/**
 * 
 */
package com.http.testrestfulrpc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor; 

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 服务提供者 
 * @Author chenkangxian   
 * @Date 2013-7-13 下午5:13:43 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
/*
 * 请求地址
 * http://localhost:8080/testrestfulrpc/provider/sayhelloservice/2013-11-12-11-12-23.json
 */
@Controller
public class ServiceProvider {

	private Map<String,BaseService> serviceMap ;

	public ServiceProvider() {
		//服务map初始化
		serviceMap = new HashMap<String,BaseService>();
		serviceMap.put("sayhelloservice", new SayHelloService());
	}


	@InitBinder    
	public void initBinder(WebDataBinder binder) {    
		//设置日期转换  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");    
		dateFormat.setLenient(false);    
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));    
	}   

	@ResponseBody 
	@RequestMapping(value="/provider/{servicename}/{timestamp}", method=RequestMethod.POST)
	public JsonResult provide(HttpServletRequest request,HttpServletResponse response,
			@PathVariable("servicename") String servicename,
			@PathVariable("timestamp") Date timestamp){

		//取得参数
		Map parameters =  request.getParameterMap();
		BaseService service = serviceMap.get(servicename);
		Object result = service.execute(parameters);

		//生成json结果集
		JsonResult jsonResult = new JsonResult();
		jsonResult.setResult(result);
		jsonResult.setMessage("success");
		jsonResult.setResultCode(200);

		return jsonResult;
	}
}
