/**
 * 
 */
package com.http.testredis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * @Description: redis api使用 
 * @Author chenkangxian   
 * @Date 2014-4-6 下午07:10:21 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public class RedisTest {


	public static void main(String[] args) {

		 Jedis  redis = new Jedis ("192.168.136.135",6379);//连接redis
		 
         //Keys 
		 System.out.println("=============keys=============");
         Set<String> keys = redis.keys("name*");//取的以name开始的key
         Iterator<String> it =keys.iterator() ;  
         while(it.hasNext()){  
             String key = it.next();  
             System.out.println(key);  
         } 
         System.out.println("=============keys=============");
         
         //String
         System.out.println("=============String=============");
         redis.set("name", "chenkangxian");//设置key-value
         redis.set("id", "123456");  
         redis.set("address", "hangzhou"); 
         redis.setex("content", 5, "hello");//设置有效期为5秒
         redis.mset("class","a","age","250"); //一次设置多个key-value
         redis.append("content", " lucy");//给字符串追加内容
         String content = redis.get("content"); //根据key获取value
         System.out.println(content);
         List<String> list = redis.mget("class","age");//一次取多个key
         for(int i=0;i<list.size();i++){  
             System.out.println(list.get(i));  
         }
         System.out.println("=============String=============");
         
         //Hashs使用
         System.out.println("=============hash=============");
         redis.hset("url", "google", "www.google.cn");//给hash设置值  
         redis.hset("url", "taobao", "www.taobao.com");  
         redis.hset("url", "sina", "www.sina.com.cn");  
         
         Map<String,String> map = new HashMap<String,String>();  
         map.put("name", "chenkangxian");  
         map.put("sex", "man");  
         map.put("age", "100");  
         redis.hmset("userinfo", map);//批量设置值
         
         String name = redis.hget("userinfo", "name");//取hash中某个key的值
         System.out.println(name);
         
         //取hash的多个key的值
         List<String> urllist = redis.hmget("url","google","taobao","sina");  
         for(int i=0;i<urllist.size();i++){  
             System.out.println(urllist.get(i));  
         }
         
         //取hash的所有key的值
         Map<String,String> userinfo = redis.hgetAll("userinfo");  
         for(Map.Entry<String,String> entry: userinfo.entrySet()) {  
              System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");  
         } 
         System.out.println("");
         System.out.println("=============hash=============");
         
         //list
         System.out.println("=============list=============");
         redis.lpush("charlist", "abc");//在list首部添加元素 
         redis.lpush("charlist", "def");  
         redis.rpush("charlist", "hij");//在list尾部添加元素
         redis.rpush("charlist", "klm");
         List<String> charlist = redis.lrange("charlist", 0, 2);  
         for(int i=0;i<charlist.size();i++){  
             System.out.println(charlist.get(i));  
         }
         
         redis.lpop("charlist");//在list首部删除元素
         redis.rpop("charlist");//在list尾部删除元素
         
         Long charlistSize = redis.llen("charlist");//获得list的大小
         System.out.println(charlistSize);  
         
         System.out.println("=============list=============");
         
         //set
         System.out.println("=============set=============");
		 redis.sadd("SetMem", "s1");//给Set添加元素
		 redis.sadd("SetMem", "s2");  
		 redis.sadd("SetMem", "s3");  
		 redis.sadd("SetMem", "s4");  
		 redis.sadd("SetMem", "s5");
		 
		 redis.srem("SetMem", "s5");//从Set中移除元素
		 
		 Set<String> set = redis.smembers("SetMem");//枚举出set的元素  
         Iterator<String> setit = set.iterator() ;  
         while(setit.hasNext()){  
             String setvalue=setit.next();  
             System.out.println(setvalue);  
         }  
         System.out.println("=============set=============");
         
         //sorted set
         System.out.println("=============sort set=============");
		 redis.zadd("SortSetMem", 1, "5th");//插入sort set,并指定权重
		 redis.zadd("SortSetMem", 2, "4th");
		 redis.zadd("SortSetMem", 3, "3th");
		 redis.zadd("SortSetMem", 4, "2th");
		 redis.zadd("SortSetMem", 5, "1th");
		 
		 //根据范围取set
		 Set<String> sortset = redis.zrange("SortSetMem", 2, 4);
		     Iterator<String> sortsetit = sortset.iterator() ;  
		     while(sortsetit.hasNext()){  
		         String setvalue=sortsetit.next();  
		         System.out.println(setvalue);  
		     }  
		     
		   //根据范围反向取set
		 Set<String> revsortset = redis.zrevrange("SortSetMem", 1, 2);
		 Iterator<String> revsortsetit = revsortset.iterator() ;  
		 while(revsortsetit.hasNext()){  
		     String setvalue=revsortsetit.next();  
		     System.out.println(setvalue);  
		 }  
         System.out.println("=============sort=============");
         

	}

}
