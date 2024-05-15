package com.taixingyiji.single.module.log.service.impl;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.single.module.log.service.LogService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class LogServiceImpl implements LogService{

	 private static final String LOG_ID = "LOG_ID";
	 private static final String OS_SYS_TITLE = "GB_LOGS";
	 private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(OS_SYS_TITLE).tablePk(LOG_ID).build();

	 private static final OsSysTable TABLE_INFO_LOGIN = OsSysTable.builder().tableName("GB_LOGIN_LOGS").tablePk("LOGIN_LOG_ID").build();

	    final BaseMapper baseMapper;

	    final TableService tableService;

	    public LogServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
	                          TableService tableService) {
	        this.baseMapper = baseMapper;
	        this.tableService = tableService;
	    }

	    @Override
	    public  ResultVO<PageInfo<Map<String, Object>>> getLogList(String data, WebPageInfo webPageInfo) {
	        PageInfo<Map<String,Object>> pageInfo = tableService.searchSingleTables(data, TABLE_INFO, webPageInfo);
	        return ResultVO.getSuccess(pageInfo);
	    }

	    @Override
	    public  ResultVO<PageInfo<Map<String, Object>>> getLoginLogList(String data, WebPageInfo webPageInfo) {
	        PageInfo<Map<String,Object>> pageInfo = tableService.searchSingleTables(data, TABLE_INFO_LOGIN, webPageInfo);
	        return ResultVO.getSuccess(pageInfo);
	    }

}
