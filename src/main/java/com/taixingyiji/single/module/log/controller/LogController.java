package com.taixingyiji.single.module.log.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.single.module.log.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("log")
@Api(tags = "日志查询")
public class LogController {

	    final LogService logService;

	    public LogController(LogService logService) {
	        this.logService = logService;
	    }

	    @GetMapping()
	    @ApiOperation(value = "日志查询模块")
	    public ResultVO<PageInfo<Map<String,Object>>> getLogList(String data, WebPageInfo webPageInfo) {
	        return logService.getLogList(data, webPageInfo);
	    }
	    @GetMapping("/loginlog")
	    @ApiOperation(value = "登录日志查询模块")
	    public ResultVO<PageInfo<Map<String,Object>>> getLoginLogList(String data, WebPageInfo webPageInfo) {
	        return logService.getLoginLogList(data, webPageInfo);
	    }

}
