package com.taixingyiji.single.common.utils;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginUtil {

    static final int PHONE_LENGTH = 11;
    static final int IDCODE_LENGTH = 18;
    public static final String PRIVATEKEY_PREFIX = "-----BEGIN PRIVATE KEY-----";
    public static final String PRIVATEKEY_SUFFIX = "-----END PRIVATE KEY-----";

    static Logger logger = LoggerFactory.getLogger(LoginUtil.class);

    /**
     * @param phone 电话号
     * @return
     */
    public static boolean isPhone(String phone) {
        if (StrUtil.isEmpty(phone) && phone.length() != PHONE_LENGTH) {
            return false;
        }
        String regex = "^((13[0-9])|(14([01]|[4-9]))|(15([0-3]|[5-9]))|(16([2567]))|(17[0135678])|(18[0-9])|(19[0-9]))\\d{8}$";
        //String regex = "^[1][3456789]\\d{9}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * <blockquote><pre>
     * ^开头
     * [1-9]                            第一位1-9中的一个
     * \\d{5}                           五位数字（前六位省市县地区）
     * (18|19|20)                       现阶段可能取值范围18xx-20xx年
     * \\d{2}                           年份
     * ((0[1-9])|(10|11|12))            月份
     * (([0-2][1-9])|10|20|30|31)01     日期
     * \\d{3}                           三位数字，第十七位（奇数代表男，偶数代表女）
     * [0-9Xx]                          0123456789Xx其中的一个（第十八位为校验值）
     * $结尾
     *
     * ^开头
     * [1-9] 第一位1-9中的一个
     * \\d{5}                           五位数字（前六位省市县地区）
     * \\d{2}                           年份
     * ((0[1-9])|(10|11|12))            月份
     * (([0-2][1-9])|10|20|30|31)01     日期
     * \\d{3}                           三位数字（第十五位奇数代表男，偶数代表女），15位身份证不含X
     * $结尾
     * </pre></blockquote>
     *
     * @param idCode 身份证号
     * @return
     */
    public static boolean isIdCode(String idCode) {
        if (StrUtil.isEmpty(idCode)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

        boolean matches = idCode.matches(regularExpression);

        //判断第18位校验值
        if (matches) {

            if (idCode.length() == IDCODE_LENGTH) {
                try {
                    char[] charArray = idCode.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].equalsIgnoreCase(String.valueOf(idCardLast))) {
                        return true;
                    } else {
                        logger.error("身份证最后一位: {}, 错误, 正确的应该是: {}", String.valueOf(idCardLast).toUpperCase(), idCardY[idCardMod].toUpperCase());
                        return false;
                    }

                } catch (Exception e) {
                    logger.error("异常: {}", idCode, e);
                    return false;
                }
            }

        }
        return matches;
    }

    /**
     * @param privateKey 私钥pem字符串
     */
    public static boolean isPrivateKey(String privateKey) {
        if (StrUtil.isEmpty(privateKey)) {
            return false;
        }
        String withOutBreaks = StrUtil.removeAllLineBreaks(privateKey);
        return withOutBreaks.startsWith(PRIVATEKEY_PREFIX) && withOutBreaks.endsWith(PRIVATEKEY_SUFFIX);
    }

    public static void main(String[] args) {
        System.out.println(isPhone(""));
        System.out.println(isIdCode(""));
        System.out.println(isPrivateKey(""));
    }
}
