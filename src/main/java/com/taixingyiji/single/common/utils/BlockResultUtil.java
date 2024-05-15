package com.taixingyiji.single.common.utils;

//import com.rcjava.util.OperLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lhc
 * @version 1.0
 * @className BlockResultUtil
 * @date 2021年07月08日 1:23 下午
 * @description 描述
 */
public class BlockResultUtil {
    private static final Logger logger = LoggerFactory.getLogger(BlockResultUtil.class);

//    public static Map<String, Object> toMap(Peer.OperLog ol) {
//        String order = (String) OperLogUtil.toInstance(ol.getNewValue().toByteArray());
//        return JSONUtil.parseObj(order);
//    }
//
//    public static <T> T toBean(Peer.OperLog ol, Class<T> clazz) {
//        String order = (String) OperLogUtil.toInstance(ol.getNewValue().toByteArray());
//        return JSONUtil.toBean(order, clazz);
//    }
//
//    public static Double toDouble(Peer.OperLog ol) {
//        String value = OperLogUtil.toJson(ol.getNewValue().toByteArray());
//        return Double.parseDouble(value);
//    }
//
//
//    public static Integer toInteger(Peer.OperLog ol) {
//        String value = OperLogUtil.toJson(ol.getNewValue().toByteArray());
//        return Integer.parseInt(value);
//    }
//
//    public static String getCommonId(Peer.OperLog ol, String type) {
//        String key = ol.getKey();
//        String endValue = key.split(type)[0];
//        String[] ids = endValue.split("_");
//        return ids[0];
//    }
}
