package com.yonyoucloud.ec.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyoucloud.ec.util.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ISV应用消息接入代码示例
 *
 * @author litfb
 * @version 1.3
 * @apiNote 此接口示例仅限于通过友空间开放平台使用ISV方式接入了应用，并想要推送消息到友空间的开发者，如不符合请移步其他文档！！！
 */
public class ISVMessageDemo {

    private static final Logger logger = LoggerFactory.getLogger(ISVMessageDemo.class);

    // #2 发送消息到服务号
    /**
     * ##2.1 发送文本消息到服务号
     */
    private static final String URL_SERVICE_TEXT = "https://openapi.yonyoucloud.com/rest/message/service/txt";
    /**
     * ##2.2 发送分享消息（业务消息）到服务号
     */
    private static final String URL_SERVICE_SHARE = "https://openapi.yonyoucloud.com/rest/message/service/share";
    /**
     * ##2.3 发送图文/多图文消息到服务号
     */
    private static final String URL_SERVICE_MIXED = "https://openapi.yonyoucloud.com/rest/message/service/mixed";

    // #3 发送消息到应用号/应用通知
    /**
     * ##3.1 发送文本消息到应用号/应用通知
     */
    private static final String URL_APP_TEXT = "https://openapi.yonyoucloud.com/rest/message/app/txt";
    /**
     * ##3.2 发送分享消息（业务消息）到应用号/应用通知
     */
    private static final String URL_APP_SHARE = "https://openapi.yonyoucloud.com/rest/message/app/share";
    /**
     * ##3.3 发送图文/多图文消息到应用号
     */
    private static final String URL_APP_MIXED = "https://openapi.yonyoucloud.com/rest/message/app/mixed";

    /**
     * TODO 你的空间ID
     */
    private static final String SPACE_ID = "your SPACE_ID";
    /**
     * TODO 你的服务号ID
     */
    private static final String PUBACC_ID = "your PUBACC_ID";
    /**
     * TODO 你的应用号绑定的应用ID
     */
    private static final String APPACC_ID = "your APPACC_ID";

    /**
     * TODO 友空间用户ID数组（消息要发给谁）
     */
    private static final String[] MEMBER_IDS = new String[]{"your MEMBER_ID"};

    /**
     * 示例各个接口调用
     *
     * @param args
     */
    public static void main(String[] args) {
        // 测试用文本消息内容
        String content = "测试文本消息内容";
        // 测试用分享消息内容
        String title = "测试分享消息标题";
        String desc = "测试分享消息内容";
        String detailUrl = "https://ec.yonyoucloud.com";
        // 测试用图文消息内容
        JSONArray articles = new JSONArray();
        for (int i = 0; i < 3; i++) {
            JSONObject object = new JSONObject();
            object.put("title", "图文消息标题" + i);
            object.put("thumbId", "https://imoss.yonyoucloud" +
                    ".com/upesn/esn/155359/20171127/1856/a3e9c85d-ef4e-415a-baee-cf5292c12922.jpg.thumb.jpg");
            object.put("digest", "摘要" + i);
            object.put("contentSourceUrl", "https://www.baidu.com/s?wd=" + i);
            articles.add(object);
        }

        // #1 获取AccessToken
        String accessToken = null;
        // ISV获取AccessToken参见https://open.esn.ren/isv-auth-demo/entrance/index
        boolean result;
        // #2 发送消息到服务号
        // ##2.1 发送文本消息到服务号
        result = sendServiceTxt(accessToken, SPACE_ID, PUBACC_ID, "list", MEMBER_IDS, content);
        logger.info("发送文本消息到服务号:" + (result ? "成功" : "失败"));
        // ##2.2 发送分享消息（业务消息）到服务号
        result = sendServiceShare(accessToken, SPACE_ID, PUBACC_ID, "list", MEMBER_IDS, title, desc,
                detailUrl);
        logger.info("发送分享消息（业务消息）到服务号:" + (result ? "成功" : "失败"));
        // ##2.3 发送图文/多图文消息到服务号
        result = sendServiceMixed(accessToken, SPACE_ID, PUBACC_ID, "list", MEMBER_IDS, articles);
        logger.info("发送图文/多图文消息到服务号:" + (result ? "成功" : "失败"));

        // #3 发送消息到应用号/应用通知
        // 发送途径-通过应用号发送
        final String sendThroughAppAccount = "appAccount";
        // 发送途径-通过应用通知发送，文本/分享消息都可以发到应用通知，但是图文消息不可以
        final String sendThroughAppNotify = "appNotify";
        // ##3.1 发送文本消息到应用号/应用通知
        result = sendAppTxt(accessToken, SPACE_ID, APPACC_ID, sendThroughAppAccount, "list", MEMBER_IDS, content);
        logger.info("发送文本消息到应用号:" + (result ? "成功" : "失败"));
        result = sendAppTxt(accessToken, SPACE_ID, APPACC_ID, sendThroughAppNotify, "list", MEMBER_IDS, content);
        logger.info("发送文本消息到应用通知:" + (result ? "成功" : "失败"));
        // ##3.2 发送分享消息（业务消息）到应用号/应用通知
        result = sendAppShare(accessToken, SPACE_ID, APPACC_ID, sendThroughAppAccount, "list", MEMBER_IDS,
                title, desc, detailUrl);
        logger.info("发送分享消息（业务消息）到应用号:" + (result ? "成功" : "失败"));
        result = sendAppShare(accessToken, SPACE_ID, APPACC_ID, sendThroughAppNotify, "list", MEMBER_IDS,
                title, desc, detailUrl);
        logger.info("发送分享消息（业务消息）到应用通知:" + (result ? "成功" : "失败"));
        // ##3.3 发送图文/多图文消息到应用号
        result = sendAppMixed(accessToken, SPACE_ID, APPACC_ID, "list", MEMBER_IDS, articles);
        logger.info("应用号发送图文消息:" + (result ? "成功" : "失败"));
    }

