package com.taixingyiji.single.module.log.service;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

import java.util.Map;

public interface LogService {
	ResultVO<PageInfo<Map<String, Object>>> getLogList(String data, WebPageInfo webPageInfo);

	ResultVO<PageInfo<Map<String, Object>>> getLoginLogList(String data, WebPageInfo webPageInfo);
}
