package com.taixingyiji.single.common.utils;

/**
 * @author lhc
 * @version 1.0
 * @className CastUtil
 * @date 2022年04月27日 11:24
 * @description 描述
 */
public class CastUtil {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
