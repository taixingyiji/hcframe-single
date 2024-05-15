package com.taixingyiji.single.module.log.service;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

import java.util.List;
import java.util.Map;

 /**
 * @author lhc
 * @version 1.0
 * @className GbLoginLogsService
 * @date 2022-01-06 14:26:34
 * @description (GbLoginLogs)表服务接口
 */
public interface GbLoginLogsService {

    ResultVO<Map<String, Object>> add(Map<String, Object> data);

    ResultVO<Map<String, Object>> update(Map<String, Object> data, Integer version);

    ResultVO<Integer> updateMany(Map<String, Object> data, String ids);

    ResultVO<Integer> delete(String ids);

    ResultVO<Map<String, Object>> deleteOne(String id);

    ResultVO<PageInfo<Map<String, Object>>> getList(String data, WebPageInfo webPageInfo);

    ResultVO<Map<String, Object>> getOne(String id);

    ResultVO<List<Map<String, Object>>> getMany(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getReference(String data, WebPageInfo webPageInfo, String target, String id);
}
