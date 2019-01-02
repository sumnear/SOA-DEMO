/**
 * 
 */
package com.http.testrestfulrpc;

import java.util.Map;

/**
 * @Description: 基础服务接口 
 * @Author chenkangxian   
 * @Date 2013-6-24 下午8:23:26 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public interface BaseService {

	public Object execute(Map<String,Object> args);
}
