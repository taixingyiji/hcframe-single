package com.taixingyiji.single.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类类
 *
 */
public class MD5Utils {

	/**
	 * 加密
	 *
	 * @param data
	 *            原始数据
	 * @return 加密后的数据
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(data.getBytes("UTF8"));
		return StringUtils.bytesToString(digest.digest());
	}

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		System.out.println(MD5Utils.encode("Guobo@123"));
	}
}
