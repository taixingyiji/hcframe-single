package com.taixingyiji.single.common.config;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

/**
 * @author lhc
 * @version 1.0
 * @className WxClient
 * @date 2022年01月11日 1:32 PM
 * @description 描述
 */
@Component
public class WxClient {

    final WeixinConfig weixinConfig;

    public WxClient(WeixinConfig weixinConfig) {
        this.weixinConfig = weixinConfig;
    }

    public JSONObject auth(String code) {
        String result = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + this.weixinConfig.getAppId() +
                "&secret=" + this.weixinConfig.getAppSecret() +
                "&js_code=" + code +
                "&grant_type=authorization_code");
        return JSONUtil.parseObj(result);
    }
}
