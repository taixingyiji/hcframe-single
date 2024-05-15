package com.taixingyiji.single.common.utils;

import java.util.Random;

/**
 * @author lhc
 * @version 1.0
 * @className RandomUtil
 * @date 2022年06月22日 10:55
 * @description 描述
 */
public class RandomUtil {

    public static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
