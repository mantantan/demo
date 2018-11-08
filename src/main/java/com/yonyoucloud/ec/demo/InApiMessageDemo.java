package com.yonyoucloud.ec.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonyoucloud.ec.util.HttpClientUtils;
import com.yonyoucloud.ec.util.SecurityUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 自研应用消息接入代码示例
 *
 * @author litfb
 * @version 1.3
 * @apiNote 此示例的友空间InApi接口，仅允许ali云10网段内网调用！！！
 */
public class InApiMessageDemo {

    private static final Logger logger = LoggerFactory.getLogger(InApiMessageDemo.class);

    // TODO InApi环境变量
    /**
     * 接口地址
     * 测试环境:http://in-api.chaoke.com:6062
     * 正式环境:https://in-api.yonyoucloud.com
     * 授权参数颁发，请联系袁婧经理<a>mailto:yuanjinga@yonyou.com</a>
     */
    private static final String INAPI_DOMAIN = "";
    // TOKEN
    private static final String INAPI_TOKEN = "";
    // SALT
    private static final String INAPI_SALT = "";
    // AGENT_ID
    private static final String INAPI_AGENT_ID = "";
    // V
    private static final String INAPI_VERSION = "1.0";

    /**
     * 如果你的服务对接了友户通!!!
     * 你一定有友户通的租户ID和友户通的用户ID
     */
    // TODO 友户通租户ID
    private static final String TENENT_ID = "your TENENT_ID";
    // TODO 友户通用户ID
    private static final String[] USER_IDS = new String[]{"your USER_ID"};
    /**
     * 如果你没有接友户通，那必须有友空间的空间ID和友空间的用户ID
     */
    // TODO 空间ID
    private static final String SPACE_ID = "your SPACE_ID";
    // TODO 友空间用户ID
    private static final String[] MEMBER_IDS = new String[]{"your MEMBER_ID"};

    /**
     * TODO 你的服务号ID
     */
    private static final String PUBACC_ID = "your PUBACC_ID";
    /**
     * TODO 你的应用号绑定的应用ID
     */
    private static final String APPACC_ID = "your APPACC_ID";

    /**
     * 友空间 & 友户通 信息对照接口
     */
    // 根据友户通租户ID获得空间ID接口
    private static final String URI_GET_QZ_ID = "/yonCloudInfo/getQzIdByTenantId";
    // 根据友户通用户ID获得空间用户ID接口
    private static final String URI_GET_MEMBER_ID = "/yonCloudInfo/getUspaceMemberIdsByYhtUserIds";
    // 根据空间用户信息获得友户通用户信息接口
    private static final String URI_GET_YHT_INFO = "/yonCloudInfo/getTenantIdAndYhtUserIdsByQzIdAndUspaceMemberIds";

    /**
     * 消息相关接口
     */
    // 发送文本消息到服务号
    private static final String URI_MESSAGE_SERVICE_TXT = "/message/txt";
    // 发送分享消息（业务消息）到服务号
    private static final String URI_MESSAGE_SERVICE_SHARE = "/message/share";
    // 发送图文/多图文消息到服务号
    private static final String URI_MESSAGE_SERVICE_MIXED = "/message/mixed";
    // 发送文本消息到应用号/应用通知
    private static final String URI_MESSAGE_APP_TXT = "/message/appTxt";
    // 发送分享消息（业务消息）到应用号/应用通知
    private static final String URI_MESSAGE_APP_SHARE = "/message/appShare";
    // 发送图文/多图文消息到应用号
    private static final String URI_MESSAGE_APP_MIXED = "/message/appMixed";

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

        // 根据友户通租户ID取空间ID
        String spaceId = getQzIdByTenantId(TENENT_ID);
        // 根据空间ID和友户通用户ID获取空间用户ID
        String[] memberIds = getUspaceMemberIdsByYhtUserIds(spaceId, USER_IDS);

        boolean result;
        // 发送消息到服务号
        // 发送文本消息到服务号
        result = sendServiceTxt(spaceId, PUBACC_ID, "list", memberIds, content);
        logger.info("发送文本消息到服务号:" + (result ? "成功" : "失败"));
        // 发送分享消息（业务消息）到服务号
        result = sendServiceShare(spaceId, PUBACC_ID, "list", memberIds, title, desc,
                detailUrl);
        logger.info("发送分享消息（业务消息）到服务号:" + (result ? "成功" : "失败"));
        // 发送图文/多图文消息到服务号
        result = sendServiceMixed(spaceId, PUBACC_ID, "list", memberIds, articles);
        logger.info("发送图文/多图文消息到服务号:" + (result ? "成功" : "失败"));

