package yonyou.esn.openapi.configrations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by zhaohlp on 2018/1/19.
 */
@Component
@ConfigurationProperties(prefix = "self_app_config")
public class SelfAppConfig {

    public String appId;
    public String secret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
