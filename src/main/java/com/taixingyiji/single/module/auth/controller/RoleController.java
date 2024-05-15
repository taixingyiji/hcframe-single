package com.taixingyiji.single.module.auth.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.redis.RedisUtil;
import com.taixingyiji.single.module.auth.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleController
 * @date 2021年03月26日 9:40 上午
 * @description 角色管理
 */
@RestController
@Api(tags = "角色管理")
@RequestMapping("role")
public class RoleController {

    final RoleService roleService;
    final RedisUtil redisUtil;

    public RoleController(RoleService roleService,
                          RedisUtil redisUtil) {
        this.roleService = roleService;
        this.redisUtil = redisUtil;
    }

    @PostMapping()
    @LogAnno(operateType="新增角色信息",moduleName="系统管理-权限管理-角色管理")
    @ApiOperation(value = "新增role", notes = "给后台传key-value对象模式即可")
    @RequiresPermissions(value = {"system:auth:role:add"})
    public ResultVO<Object> addRole(@RequestParam Map<String, Object> role) {
        return roleService.addRole(role);
    }

    @PutMapping("/{version}")
    @LogAnno(operateType="更新角色信息",moduleName="系统管理-权限管理-角色管理")
    @ApiOperation(value = "更新role")
    @RequiresPermissions(value = {"system:auth:role:edit"})
    public ResultVO<Map<String, Object>> updateRole(@RequestParam Map<String, Object> role, @PathVariable Integer version) {
        redisUtil.del("auth");
        return roleService.updateRole(role, version);
    }

    @DeleteMapping("/{ids}")
    @LogAnno(operateType="删除角色信息",moduleName="系统管理-权限管理-角色管理")
    @ApiOperation(value = "删除role", notes = "删除后关联表数据也会被删除")
    @RequiresPermissions(value = {"system:auth:role:delete"})
    public ResultVO<Object> deleteRole(@PathVariable String ids) {
        redisUtil.del("auth");
        return roleService.deleteRole(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取角色列表")
    @RequiresPermissions(value = {"role","system:auth:role:list","roleAuth","system:empower:role:list"},logical = Logical.OR)
    public ResultVO<PageInfo<Map<String, Object>>> getOrgList(String data, WebPageInfo webPageInfo) {
        return roleService.getRoleList(data, webPageInfo);
    }

    @GetMapping("valid")
    @ApiOperation(value = "校验角色是否重复")
    @ApiImplicitParam(name = "code", value = "角色编码")
    public ResultVO<Object> validCode(String code) {
        return roleService.validCode(code);
    }

    @GetMapping("all")
    @ApiOperation(value = "获取全部角色信息")
    public ResultVO<List<Map<String,Object>>> getAll() {
        return roleService.getAll();
    }
}
