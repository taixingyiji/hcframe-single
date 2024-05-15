package com.taixingyiji.single.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lhc
 * @version 1.0
 * @className WeixinConfig
 * @date 2022年01月11日 1:17 PM
 * @description 描述
 */
@Component
@ConfigurationProperties(prefix = "weixin")
@Data
public class WeixinConfig {
    private String appId;
    private String appSecret;
    private String jsUrl;
}
