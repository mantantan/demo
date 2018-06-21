#1 获取AccessToken

1、地址

https://openapi.yonyoucloud.com/token
	
2、提交方式

GET
	
3、参数

字段|必须|类型|说明
---|---|---|---
appid|Y|String|凭证id，参见《友空间消息接入说明》6.1.1
secret|Y|String|凭证secret，参见《友空间消息接入说明》6.1.1

4、 返回结果

字段|必须|类型|说明
---|---|---|---
access_token|Y|String|token值
expires_in|Y|Integer|有效期（秒）

5、 结果示例

```
{
	"code":0,
	"msg":"",
	"data":{
		"access_token":"ACCESS_TOKEN",
		"expires_in":7200
	}
}
```

#2 发送消息到服务号
##2.1 发送文本消息到服务号

1、地址

https://openapi.yonyoucloud.com/rest/message/service/txt?access_token=ACCESS_TOKEN

2、方法

POST

3、参数（RequestBody）

```
{
	"spaceId":"空间ID",
	"pubAccId":"服务号ID",
	"sendScope":"list",
	"to":[
		"MEMBER_ID1",
		"MEMBER_ID2"
	],
	"content":"文本消息内容"
}
```

字段|必须|类型|说明
---|---|---|---
access_token|是|String|开放平台access_token，参见#1
spaceId|是|String|空间ID
pubAccId|是|String|服务号ID
sendScope|是|String|发送范围，list:发送给to参数数组中成员
to|是|Array|发送对象
content|是|String|文本消息内容

4、返回值

```
{
	"flag": 0,
	"message":""
}
```

5、失败

HTTP StatusCode|原因
---|---
400|参数不合法
401|认证信息错误
405|Method Not Allowed，HTTP请求方法错误
500|内部服务器错误

##2.2 发送分享消息（业务消息）到服务号

1、地址

https://openapi.yonyoucloud.com/rest/message/service/share?access_token=ACCESS_TOKEN

2、方法

POST

3、参数（RequestBody）

```
{
	"spaceId":"空间ID",
	"pubAccId":"服务号ID",
	"sendScope":"list",
	"to":[
		"MEMBER_ID1",
		"MEMBER_ID2"
	],
	"title":"消息标题",
	"desc":"消息描述",
	"detailUrl":"http://ec.yonyoucloud.com/detail"
}
```

字段|必须|类型|说明
---|---|---|---
access_token|是|String|开放平台access_token，参见#1
spaceId|是|String|空间ID
pubAccId|是|String|服务号ID
sendScope|是|String|发送范围，list:发送给to参数数组中成员
to|是|Array|发送对象
title|是|String|消息标题
desc|是|String|消息内容
detailUrl|是|String|消息链接地址

* 注:Summer应用链接地址为：https://pubaccount.yonyoucloud.com/summer?appid={APPID}&page={PAGE}&param1=val1&param2=val2&...&paramN=valN ，链接地址路径写死，appid为summer应用ID，page为页面，如: index.html，后面拼装自己需要的参数。
* 注:RN应用链接地址为：https://pubaccount.yonyoucloud.com/rn?param1=val1&param2=val2&...&paramN=valN ，链接地址路径写死，后面拼装自己需要的参数。
* 普通h5应用链接地址直接使用应用自己的地址，支持\${esncode}变量，用户点击消息打开应用时，会将\${esncode}变量替换成真实的友空间单点code。用法：https://ec.yonyoucloud.com?code=${esncode}

4、返回值

```
{
	"flag": 0,
	"message":""
}
```

5、失败

HTTP StatusCode|原因
---|---
400|参数不合法
401|认证信息错误
405|Method Not Allowed，HTTP请求方法错误
500|内部服务器错误

##2.3 发送图文/多图文消息到服务号

1、地址

https://openapi.yonyoucloud.com/rest/message/service/mixed?access_token=ACCESS_TOKEN

2、方法

POST

3、参数（RequestBody）

```
{
	"spaceId":"空间ID",
	"pubAccId": "服务号ID",
	"sendScope":"list",
	"to": ["MEMBER_ID1", "MEMBER_ID2"],
	"articles": [{
		"title":"消息标题1",
		"thumbId":"消息封面图1",
		"digest":"消息摘要1",
		"contentSourceUrl":"消息链接地址1"
	},
	{...},
	{
		"title":"消息标题N",
		"thumbId":"消息封面图N",
		"digest":"消息摘要N",
		"contentSourceUrl":"消息链接地址N"
	}]
}
```

字段|必须|类型|说明
---|---|---|---
access_token|是|String|开放平台access_token，参见#1
spaceId|是|String|空间ID
pubAccId|是|String|服务号ID
sendScope|是|String|发送范围，list:发送给to参数数组中成员
to|是|Array|发送对象
articles.title|是|String|消息标题
articles.thumbId|是|String|消息封面图
articles.digest|是|String|消息摘要
articles.contentSourceUrl|是|String|消息链接地址

* 注articles为数组，当内容只有一个时为单图文消息，多个时为多图文消息，不建议超过5个

4、返回值

