package com.taixingyiji.single.module.auth.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.redis.RedisUtil;
import com.taixingyiji.single.module.auth.service.RoleGroupServie;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleGroupController
 * @date 2021年04月02日 10:11 上午
 * @description 描述
 */
@RestController
@Api(tags = "角色组管理")
@RequestMapping("roleGroup")
public class RoleGroupController {

    final RoleGroupServie roleGroupServie;
    final RedisUtil redisUtil;

    public RoleGroupController(RoleGroupServie roleGroupServie,
                               RedisUtil redisUtil) {
        this.roleGroupServie = roleGroupServie;
        this.redisUtil = redisUtil;
    }

    @PostMapping()
    @LogAnno(operateType="新增角色组信息",moduleName="系统管理-权限管理-角色组管理")
    @ApiOperation(value = "新增角色组", notes = "给后台传key-value对象模式即可")
    @RequiresPermissions(value = {"system:auth:roleGroup:add"})
    public ResultVO<Map<String,Object>>  addRole(@RequestParam Map<String, Object> roleGroup) {
        return roleGroupServie.add(roleGroup);
    }

    @PutMapping("/{version}")
    @LogAnno(operateType="更新角色组信息",moduleName="系统管理-权限管理-角色组管理")
    @ApiOperation(value = "更新角色组")
    @RequiresPermissions(value = {"system:auth:roleGroup:edit"})
    public ResultVO<Map<String, Object>> updateRole(@RequestParam Map<String, Object> roleGroup, @PathVariable Integer version) {
        redisUtil.del("auth");
        return roleGroupServie.update(roleGroup, version);
    }

    @DeleteMapping("/{ids}")
    @RequiresPermissions(value = {"system:auth:roleGroup:delete"})
    @LogAnno(operateType="删除角色组信息",moduleName="系统管理-权限管理-角色组管理")
    @ApiOperation(value = "删除角色组", notes = "删除后关联表数据也会被删除")
    public ResultVO<Integer> deleteOrg(@PathVariable String ids) {
        redisUtil.del("auth");
        return roleGroupServie.delete(ids);
    }

    @GetMapping()
    @ApiOperation(value = "获取角色组列表")
    @RequiresPermissions(value = {"roleGroup","system:auth:roleGroup:list"},logical = Logical.OR)
    public ResultVO<PageInfo<Map<String, Object>>> getOrgList(String data, WebPageInfo webPageInfo) {
        return roleGroupServie.getList(data, webPageInfo);
    }

    @PostMapping("bind")
    @LogAnno(operateType="绑定角色组信息",moduleName="系统管理-权限管理-角色组管理")
    @ApiOperation(value = "绑定角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "角色组ID",required = true),
            @ApiImplicitParam(name = "roleIds", value = "角色ID数组",required = true)
    })
    @RequiresPermissions(value = {"system:auth:roleGroup:bind"})
    public ResultVO<Object> bind(Integer groupId, String roleIds) {
        redisUtil.del("auth");
        return roleGroupServie.bind(groupId, roleIds);
    }

    @GetMapping("getRoles")
    @ApiOperation(value = "获取角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "角色组ID",required = true),
    })
    public ResultVO<Object> getRoles(Integer groupId) {
        return roleGroupServie.getRoles(groupId);
    }

    @GetMapping("all")
    @ApiOperation(value = "获取全部角色组")
    public ResultVO<Object> getAll() {
        return roleGroupServie.getAll();
    }

}
