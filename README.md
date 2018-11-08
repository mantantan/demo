## 开放平台接入demo
#### 友空间官网  

```
https://ec.yonyoucloud.com/
```

#### 开放平台文档中心
详细接入请参考开放平台文档中心
```
// 文档中心地址
https://open.yonyoucloud.com/developer/doc?id=263e0eddcf100759eaeaa61827ba02cf
// 在线接口调试地址
http://open.yonyoucloud.com/developer/debugger/demo.html
```

#### demo 接口：
- 企业自建应用获取token demo接口：yonyou.esn.openapi.controller.SelfAppController#getAccessToken
- 友空间推送数据到isv套件demo接口：yonyou.esn.openapi.controller.SuiteController#message_push
- isv应用获取空间token的demo接口:yonyou.esn.openapi.controller.SuiteController#getQzAccessToken

#### demo接入说明
- 企业自建直接从进入空间后台，获取到appid和secret填入properties文件，调用企业自建应用获取token接口拿token即可，具体可参见开放平台文档中心
- isv demo接入请参考[isv接入流程](isv_doc/README.md)
