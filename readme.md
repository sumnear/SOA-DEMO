### 大型电商分布式系统实践学习代码
#### 模块阅读顺序：  
##### 第一课 大型分布式网站的演变历程、SOA架构体系通信协议和远程调用
1. testprotocol
2. testserialization
3. testtcprpc
4. testhttprpc
5. testrestfulrpcs    
##### 第二课 SOA架构体系服务路由和服务治理
1. testrouteandloadbalance    //zookeeper 的注册发现服务
##### 第三课  常见的网站攻击手段和防御方式、安全加密算法以及使用场景
1. validatefile   //根据文件内容（魔数）判断文件类型
2. encrypt      //各种加密算法（对称 非对称 摘要）
##### 第四课  数字签名与数字证书、认证、HTTPS、OAuth
1. testcertificate
2. testsign
##### 第五课  缓存与持久化存储
##### 第六课  缓存持久化存储与消息系统  
1. testhbase
2. testredis
##### 第七课  垂直化搜索引擎
1.testlucene
##### 第八课  日志分析、服务器监控、jvm
##### 第九课  心跳检测、容量与水位、流控 
1. shell1
2. shell2
##### 第十课  高并发系统设计
1. testatomicandlock