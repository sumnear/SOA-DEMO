/**
 * 
 */
package com.http.testtcprpc;

/**
 * @Description: say hello的接口
 * @Author chenkangxian   
 * @Date 2013-6-20 下午6:51:09 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public interface SayHelloService {

	/**
	 * 问好的接口
	 * @param helloArg 参数
	 * @return
	 */
	public String sayHello(String helloArg);
}
