package com.taixingyiji.single.common.utils;

import com.taixingyiji.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author lhc
 * @version 1.0
 * @className GenerateDealId
 * @date 2021年07月12日 1:10 下午
 * @description 每隔一段时间区间，生成随机id
 */
@Component
public class GenerateDealId {

    private static RedisUtil redisUtil;

    public static final String CRON_DEAL_ID = "cronDealId";
    public static final String NOW_KEY = "nowKey";
    public static final String CRON_KEY = "cronKey";
    public static final String CRON_CLOSE_KEY = "cronCloseKey";

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        GenerateDealId.redisUtil = redisUtil;
    }




//    @Scheduled(
//            cron = "${repchain.dealIdCron}"
//    )
    public void generate() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String cronKey = (String) redisUtil.hget(CRON_DEAL_ID,NOW_KEY);
        String closeKey = (String) redisUtil.hget(CRON_DEAL_ID,CRON_KEY);
        redisUtil.hset(CRON_DEAL_ID,CRON_KEY,cronKey);
        redisUtil.hset(CRON_DEAL_ID,CRON_CLOSE_KEY,closeKey);
        redisUtil.hset(CRON_DEAL_ID, NOW_KEY, uuid);
    }

    public static String getId() {
        if (redisUtil.hget(CRON_DEAL_ID, NOW_KEY) != null) {
            return (String) redisUtil.hget(CRON_DEAL_ID, NOW_KEY);
        } else {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String cronKey = (String) redisUtil.hget(CRON_DEAL_ID,NOW_KEY);
            redisUtil.hset(CRON_DEAL_ID, NOW_KEY, uuid,60 * 60);
            String closeKey = (String) redisUtil.hget(CRON_DEAL_ID,CRON_KEY);
            redisUtil.hset(CRON_DEAL_ID,CRON_KEY,cronKey);
            redisUtil.hset(CRON_DEAL_ID,CRON_CLOSE_KEY,closeKey);
            return uuid;
        }
    }

    public static String getCloseId() {
        return (String) redisUtil.hget(CRON_DEAL_ID, CRON_CLOSE_KEY);
    }

    public static String getMatchId() {
        return (String) redisUtil.hget(CRON_DEAL_ID, CRON_KEY);
    }
}
