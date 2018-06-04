package yonyou.esn.openapi.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yonyou.esn.openapi.bo.OpenApiPush;
import yonyou.esn.openapi.bo.PermanentCodeBo;
import yonyou.esn.openapi.common.CodeEnum;
import yonyou.esn.openapi.configrations.SuiteConfig;
import yonyou.esn.openapi.exception.BizException;
import yonyou.esn.openapi.uitils.HttpReq;
import yonyou.esn.openapi.uitils.MapUtil;
import yonyou.esn.openapi.uitils.WXBizMsgCrypt;
import java.util.Map;

@Service
public class AuthAppSuiteService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthAppSuiteService.class);
    // 模拟缓冲存储suitetTicket
    private  static String suitetTicket=null;

    @Value("${auth_base_path}")
    private String authBasePath;
    @Autowired
    private SuiteConfig suiteConfig;
    @Autowired
    private PermanentCodeService permanentCodeService;
    // 保存获取的永久code
    public void savePermanentCode(PermanentCodeBo permanentCodeBo) {
        // 本系统仅为演示，持久化层实现
        permanentCodeService.savePermanentCode(permanentCodeBo);

    }

    // 获取本地存储的永久code
    public String getNativePermanentCode(String qzId) {
        // 持久化操作，建议存入redis一份
        PermanentCodeBo permanentCodeBo = permanentCodeService.getPermanentCode(Integer.parseInt(qzId));
        return permanentCodeBo.getPermanentCode();
    }

    public void saveSuiteTicket(String ticket) {
        // 本系统仅为演示，不做持久化层实现
        suitetTicket=ticket;
    }

    public String getSuiteTicket() {
        // 要从存储的ticket中取出,建议使用缓存
        return suitetTicket;
    }

    public Map<String, String> decodeData(OpenApiPush openApiPush) {
        WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(suiteConfig.token, suiteConfig.EncodingAESKey, suiteConfig.suiteKey);
        String xmlString = msgCrypt.DecryptMsg(openApiPush.getMsgSignature(), openApiPush.getTimestamp(), openApiPush.getNonce(), openApiPush.getEncrypt());
        LOG.info(xmlString);
        return MapUtil.xmlToMap(xmlString);
    }

    public String getSuiteAccessToken(String suiteTicket) {
        String url = authBasePath + "/get_suite_token";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("suiteKey", suiteConfig.suiteKey);
        jsonObject.put("suiteSecret", suiteConfig.suiteSecret);
        jsonObject.put("suiteTicket", suiteTicket);
        String back = HttpReq.postBody(url, jsonObject.toJSONString());
        JSONObject jsonObjec1t = JSONObject.parseObject(back);
        String data = jsonObjec1t.getString("data");
        String suite_token = "";
        if (data == null) {
            LOG.error("获得access_token出错,请求参数suite_id=" + suiteConfig.suiteKey + ",suite_secret=" + suiteConfig.suiteSecret + ",suite_ticket=" + suiteTicket + ";返回结果=" + jsonObjec1t);
            return null;
        } else {
            suite_token = JSONObject.parseObject(data).getString("suite_access_token");
        }
        return suite_token;
    }

    /**
     * 获取PermanentCode， 只能在push消息的时候使用一次
     *
     * @param suite_access_token
     * @param tempCode
     * @return
     */
    public String getPermanentCode(String suite_access_token, String tempCode) {
        String url = authBasePath + "/auth/get_permanent_code?suite_token=" + suite_access_token;
        JSONObject jsObj = new JSONObject();
        jsObj.put("suiteKey", suiteConfig.suiteKey);
        jsObj.put("tmpAuthCode", tempCode);
        String backData = HttpReq.postBody(url, jsObj.toJSONString());
        JSONObject backDataObj = JSONObject.parseObject(backData);
        String data = backDataObj.getString("data");
        String permenentCode = "";
        if (data == null) {
            LOG.error("获得永久授权码失败,请求参数suite_id=" + suiteConfig.suiteKey + ",auth_code=" + tempCode + ",token=" + suite_access_token + ";返回结果=" + backDataObj);
            throw new BizException(CodeEnum.C_90004);
        } else {
            permenentCode = JSONObject.parseObject(data).getString("permanentCode");
            String qzId = JSONObject.parseObject(data).getString("qzId");
            String qzName = JSONObject.parseObject(data).getString("qzName");
            LOG.info("空间名称=" + qzName + ",空间Id=" + qzId + "的永久授权码=" + permenentCode);
        }
        return permenentCode;
    }

    public String getAccessToken(String suiteAccessToken, String pernanentCode) {
        String url = authBasePath + "/get_corp_token?suite_token=" + suiteAccessToken;
        JSONObject jsObj = new JSONObject();
        jsObj.put("suite_id", suiteConfig.suiteKey);
        jsObj.put("permanent_code", pernanentCode);
        String backData = HttpReq.postBody(url, jsObj.toJSONString());
        JSONObject backDataObj = JSONObject.parseObject(backData);
        String data = backDataObj.getString("data");
        String accessToken = "";
        if (data == null) {
            LOG.error("获得空间访问令牌失败,请求参数suite_id=" + suiteConfig.suiteKey + ",permanent_code=" + pernanentCode + ";返回结果=" + backDataObj);
            return null;
        } else {
            accessToken = JSONObject.parseObject(data).getString("access_token");
            Long expiresIn = JSONObject.parseObject(data).getLongValue("expires_in");
            LOG.info("已获得空间访问令牌=" + accessToken + ",有效时间是" + (expiresIn / 60 / 60) + "小时");
            return accessToken;
        }
    }
}
