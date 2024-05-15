package com.taixingyiji.single.module.log.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.single.module.log.service.GbLoginLogsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


 /**
 * @author lhc
 * @version 1.0
 * @className GbLoginLogsController
 * @date 2022-01-06 14:26:30
 * @description (GbLoginLogs)表控制层
 */
@Api(tags = "GbLoginLogsController操作")
@RestController
@RequestMapping("gbLoginLogs")
public class GbLoginLogsController {

    final GbLoginLogsService gbLoginLogsService;

    public GbLoginLogsController(GbLoginLogsService gbLoginLogsService) {
        this.gbLoginLogsService = gbLoginLogsService;
    }

    @PostMapping()
    @LogAnno(operateType = "新增GbLoginLogs", moduleName = "新增GbLoginLogs")
    @ApiOperation(value = "新增GbLoginLogs", notes = "传key-value对象模式即可")
    // @RequiresPermission(value = {"GbLoginLogs:add"})
    public ResultVO<Map<String, Object>> add(@RequestParam Map<String, Object> data) {
        return gbLoginLogsService.add(data);
    }

    @PutMapping("/{version}")
    @LogAnno(operateType = "更新GbLoginLogs", moduleName = "更新GbLoginLogs")
    @ApiOperation(value = "更新GbLoginLogs")
    // @RequiresPermission(value = {"GbLoginLogs:edit"})
    public ResultVO<Map<String, Object>> update(@RequestParam Map<String, Object> data, @PathVariable Integer version) {
        data.remove("id");
        return gbLoginLogsService.update(data, version);
    }

    @PutMapping("/many/{ids}")
    @LogAnno(operateType = "批量更新GbLoginLogs", moduleName = "批量更新GbLoginLogs")
    @ApiOperation(value = "批量更新GbLoginLogs")
    // @RequiresPermission(value = {"GbLoginLogs:edit:batch"})
    public ResultVO<Integer> updateMany(@RequestParam Map<String, Object> data, @PathVariable String ids) {
        return gbLoginLogsService.updateMany(data, ids);
    }

    @DeleteMapping("/{ids}")
    @LogAnno(operateType = "删除GbLoginLogs", moduleName = "删除GbLoginLogs")
    @ApiOperation(value = "删除GbLoginLogs")
    // @RequiresPermission(value = {"GbLoginLogs:delete"})
    public ResultVO<Integer> delete(@PathVariable String ids) {
        return gbLoginLogsService.delete(ids);
    }

     @DeleteMapping("one/{id}")
     @LogAnno(operateType = "删除单条GbLoginLogs", moduleName = "删除单条GbLoginLogs")
     @ApiOperation(value = "删除单条GbLoginLogs")
     // @RequiresPermission(value = {"GbLoginLogs:delete:one"})
     public ResultVO<Map<String, Object>> deleteOne(@PathVariable String id) {
         return gbLoginLogsService.deleteOne(id);
     }

    @GetMapping()
    @ApiOperation(value = "获取GbLoginLogs列表")
    // @RequiresPermission(value = {"GbLoginLogs", "GbLoginLogs:list"}, logical = Logical.OR)
    public ResultVO<PageInfo<Map<String, Object>>> getList(String data, WebPageInfo webPageInfo) {
        return gbLoginLogsService.getList(data, webPageInfo);
    }

    @GetMapping("one/{id}")
    @ApiOperation(value = "获取单条GbLoginLogs")
    // @RequiresPermission(value = {"GbLoginLogs:one"})
    public ResultVO<Map<String, Object>> getOne(@PathVariable String id) {
        return gbLoginLogsService.getOne(id);
    }

    @GetMapping("many/{ids}")
    @ApiOperation(value = "获取多条GbLoginLogs")
    // @RequiresPermission(value = {"GbLoginLogs", "GbLoginLogs:many"}, logical = Logical.OR)
    public ResultVO<List<Map<String, Object>>> getMany(@PathVariable String ids) {
        return gbLoginLogsService.getMany(ids);
    }

    @GetMapping("reference")
    @ApiOperation(value = "获取GbLoginLogs关联列表")
    // @RequiresPermission(value = {"GbLoginLogs", "GbLoginLogs:reference"}, logical = Logical.OR)
    public ResultVO<PageInfo<Map<String, Object>>> getReference(String data, WebPageInfo webPageInfo, String target, String id) {
        return gbLoginLogsService.getReference(data, webPageInfo, target, id);
    }
}

