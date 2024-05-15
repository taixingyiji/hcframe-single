package com.taixingyiji.single.module.log.service.impl;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.single.module.log.service.GbLoginLogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className GbLoginLogsServiceImpl
 * @date 2022-01-06 14:26:32
 * @description (GbLoginLogs)表服务实现类
 */
@Service
public class GbLoginLogsServiceImpl implements GbLoginLogsService {

    public static final String TABLE_NAME = "gb_login_logs".toUpperCase();
    public static final String PK = "login_log_id".toUpperCase();
    public static final OsSysTable TABLE_INFO = OsSysTable.builder().tablePk(PK).tableName(TABLE_NAME).build();

    final TableService tableService;

    final BaseMapper baseMapper;

    public GbLoginLogsServiceImpl(TableService tableService, BaseMapper baseMapper) {
        this.tableService = tableService;
        this.baseMapper = baseMapper;
    }

    @Override
    public ResultVO<Map<String, Object>> add(Map<String, Object> data) {
        return tableService.saveWithDate(TABLE_INFO, data);
    }

    @Override
    public ResultVO<Map<String, Object>> update(Map<String, Object> data, Integer version) {
        return tableService.updateWithDate(TABLE_INFO, data, version);
    }

    @Override
    public ResultVO<Integer> updateMany(Map<String, Object> data, String ids) {
        data.put(PK, ids);
        return tableService.updateBatchWithDate(TABLE_INFO, data);
    }

    @Override
    public ResultVO<Integer> delete(String ids) {
        return tableService.logicDelete(TABLE_INFO, ids);
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getList(String data, WebPageInfo webPageInfo) {
        return ResultVO.getSuccess(tableService.searchSingleTables(data, TABLE_INFO, webPageInfo));
    }

    @Override
    public ResultVO<Map<String, Object>> getOne(String id) {
        return ResultVO.getSuccess(tableService.getOne(TABLE_INFO, id));
    }

    @Override
    public ResultVO<List<Map<String, Object>>> getMany(String ids) {
        return ResultVO.getSuccess(tableService.getMany(TABLE_INFO, ids));
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getReference(String data, WebPageInfo webPageInfo, String target, String id) {
        return ResultVO.getSuccess(tableService.getReference(TABLE_INFO, data, webPageInfo, target, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Map<String, Object>> deleteOne(String id) {
        Map<String, Object> map = baseMapper.selectByPk(TABLE_INFO, id);
        ResultVO result = tableService.logicDelete(TABLE_INFO, id);
        if (result.getCode() != 0) {
            throw new ServiceException("删除失败");
        }
        return ResultVO.getSuccess(map);
    }
}
