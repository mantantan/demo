#### ISV轻应用通过开放平台接入企业空间文档说明(主要对于ISV开发人员)：
1. 在开放平台官网登录(如果没有账号请注册并登录)，进入开放平台后台管理界面。
2. 在套件管理页签下，点击右上角"添加套件/应用"，按要求填写完善后申请上架；在"测试空间管理"中新建测试空间；在企业资质中注册企业并认证
3. 编码开发工作如下：ISV套件后台主要完成接收ticket和临时授权码部分的编码开发(解密ticket流程参考开放平台官方文档，接入代码参考Isv授权Demo)。
4. 审核通过后，进入测试空间点击相应套件的授权按钮，授权给该空间，并将套件中的应用添加至该空间即可。

##### 特别说明:
1. 在添加套件过程中填写的"回调url"<br/> 示例:"http://具有外网的ip:port/项目路径(如果有)/suite/open_api_push"
(2) 开放平台[线上地址]：https://open.yonyoucloud.com
(3) 解密消息异常java.security.InvalidKeyException:illegal Key Size的解决方案<br/>
```
    * 
 	* 在官方网站下载JCE无限制权限策略文件（JDK7的下载地址：
 	* http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
 	* 下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt
	* 如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件
 	* 如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件
	* 
```

(4) properties文件说明:

```
    # 开放平台openapi host
    isv_auth_base_path=https://openapi.yonyoucloud.com/
    # suite配置，下列信息均能配置在开放平台套件管理的套件中
    suite_config.token  => 创建套件的时候输入的token
    suite_config.suiteKey => 套件id
    suite_config.suiteSecret => 套件秘钥
    suite_config.EncodingAESKey => 加密解密的AESKey，创建套件的时候输入的key
```

(5)!!特别注意的是!! <br/> 强烈建议您把接收到的"suiteKey"、"空间Id"和"永久授权码"持久化到数据库中用于标识 "哪个空间的哪个套件被授权!"

详细文档可参考： [开放平台文档中心](https://open.yonyoucloud.com/doc?id=dfa2148e43683828af0c304c5a7dde15)

