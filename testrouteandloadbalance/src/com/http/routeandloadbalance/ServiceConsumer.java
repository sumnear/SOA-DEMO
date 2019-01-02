/**
 * 
 */
package com.http.routeandloadbalance;

import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @Description: 服务消费者 
 * @Author chenkangxian   
 * @Date 2013-7-29 下午7:17:36 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class ServiceConsumer {
	
	private List<String> serverList = new ArrayList<String>();
	
	private String serviceName = "service-B";
	
	
	//初始化服务地址信息
	public void init(){
		
		String serviceName = "service-B";
		String zkServerList = "192.168.136.130:2181";
		String SERVICE_PATH = "/configcenter/"+serviceName;//服务节点路径
		ZkClient zkClient = new ZkClient(zkServerList);
		
		boolean serviceExists = zkClient.exists(SERVICE_PATH);
		if(serviceExists){//服务存在,取地址列表
			serverList = zkClient.getChildren(SERVICE_PATH);
		}else{
			throw new RuntimeException("service not exist!");
		}
		
		//注册事件监听
		zkClient.subscribeChildChanges(SERVICE_PATH, new IZkChildListener(){

			@Override
			public void handleChildChange(String parentPath,
					List<String> currentChilds)
					throws Exception {
				serverList = currentChilds;
			}
			
		});
		
	}
	
	//消费服务
	public void consume(){
		//通过负载均衡算法，找到一台服务器进行调用
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		ServiceConsumer consumer = new ServiceConsumer();
		consumer.init();

		Thread.sleep(1000 * 60 * 60 * 24);
	}

}
