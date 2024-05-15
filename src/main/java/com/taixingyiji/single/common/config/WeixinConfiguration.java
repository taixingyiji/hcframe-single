package com.taixingyiji.single.common.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lhc
 * @version 1.0
 * @className WeixinConfiguration
 * @date 2022年08月11日 10:10
 * @description 描述
 */
@Configuration
@EnableConfigurationProperties(WeixinConfig.class)
@AutoConfigureAfter(WeixinConfig.class)
public class WeixinConfiguration {
}
