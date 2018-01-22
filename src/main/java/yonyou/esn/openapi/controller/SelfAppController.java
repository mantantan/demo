package yonyou.esn.openapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yonyou.esn.openapi.service.AuthSelfAppService;

/**
 * 企业自建应用接入demo的controller，主要演示：
 * 获取access_token的过程
 * Created by zhaohlp on 2018/1/19.
 */

@Controller
@RequestMapping("/self_app")
public class SelfAppController {

    @Autowired
    private AuthSelfAppService authSelfAppService;

    /**
     * 获取空间的接口访问令牌(access_token),即第三方开发者拿此token作为参数访问空间提供的接口
     * 建议使用缓冲存储令牌
     * @return
     */
    @GetMapping("/get_access_token")
    @ResponseBody
    public String getAccessToken(){
        return authSelfAppService.getAccessToken();
    }
}