```
{
	"flag": 0,
	"message":""
}
```

5、失败

HTTP StatusCode|原因
---|---
400|参数不合法
401|认证信息错误
405|Method Not Allowed，HTTP请求方法错误
500|内部服务器错误

#3 发送消息到应用号/应用通知
##3.1 发送文本消息到应用号/应用通知

1、地址

https://openapi.yonyoucloud.com/rest/message/app/txt?access_token=ACCESS_TOKEN

2、方法

POST

3、参数（RequestBody）

```
{
	"spaceId":"空间ID",
	"appId":"应用ID",
	"sendThrough":"appAccount",
	"sendScope":"list",
	"to":[
		"MEMBER_ID1",
		"MEMBER_ID2"
	],
	"content":"文本消息内容"
}
```

字段|必须|类型|说明
---|---|---|---
access_token|是|String|开放平台access_token，参见#1
spaceId|是|String|空间ID
appId|是|String|应用ID
sendThrough|是|String|发送途径，appAccount:通过应用号发送，appNotify:通过应用通知发送
sendScope|是|String|发送范围，list:发送给to参数数组中成员
to|是|Array|发送对象
content|是|String|文本消息内容

4、返回值

```
{
	"flag": 0,
	"message":""
}
```

5、失败

HTTP StatusCode|原因
---|---
400|参数不合法
401|认证信息错误
405|Method Not Allowed，HTTP请求方法错误
500|内部服务器错误

##3.2 发送分享消息（业务消息）到应用号/应用通知

1、地址

https://openapi.yonyoucloud.com/rest/message/app/share?access_token=ACCESS_TOKEN

2、方法

POST

3、参数（RequestBody）

```
{
	"spaceId":"空间ID",
	"appId":"应用ID",
	"sendThrough":"appAccount",
	"sendScope":"list",
	"to":[
		"MEMBER_ID1",
		"MEMBER_ID2"
	],
	"title":"消息标题",
	"desc":"消息描述",
	"detailUrl":"http://ec.yonyoucloud.com/detail"
}
```

字段|必须|类型|说明
---|---|---|---
access_token|是|String|开放平台access_token，参见#1
spaceId|是|String|空间ID
appId|是|String|应用ID
sendThrough|是|String|发送途径，appAccount:通过应用号发送，appNotify:通过应用通知发送
sendScope|是|String|发送范围，list:发送给to参数数组中成员
to|是|Array|发送对象
title|是|String|消息标题
desc|是|String|消息内容
detailUrl|是|String|消息链接地址

* 注:Summer应用链接地址为：https://pubaccount.yonyoucloud.com/summer?appid={APPID}&page={PAGE}&param1=val1&param2=val2&...&paramN=valN ，链接地址路径写死，appid为summer应用ID，page为页面，如: index.html，后面拼装自己需要的参数。
* 注:RN应用链接地址为：https://pubaccount.yonyoucloud.com/rn?param1=val1&param2=val2&...&paramN=valN ，链接地址路径写死，后面拼装自己需要的参数。
* 普通h5应用链接地址直接使用应用自己的地址，支持\${esncode}变量，用户点击消息打开应用时，会将\${esncode}变量替换成真实的友空间单点code。用法：https://ec.yonyoucloud.com?code=${esncode}
  
4、返回值

```
{
	"flag": 0,
	"message":""
}
```

5、失败

HTTP StatusCode|原因
---|---
400|参数不合法
401|认证信息错误
405|Method Not Allowed，HTTP请求方法错误
500|内部服务器错误

##3.3 发送图文/多图文消息到应用号

1、地址

https://openapi.yonyoucloud.com/rest/message/app/mixed?access_token=ACCESS_TOKEN

2、方法

POST

3、参数（RequestBody）

```
{
	"spaceId":"空间ID",
	"appId": "应用ID",
	"sendScope":"list",
	"to": ["MEMBER_ID1", "MEMBER_ID2"],
	"articles": [{
		"title":"消息标题1",
		"thumbId":"消息封面图1",
		"digest":"消息摘要1",
		"contentSourceUrl":"消息链接地址1"
	},
	{...},
	{
		"title":"消息标题N",
		"thumbId":"消息封面图N",
		"digest":"消息摘要N",
		"contentSourceUrl":"消息链接地址N"
	}]
}
```

字段|必须|类型|说明
---|---|---|---
access_token|是|String|开放平台access_token，参见#1
spaceId|是|String|空间ID
appId|是|String|应用ID
sendScope|是|String|发送范围，list:发送给to参数数组中成员
to|是|Array|发送对象
articles.title|是|String|消息标题
articles.thumbId|是|String|消息封面图
articles.digest|是|String|消息摘要
articles.contentSourceUrl|是|String|消息链接地址

* 注articles为数组，当内容只有一个时为单图文消息，多个时为多图文消息，不建议超过5个

4、返回值

```
{
	"flag": 0,
	"message":""
}
```

5、失败

HTTP StatusCode|原因
---|---
400|参数不合法
401|认证信息错误
405|Method Not Allowed，HTTP请求方法错误
500|内部服务器错误


