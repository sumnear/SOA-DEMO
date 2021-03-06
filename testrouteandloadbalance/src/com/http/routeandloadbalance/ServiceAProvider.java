/**
 * 
 */
package com.http.routeandloadbalance;

import java.net.InetAddress;
import org.I0Itec.zkclient.ZkClient;

/**
 * @Description: service provider A 实现 
 * @Author chenkangxian   
 * @Date 2013-7-29 下午7:21:54 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class ServiceAProvider {
	
	private String serviceName = "service-A";
	
	//向zookeeper注册服务
	public void init() throws Exception{
		
		String serverList = "192.168.136.130:2181";
		String PATH = "/configcenter";//根节点路径
		ZkClient zkClient = new ZkClient(serverList);
		
		boolean rootExists = zkClient.exists(PATH);
		
		if(!rootExists){
			zkClient.createPersistent(PATH);
		}
		
		boolean serviceExists = zkClient.exists(PATH + "/" + serviceName);
		
		if(!serviceExists){
			zkClient.createPersistent(PATH + "/" + serviceName);//创建服务节点
		}
		
		//注册当前服务器和权重
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString();//获得本机IP
		
		zkClient.createEphemeral(PATH + "/" + serviceName + "/" + ip);
	}
	
	//提供服务
	public void provide(){
		
	}

	public static void main(String[] args) throws Exception {
		ServiceAProvider a = new ServiceAProvider();
		a.init();
		
		Thread.sleep(1000 * 60 * 60 * 24);
	}

}
