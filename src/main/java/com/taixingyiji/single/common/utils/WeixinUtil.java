package com.taixingyiji.single.common.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.taixingyiji.single.common.config.RedisConstant;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.redis.RedisUtil;
import com.taixingyiji.single.common.config.WeixinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author lhc
 * @version 1.0
 * @className WeixinUtil
 * @date 2022年08月11日 09:57
 * @description 描述
 */
@Component
public class WeixinUtil {

    final WeixinConfig weixinConfig;
    final RedisUtil redisUtil;
    private final Logger logger = LoggerFactory.getLogger(WeixinUtil.class);
    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    public final static String ACCESS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public final static String ERRCODE = "errcode";
    public final static String ACCESS_TOKEN = "access_token";
    public final static String TICKET = "ticket";

    public WeixinUtil(WeixinConfig weixinConfig,
                      RedisUtil redisUtil) {
        this.weixinConfig = weixinConfig;
        this.redisUtil = redisUtil;
    }

    /**
     * @return java.lang.String
     * @author lhc
     * @description // 获取微信accessToken
     * @date 2022/8/11 10:29
     * @params []
     **/
    public String getAccessToken() {
        // 从redis获取token
        String token = (String) redisUtil.get(RedisConstant.WEIXIN + RedisConstant.ACCESS_TOKEN);
        // 若redis为空，则需要从微信处获取token
        if (StringUtils.isEmpty(token)) {
            String requestUrl = ACCESS_TOKEN_URL.replace("APPID", weixinConfig.getAppId()).replace("SECRET", weixinConfig.getAppSecret());
            String response = HttpUtil.get(requestUrl);
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 判断微信返回结果是否异常
            if (StringUtils.isEmpty(jsonObject.get(ERRCODE)) || Integer.parseInt(String.valueOf(jsonObject.get(ERRCODE))) == 0) {
                // 持久化到redis中
                redisUtil.set(RedisConstant.WEIXIN + RedisConstant.ACCESS_TOKEN, jsonObject.getStr(ACCESS_TOKEN), 7000);
                return jsonObject.getStr(ACCESS_TOKEN);
            } else {
                logger.error(jsonObject.toStringPretty());
                throw new ServiceException("获取公众号token信息异常");
            }
        } else {
            return token;
        }
    }

    /**
     * @return cn.hutool.json.JSONObject
     * @author lhc
     * @description // 获取微信jssdk授权
     * @date 2022/8/11 10:30
     * @params []
     **/
    public String jsapiTicket() {
        // 从redis获取ticket
        String ticket = (String) redisUtil.get(RedisConstant.WEIXIN + RedisConstant.JS_TICKET);
        // 若为空，则从微信服务器获取
        if (StringUtils.isEmpty(ticket)) {
            String accessToken = getAccessToken();
            String requestUrl = ACCESS_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
            String response = HttpUtil.get(requestUrl);
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 判断微信返回结果是否异常
            if (StringUtils.isEmpty(jsonObject.get(ERRCODE)) || Integer.parseInt(String.valueOf(jsonObject.get(ERRCODE))) == 0) {
                // 持久化到redis中
                redisUtil.set(RedisConstant.WEIXIN + RedisConstant.JS_TICKET, jsonObject.getStr(TICKET), 7000);
                return jsonObject.getStr(TICKET);
            } else {
                logger.error(jsonObject.toStringPretty());
                throw new ServiceException("获取公众号ticket信息异常");
            }
        }
        return ticket;
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
