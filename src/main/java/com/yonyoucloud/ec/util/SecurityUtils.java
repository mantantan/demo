package com.yonyoucloud.ec.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密/解密工具类
 * <p>
 * Created by litengfei on 2017/5/26.
 */
public class SecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    /**
     * MD5加密
     *
     * @param originalStr
     * @param toLowerCase
     * @return
     */
    public static String encryptMD5(String originalStr, boolean toLowerCase) {
        try {
            // digest
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 计算md5函数
            digest.update(originalStr.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encodeHex(digest.digest(), toLowerCase));
        } catch (NoSuchAlgorithmException e) {
            logger.error("encryptMD5 faild", e);
            return "";
        }
    }

}