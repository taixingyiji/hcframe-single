package com.taixingyiji.single.common.utils;

import com.taixingyiji.base.common.utils.TokenProccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串工具类类
 *
 * @author 袁涛
 * @date 2018.08.16
 */
public class StringUtils {
	private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 字节数组转为字符串
	 *
	 * @param data
	 *            字节数组
	 * @return 字符串
	 */
	public static String bytesToString(byte[] data) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buffer.append(Integer.toHexString((0x000000FF & data[i]) | 0xFFFFFF00)
					.substring(6));
		}

		return buffer.toString();
	}

	public static void main(String[] args) {
		TokenProccessor tokenProccessor = TokenProccessor.getInstance();
		//生成一个token
		String token = tokenProccessor.makeToken();
		System.out.println(token);
	}
}
