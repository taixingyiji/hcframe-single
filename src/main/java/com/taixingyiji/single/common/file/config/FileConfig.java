package com.taixingyiji.single.common.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
@Data
public class FileConfig {
    private String path;
    private String herf;
    private String reportPath;
    private String zip;
}
