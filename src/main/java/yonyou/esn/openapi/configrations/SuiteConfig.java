package yonyou.esn.openapi.configrations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by mantantan on 2018/1/19.
 */
@Component
@ConfigurationProperties(prefix = "suite_config")
public class SuiteConfig {

    public String token;
    public String suiteKey;
    public String suiteSecret;
    public String EncodingAESKey;

    public void setToken(String token) {
        this.token = token;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        EncodingAESKey = encodingAESKey;
    }

    public void setSuiteSecret(String suiteSecret) {
        this.suiteSecret = suiteSecret;
    }
}
