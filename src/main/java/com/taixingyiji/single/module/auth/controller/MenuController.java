package com.taixingyiji.single.module.auth.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.redis.RedisUtil;
import com.taixingyiji.single.module.auth.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * @author wewe
 * @date 2021年4月13日
 * @description 角色管理
 */
@RestController("userMenuController")
@Api(tags = "功能级权限管理")
@RequestMapping("menu")
public class MenuController {

	@Autowired MenuService menuService;

    final RedisUtil redisUtil;

    public MenuController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

	@PostMapping("add")
    @RequiresPermissions(value = {"system:auth:function:add"})
	@LogAnno(operateType="新增功能权限",moduleName="系统管理-权限管理-功能权限管理")
    @ApiOperation(value = "新增功能级权限", notes = "给后台传key-value对象模式即可")
    public ResultVO<Object> addMenu(@RequestParam Map<String, Object> data) {
        data.remove("children");
        return menuService.addMenu(data);
    }

	@PostMapping("delete")
    @RequiresPermissions(value = {"system:auth:function:delete"})
	@LogAnno(operateType="删除功能权限",moduleName="系统管理-权限管理-功能权限管理")
    @ApiOperation(value = "删除功能级权限", notes = "删除后关联表数据也会被删除")
    public ResultVO<Object> deleteMenu(@RequestParam List<Long> ids) {
        redisUtil.del("auth");
        return menuService.deleteMenu(ids);
    }

	@PutMapping("/{version}")
    @RequiresPermissions(value = {"system:auth:function:edit"})
	@LogAnno(operateType="更新功能权限",moduleName="系统管理-权限管理-功能权限管理")
    @ApiOperation(value = "更新功能级权限")
    public ResultVO<Map<String, Object>> updateMenu(@RequestParam Map<String, Object> data, @PathVariable Integer version) {
        redisUtil.del("auth");
        data.remove("children");
        return menuService.updateMenu(data, version);
    }

	@GetMapping("list")
    @ApiOperation(value = "查询功能级权限列表")
    @RequiresPermissions(value = {"system:auth:function:list","menu"},logical = Logical.OR)
    public ResultVO<PageInfo<Map<String, Object>>> getMenuList(String data, WebPageInfo webPageInfo) {
        return menuService.getMenuList(data, webPageInfo);
    }

	@PostMapping("addRole")
	@LogAnno(operateType="角色授权",moduleName="系统管理-授权管理-角色授权")
    @ApiOperation(value = "角色授权", notes = "roleId,menuIds,中间用逗号连接")
    @RequiresPermissions(value = {"system:empower:role:bind"})
    public ResultVO<Object> addRoleMenu(@RequestParam Long roleId,@RequestParam(required=false) List<String> menuIds) {
        redisUtil.del("auth");
        return menuService.addRoleMenu(roleId, menuIds);
    }

	@GetMapping("tree")
    @ApiOperation(value = "功能级权限树，根节点是业务系统")
    public ResultVO<Object> getMenuTree() {
        return menuService.getMenuTree();
    }

	@GetMapping("selected")
    @ApiOperation(value = "获取当前角色已选中节点")
    public ResultVO<Object> getSelectedMenu(@RequestParam Long roleId) {
        return menuService.getSelectedMenu(roleId);
    }

	@PostMapping("checkPath")
    @ApiOperation(value = "校验PATH是否唯一", notes = "")
    public ResultVO<Object> checkPath(@RequestParam Map<String, Object> data) {
        return menuService.checkPath(data);
    }

	@GetMapping("oslist")
    @ApiOperation(value = "获取系统信息列表")
    public ResultVO<Object> getOsList() {
        return menuService.getOsList();
    }

}
