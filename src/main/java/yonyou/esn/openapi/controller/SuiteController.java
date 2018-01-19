package yonyou.esn.openapi.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yonyou.esn.openapi.Service.AuthAppSuiteService;
import yonyou.esn.openapi.bo.OpenApiPush;
import yonyou.esn.openapi.bo.PermanentCodeBo;
import yonyou.esn.openapi.configrations.SuiteConfig;
import yonyou.esn.openapi.enums.PushType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static yonyou.esn.openapi.common.ConstantValue.*;

/**
 * isv套件接入demo的controller，主要演示：
 * 1.接受推送消息获取ticket和临时code处理过程
 * 2.获取suite_token和access_token的过程
 * Created by mantantan on 2018/1/19.
 */
@RestController("/suite")
public class SuiteController {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SuiteController.class);

    @Autowired
    private AuthAppSuiteService authAppSuiteService;
    @Autowired
    private SuiteConfig suiteConfig;

    @PostMapping("/open_api_push")
    public String message_push(HttpServletRequest httpServletRequest,
                               @RequestParam("msg_signature") String msgSignature,
                               @RequestParam("timestamp") String timestamp,
                               @RequestParam("nonce") String nonce,
                               @RequestParam("encrypt") String encrypt) {
        OpenApiPush openApiPush = new OpenApiPush(msgSignature, timestamp, nonce, encrypt);
        Map<String, String> dataMap = authAppSuiteService.decodeData(openApiPush);
        String ticketType = dataMap.get(KEY_INFO_TYPE);

        // 推送的是ticket
        if(PushType.TICKET.getMessage().equals(ticketType)){
            String suiteTicket = dataMap.get(KEY_SUITE_TICKET);
            // 建议票据存入在缓存中,方便需要时直接获取
            authAppSuiteService.saveSuiteTicket(suiteTicket);
        }else{ // 推送的是临时码
            String tempCode = dataMap.get(KEY_AUTH_CODE);
            String qzId = dataMap.get(KEY_AUTH_CROP_ID);
            String suiteAccessToken = authAppSuiteService.getSuiteAccessToken(authAppSuiteService.getSuiteTicket());
            String permanentCode = authAppSuiteService.getPermanentCode(suiteAccessToken, tempCode);
            PermanentCodeBo permanentCodeBo = new PermanentCodeBo(qzId, suiteConfig.suiteKey, permanentCode);
            authAppSuiteService.savePermanentCode(permanentCodeBo);
        }
        return "success";
    }

    /**
     * 获取qz的访问token，建议使用缓存失效策略，减少开放平台压力和isv压力
     * @param qzId
     * @return
     */
    @GetMapping("/get_qz_access_token")
    public String getQzAccessToken( @RequestParam("qz_id") String qzId){
        String suiteAccessToken = authAppSuiteService.getSuiteAccessToken(authAppSuiteService.getSuiteTicket());
        String permanentCode = authAppSuiteService.getNativePermanentCode(qzId);
        return authAppSuiteService.getAccessToken(suiteAccessToken,  permanentCode);
    }
}
