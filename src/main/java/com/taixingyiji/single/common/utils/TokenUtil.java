package com.taixingyiji.single.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhc
 * @version 1.0
 * @className TokenUtil
 * @date 2021年07月05日 3:54 下午
 * @description 描述
 */
public class TokenUtil {

    public static String getToken(HttpServletRequest request) {
        return request.getHeader("token");
    }
}
