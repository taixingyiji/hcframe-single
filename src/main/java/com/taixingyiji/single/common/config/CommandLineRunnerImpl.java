package com.taixingyiji.single.common.config;

import com.taixingyiji.base.module.data.module.BaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * @author lhc
 * @date 2020-10-09
 * @description springboot启动执行配置类
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CommandLineRunnerImpl.class);

    BaseMapper baseMapper;

    //    @Autowired
//    List<CacheService> cacheServices;
    public CommandLineRunnerImpl(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("start success");
    }
}