        // 发送消息到应用号/应用通知
        // 发送途径-通过应用号发送
        final String sendThroughAppAccount = "appAccount";
        // 发送途径-通过应用通知发送，文本/分享消息都可以发到应用通知，但是图文消息不可以
        final String sendThroughAppNotify = "appNotify";
        // 发送文本消息到应用号/应用通知
        result = sendAppTxt(spaceId, APPACC_ID, sendThroughAppAccount, "list", memberIds, content);
        logger.info("发送文本消息到应用号:" + (result ? "成功" : "失败"));
        result = sendAppTxt(spaceId, APPACC_ID, sendThroughAppNotify, "list", memberIds, content);
        logger.info("发送文本消息到应用通知:" + (result ? "成功" : "失败"));
        // 发送分享消息（业务消息）到应用号/应用通知
        result = sendAppShare(spaceId, APPACC_ID, sendThroughAppAccount, "list", memberIds,
                title, desc, detailUrl);
        logger.info("发送分享消息（业务消息）到应用号:" + (result ? "成功" : "失败"));
        result = sendAppShare(spaceId, APPACC_ID, sendThroughAppNotify, "list", memberIds,
                title, desc, detailUrl);
        logger.info("发送分享消息（业务消息）到应用通知:" + (result ? "成功" : "失败"));
        // 发送图文/多图文消息到应用号
        result = sendAppMixed(spaceId, APPACC_ID, "list", memberIds, articles);
        logger.info("应用号发送图文消息:" + (result ? "成功" : "失败"));
    }

    /**
     * 根据友户通租户ID取空间ID
     *
     * @param tenantId 友户通租户ID
     * @return
     */
    private static String getQzIdByTenantId(String tenantId) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("tenantId", tenantId);
        params = genParams(URI_GET_QZ_ID, params);
        String url = INAPI_DOMAIN + URI_GET_QZ_ID;
        String result = HttpClientUtils.doGet(url, params);
        JSONObject resultObj = JSONObject.parseObject(result);
        if (resultObj == null) {
            logger.error("request faild: no result");
            return null;
        }
        if (resultObj.getIntValue("code") == 0) {
            JSONObject dataObj = resultObj.getJSONObject("data");
            return dataObj.getString("qzId");
        }
        logger.error("request faild: {}", resultObj.getString("msg"));
        return null;
    }

    /**
     * 根据空间ID和友户通用户ID获取空间用户ID
     *
     * @param qzId    空间ID
     * @param userIds 友户通用户ID
     * @return
     */
    private static String[] getUspaceMemberIdsByYhtUserIds(String qzId, String[] userIds) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("qzId", qzId);
        params.put("yhtUserIds", JSON.toJSONString(userIds));
        params = genParams(URI_GET_MEMBER_ID, params);
        String url = INAPI_DOMAIN + URI_GET_MEMBER_ID;
        String result = HttpClientUtils.doPost(url, params);
        JSONObject resultObj = JSONObject.parseObject(result);
        if (resultObj == null) {
            logger.error("request faild: no result");
            return null;
        }
        if (resultObj.getIntValue("code") == 0) {
            JSONObject dataObj = resultObj.getJSONObject("data");
            return dataObj.values().toArray(new String[dataObj.size()]);
        }
        logger.error("request faild: {}", resultObj.getString("msg"));
        return null;
    }

    /**
     * 根据空间ID和空间用户ID获取友户通租户ID和友户通用户ID
     *
     * @param qzId      空间ID
     * @param memberIds 空间用户ID
     * @return
     */
    private static JSONObject getTenantIdAndYhtUserIdsByQzIdAndUspaceMemberIds(String qzId, String[] memberIds) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("qzId", qzId);
        params.put("memberIdStr", JSON.toJSONString(memberIds));
        params = genParams(URI_GET_YHT_INFO, params);
        String url = INAPI_DOMAIN + URI_GET_YHT_INFO;
        String result = HttpClientUtils.doPost(url, params);
        JSONObject resultObj = JSONObject.parseObject(result);
        if (resultObj == null) {
            logger.error("request faild: no result");
            return null;
        }
        if (resultObj.getIntValue("code") == 0) {
            return resultObj.getJSONObject("data");
        }
        logger.error("request faild: {}", resultObj.getString("msg"));
        return null;
    }

    /**
     * 发送文本消息到服务号
     *
     * @param spaceId
     * @param pubAccId
     * @param sendScope
     * @param to
     * @param content
     * @return
     */
    private static boolean sendServiceTxt(String spaceId, String pubAccId, String sendScope, String[] to, String
            content) {
        if (StringUtils.isEmpty(spaceId) || StringUtils.isEmpty(pubAccId) || StringUtils.isEmpty(sendScope)
                || ArrayUtils.isEmpty(to) || StringUtils.isEmpty(content)) {
            return false;
        }

        TreeMap<String, String> params = new TreeMap<>();
        params.put("spaceId", spaceId);
        params.put("pubAccId", pubAccId);
        params.put("sendScope", sendScope);
        params.put("to", JSON.toJSONString(to));
        params.put("content", content);
        // gen params
        params = genParams(URI_MESSAGE_SERVICE_TXT, params);
        // url
        String url = INAPI_DOMAIN + URI_MESSAGE_SERVICE_TXT;
        // request
        String result = HttpClientUtils.doPost(url, params);
        return checkResult(result, URI_MESSAGE_SERVICE_TXT);
    }

    /**
     * 发送分享消息（业务消息）到服务号
     *
     * @param spaceId
     * @param pubAccId
     * @param sendScope
     * @param to
     * @param title
     * @param desc
     * @param detailUrl
     * @return
     */
    private static boolean sendServiceShare(String spaceId, String pubAccId, String sendScope, String[] to, String
            title,
                                            String desc, String detailUrl) {
        if (StringUtils.isEmpty(spaceId) || StringUtils.isEmpty(pubAccId) || StringUtils.isEmpty(sendScope)
                || ArrayUtils.isEmpty(to) || StringUtils.isEmpty(title) || StringUtils.isEmpty(desc)) {
            return false;
        }

        TreeMap<String, String> params = new TreeMap<>();
        params.put("spaceId", spaceId);
        params.put("pubAccId", pubAccId);
        params.put("sendScope", sendScope);
        params.put("to", JSON.toJSONString(to));
        params.put("title", title);
        params.put("desc", desc);
        params.put("detailUrl", StringUtils.isEmpty(detailUrl) ? "" : detailUrl);
        // gen params
        params = genParams(URI_MESSAGE_SERVICE_SHARE, params);
        // url
        String url = INAPI_DOMAIN + URI_MESSAGE_SERVICE_SHARE;
        // request
        String result = HttpClientUtils.doPost(url, params);
        return checkResult(result, URI_MESSAGE_SERVICE_SHARE);
    }

    /**
     * 发送图文/多图文消息到服务号
     *
     * @param spaceId
     * @param pubAccId
     * @param sendScope
     * @param to
     * @param articles
     * @return
     */
    private static boolean sendServiceMixed(String spaceId, String pubAccId, String sendScope, String[] to, JSONArray
            articles) {
        if (StringUtils.isEmpty(spaceId) || StringUtils.isEmpty(pubAccId) || StringUtils.isEmpty(sendScope)
                || ArrayUtils.isEmpty(to) || articles == null) {
            return false;
        }

        TreeMap<String, String> params = new TreeMap<>();
        params.put("spaceId", spaceId);
        params.put("pubAccId", pubAccId);
        params.put("sendScope", sendScope);
        params.put("to", JSON.toJSONString(to));
        params.put("articles", articles.toJSONString());
        // gen params
        params = genParams(URI_MESSAGE_SERVICE_MIXED, params);
        // url
        String url = INAPI_DOMAIN + URI_MESSAGE_SERVICE_MIXED;
        // request
        String result = HttpClientUtils.doPost(url, params);
        return checkResult(result, URI_MESSAGE_SERVICE_MIXED);
    }

    /**
     * 发送文本消息到应用号/应用通知
     *
     * @param spaceId
     * @param appId
     * @param sendThrough
     * @param sendScope
     * @param to
     * @param content
     * @return
     */
    private static boolean sendAppTxt(String spaceId, String appId, String sendThrough, String sendScope, String[] to,
                                      String content) {
        if (StringUtils.isEmpty(spaceId) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(sendScope)
                || ArrayUtils.isEmpty(to) || StringUtils.isEmpty(content)) {
            return false;
        }

        TreeMap<String, String> params = new TreeMap<>();
        params.put("spaceId", spaceId);
        params.put("appId", appId);
        params.put("sendThrough", StringUtils.isEmpty(sendThrough) ? "appAccount" : sendThrough);
        params.put("sendScope", sendScope);
        params.put("to", JSON.toJSONString(to));
        params.put("content", content);
        // gen params
        params = genParams(URI_MESSAGE_APP_TXT, params);
        // url
        String url = INAPI_DOMAIN + URI_MESSAGE_APP_TXT;
        // request
        String result = HttpClientUtils.doPost(url, params);
        return checkResult(result, URI_MESSAGE_APP_TXT);
    }

    /**
     * 发送分享消息（业务消息）到应用号/应用通知
     *
     * @param spaceId
     * @param appId
     * @param sendThrough
     * @param sendScope
     * @param to
     * @param title
     * @param desc
     * @param detailUrl
     * @return
     */
    private static boolean sendAppShare(String spaceId, String appId, String sendThrough, String sendScope, String[] to,
                                        String title,
                                        String desc, String detailUrl) {
        if (StringUtils.isEmpty(spaceId) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(sendScope)
                || ArrayUtils.isEmpty(to) || StringUtils.isEmpty(title) || StringUtils.isEmpty(desc)) {
            return false;
        }

        TreeMap<String, String> params = new TreeMap<>();
        params.put("spaceId", spaceId);
        params.put("appId", appId);
        params.put("sendThrough", StringUtils.isEmpty(sendThrough) ? "appAccount" : sendThrough);
        params.put("sendScope", sendScope);
        params.put("to", JSON.toJSONString(to));
        params.put("title", title);
        params.put("desc", desc);
        params.put("detailUrl", StringUtils.isEmpty(detailUrl) ? "" : detailUrl);
        // gen params
        params = genParams(URI_MESSAGE_APP_SHARE, params);
        // url
        String url = INAPI_DOMAIN + URI_MESSAGE_APP_SHARE;
        // request
        String result = HttpClientUtils.doPost(url, params);
        return checkResult(result, URI_MESSAGE_APP_SHARE);
    }

    /**
     * 发送图文/多图文消息到应用号
     *
     * @param spaceId
     * @param appId
     * @param sendScope
     * @param to
     * @param articles
     * @return
     */
    private static boolean sendAppMixed(String spaceId, String appId, String sendScope, String[] to, JSONArray
            articles) {
        if (StringUtils.isEmpty(spaceId) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(sendScope)
                || ArrayUtils.isEmpty(to) || articles == null) {
            return false;
        }

        TreeMap<String, String> params = new TreeMap<>();
        params.put("spaceId", spaceId);
        params.put("appId", appId);
        params.put("sendScope", sendScope);
        params.put("to", JSON.toJSONString(to));
        params.put("articles", articles.toJSONString());
        // gen params
        params = genParams(URI_MESSAGE_APP_MIXED, params);
        // url
        String url = INAPI_DOMAIN + URI_MESSAGE_APP_MIXED;
        // request
        String result = HttpClientUtils.doPost(url, params);
        return checkResult(result, URI_MESSAGE_APP_MIXED);
    }

    private static TreeMap<String, String> genParams(String uri, TreeMap<String, String> params) {
        params = params == null ?new TreeMap<String, String>() : params;
        // token
        params.put("token", INAPI_TOKEN);
        // timestamp
        params.put("timestamp", Long.toString(System.currentTimeMillis()));
        // v
        params.put("v", INAPI_VERSION);
        // agentId
        params.put("agent_id", INAPI_AGENT_ID);
        // sign
        String sign = getSign(uri, params);
        params.put("sign", sign);
        return params;
    }

    /**
     * 根据uri和参数签名
     * <p>
     * sign = md5(URI+STR+SALT)
     * STR:参数数组按键由小到大排序（除去sign字段）
     * 拼装成http query形式：param1=value1&param2=value2&...&paramn=valuen
     *
     * @param uri
     * @param params
     * @return
     */
    private static String getSign(String uri, TreeMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        // uri
        sb.append(uri);
        // params
        Set<Map.Entry<String, String>> paramSet = params.entrySet();
        for (Map.Entry<String, String> param : paramSet) {
            sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        // salt
        sb.append(INAPI_SALT);
        return SecurityUtils.encryptMD5(sb.toString(), true);
    }

    /**
     * 输出请求结果
     *
     * @param result
     * @param uri
     * @return
     */
    private static boolean checkResult(String result, String uri) {
        logger.info("checkResult:" + uri + ":" + result);
        // parse result
        JSONObject resultObj = JSON.parseObject(result);
        if (resultObj == null) {

        }
        if (resultObj.getInteger("code") == 0) {
            logger.info("inapi request success");
            return true;
        }
        logger.error("inapi request fail, uri:{}, msg:{}", uri, resultObj.getString("msg"));
        return false;
    }

}