    /**
     * ##2.1 发送文本消息到服务号
     *
     * @param accessToken access_token
     * @param spaceId     空间ID
     * @param pubAccId    公共号ID
     * @param sendScope   发送范围
     * @param toArray     接收人ID数组
     * @param content     消息内容
     * @return 是否成功
     */
    private static boolean sendServiceTxt(String accessToken, String spaceId, String pubAccId, String sendScope,
                                          String[] toArray, String content) {
        // url string
        String urlString = URL_SERVICE_TEXT + "?access_token=" + accessToken;

        // request body
        JSONObject body = new JSONObject();
        // 空间ID
        body.put("spaceId", spaceId);
        // 服务号ID
        body.put("pubAccId", pubAccId);
        // 发送范围
        if ("all".equals(sendScope)) {
            body.put("sendScope", "all");
        } else {
            body.put("sendScope", "list");
            // 接收人
            body.put("to", toArray);
        }
        // 消息内容
        body.put("content", content);

        logger.info("sendServiceTxt:" + urlString + ":" + body.toJSONString());
        // request result
        String result = HttpClientUtils.doPostJson(urlString, body.toJSONString());
        return checkResult(result, urlString);
    }

    /**
     * ##2.2 发送分享消息（业务消息）到服务号
     *
     * @param accessToken access_token
     * @param spaceId     空间ID
     * @param pubAccId    公共号ID
     * @param sendScope   发送范围
     * @param toArray     接收方ID数组
     * @param title       标题
     * @param desc        描述
     * @param detailUrl   链接
     * @return 是否成功
     */
    private static boolean sendServiceShare(String accessToken, String spaceId, String pubAccId, String sendScope,
                                            String[] toArray, String title, String desc, String detailUrl) {
        // url string
        String urlString = URL_SERVICE_SHARE + "?access_token=" + accessToken;

        // request body
        JSONObject body = new JSONObject();
        // 空间ID
        body.put("spaceId", spaceId);
        // 服务号ID
        body.put("pubAccId", pubAccId);
        // 发送范围
        if ("all".equals(sendScope)) {
            body.put("sendScope", "all");
        } else {
            body.put("sendScope", "list");
            // 接收人
            body.put("to", toArray);
        }
        // 消息内容
        body.put("title", title);
        body.put("desc", desc);
        body.put("detailUrl", detailUrl);

        logger.info("sendServiceShare:" + urlString + ":" + body.toJSONString());
        // request result
        String result = HttpClientUtils.doPostJson(urlString, body.toJSONString());
        return checkResult(result, urlString);
    }

    /**
     * ##2.3 发送图文/多图文消息到服务号
     *
     * @param accessToken access_token
     * @param spaceId     空间ID
     * @param pubAccId    公共号ID
     * @param sendScope   发送范围
     * @param toArray     接收方ID数组
     * @param articles    图文内容数据
     * @return 是否成功
     */
    private static boolean sendServiceMixed(String accessToken, String spaceId, String pubAccId, String sendScope,
                                            String[] toArray, JSONArray articles) {
        // url string
        String urlString = URL_SERVICE_MIXED + "?access_token=" + accessToken;

        // request body
        JSONObject body = new JSONObject();
        // 空间ID
        body.put("spaceId", spaceId);
        // 服务号ID
        body.put("pubAccId", pubAccId);
        // 发送范围
        if ("all".equals(sendScope)) {
            body.put("sendScope", "all");
        } else {
            body.put("sendScope", "list");
            // 接收人
            body.put("to", toArray);
        }
        // 消息内容
        body.put("articles", articles);

        logger.info("sendServiceMixed:" + urlString + ":" + body.toJSONString());
        // request result
        String result = HttpClientUtils.doPostJson(urlString, body.toJSONString());
        return checkResult(result, urlString);
    }

