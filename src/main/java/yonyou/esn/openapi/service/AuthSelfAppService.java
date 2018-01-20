package yonyou.esn.openapi.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yonyou.esn.openapi.configrations.SelfAppConfig;
import yonyou.esn.openapi.uitils.HttpReq;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthSelfAppService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthSelfAppService.class);

    @Value("${auth_base_path}")
    private String authBasePath;

    @Autowired
    private SelfAppConfig selfAppConfig;

    /**
     * 获取自建应用空间访问令牌
     * @return
     */
    public String getAccessToken() {
        String url = authBasePath + "/token";
        Map<String, String> map = new HashMap<String, String>();
        map.put("appid",selfAppConfig.appId);
        map.put("secret",selfAppConfig.secret);
        String backData = null;
        String accessToken = "";
        try {
            backData = HttpReq.sendGet(url, map);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        JSONObject backDataObj = JSONObject.parseObject(backData);
        String data = backDataObj.getString("data");
        if (data == null) {
            LOG.error("获得空间访问令牌失败,请求参数appid=" + selfAppConfig.appId + ",secret=" + selfAppConfig.secret+";返回结果=" + data);
            return null;
        } else {
            accessToken = JSONObject.parseObject(data).getString("access_token");
            Integer expiresIn = JSONObject.parseObject(data).getInteger("expiresIn");
            LOG.info("已获得空间访问令牌=" + accessToken + ",有效时间是" + (expiresIn / 60 / 60) + "小时");
            return accessToken;
        }
    }

}