    /**
     * ##3.1 发送文本消息到应用号/应用通知
     *
     * @param accessToken access_token
     * @param spaceId     空间ID
     * @param appId       应用ID
     * @param sendThrough 发送途径
     * @param sendScope   发送范围
     * @param toArray     接收方ID数组
     * @param content     文本内容
     * @return 是否成功
     */
    private static boolean sendAppTxt(String accessToken, String spaceId, String appId, String sendThrough, String
            sendScope, String[]
                                              toArray, String content) {
        // url string
        String urlString = URL_APP_TEXT + "?access_token=" + accessToken;

        // request body
        JSONObject body = new JSONObject();
        // 空间ID
        body.put("spaceId", spaceId);
        // 应用ID
        body.put("appId", appId);
        // 发送途径
        body.put("sendThrough", sendThrough);
        // 发送范围
        if ("all".equals(sendScope)) {
            body.put("sendScope", "all");
        } else {
            body.put("sendScope", "list");
            // 接收人
            body.put("to", toArray);
        }
        // 消息内容
        body.put("content", content);

        logger.info("sendAppTxt:" + urlString + ":" + body.toJSONString());
        // request result
        String result = HttpClientUtils.doPostJson(urlString, body.toJSONString());
        return checkResult(result, urlString);
    }

    /**
     * ##3.2 发送分享消息（业务消息）到应用号/应用通知
     *
     * @param accessToken access_token
     * @param spaceId     空间ID
     * @param appId       应用ID
     * @param sendThrough 发送途径
     * @param sendScope   发送范围
     * @param toArray     接收方ID数组
     * @param title       标题
     * @param desc        描述
     * @param detailUrl   链接
     * @return 是否成功
     */
    private static boolean sendAppShare(String accessToken, String spaceId, String appId, String sendThrough, String
            sendScope, String[] toArray, String title, String desc, String detailUrl) {
        // url string
        String urlString = URL_APP_SHARE + "?access_token=" + accessToken;

        // request body
        JSONObject body = new JSONObject();
        // 空间ID
        body.put("spaceId", spaceId);
        // 应用ID
        body.put("appId", appId);
        // 发送途径
        body.put("sendThrough", sendThrough);
        // 发送范围
        if ("all".equals(sendScope)) {
            body.put("sendScope", "all");
        } else {
            body.put("sendScope", "list");
            // 接收人
            body.put("to", toArray);
        }
        // 消息内容
        body.put("title", title);
        body.put("desc", desc);
        body.put("detailUrl", detailUrl);

        logger.info("sendAppShare:" + urlString + ":" + body.toJSONString());
        // request result
        String result = HttpClientUtils.doPostJson(urlString, body.toJSONString());
        return checkResult(result, urlString);
    }

    /**
     * ##3.3 发送图文/多图文消息到应用号
     *
     * @param accessToken access_token
     * @param spaceId     空间ID
     * @param appId       应用ID
     * @param sendScope   发送范围
     * @param toArray     接收方ID数组
     * @param articles    图文内容数据
     * @return 是否成功
     */
    private static boolean sendAppMixed(String accessToken, String spaceId, String appId, String sendScope, String[]
            toArray, JSONArray articles) {
        // url string
        String urlString = URL_APP_MIXED + "?access_token=" + accessToken;

        // request body
        JSONObject body = new JSONObject();
        // 空间ID
        body.put("spaceId", spaceId);
        // 应用ID
        body.put("appId", appId);
        // 发送范围
        if ("all".equals(sendScope)) {
            body.put("sendScope", "all");
        } else {
            body.put("sendScope", "list");
            // 接收人
            body.put("to", toArray);
        }
        // 消息内容
        body.put("articles", articles);

        logger.info("sendAppMixed:" + urlString + ":" + body.toJSONString());
        // request result
        String result = HttpClientUtils.doPostJson(urlString, body.toJSONString());
        return checkResult(result, urlString);
    }

    /**
     * 输出请求结果
     *
     * @param result
     * @param identify
     * @return
     */
    private static boolean checkResult(String result, String identify) {
        logger.info("checkResult:" + identify + ":" + result);
        if (result != null && !"".equals(result)) {
            // JSONObject
            JSONObject responseData = JSON.parseObject(result);
            if (responseData != null) {
                Integer flag = responseData.getInteger("flag");
                logger.info("response flag: " + flag);
                logger.info("response message: " + responseData.getString("message"));
                if (flag == 0) {
                    logger.info("do " + identify + " success");
                    return true;
                }
            }
        }
        logger.info("do " + identify + " faild");
        return false;
    }

}
