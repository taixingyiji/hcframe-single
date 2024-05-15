package com.taixingyiji.single.module.auth.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.redis.RedisUtil;
import com.taixingyiji.single.module.auth.service.RoleDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Li Xinlei
 * @version 1.0
 * @className RoleDeptController
 * @date 2021-4-20 17:15:09
 * @description 描述
 */
@RestController
@RequestMapping("/roleDept")
@Api(tags="机构授权")
public class RoleDeptController {

    final RoleDeptService roleDeptService;
    final RedisUtil redisUtil;

    public RoleDeptController(RoleDeptService roleDeptService,
                              RedisUtil redisUtil) {
        this.roleDeptService = roleDeptService;
        this.redisUtil = redisUtil;
    }

    @PostMapping("role")
    @RequiresPermissions(value = {"system:empower:org:role"})
    @LogAnno(operateType="角色机构绑定",moduleName="系统管理-权限管理-机构授权")
    @ApiOperation(value = "角色机构绑定")
    public ResultVO<Object> roleDeptBind(String deptId, String roleIds) {
        redisUtil.del("auth");
        return roleDeptService.roleDeptBind(deptId,roleIds);
    }

    @GetMapping("role")
    @ApiOperation(value = "获取机构角色")
    public ResultVO<Object> getDeptRole(String deptId) {
        return roleDeptService.getDeptRole(deptId);
    }

    @PostMapping("roleGroup")
    @LogAnno(operateType="角色机构组绑定",moduleName="系统管理-权限管理-机构授权")
    @ApiOperation(value = "角色机构组绑定")
    @RequiresPermissions(value = {"system:empower:org:roleGroup"})
    public ResultVO<Object> roleGroupBind(String deptId, String groupIds) {
        redisUtil.del("auth");
        return roleDeptService.roleGroupBind(deptId,groupIds);
    }

    @GetMapping("roleGroup")
    @ApiOperation(value = "获取角色机构组")
    public ResultVO<Object> getDeptGroup(String deptId) {
        return roleDeptService.getDeptGroup(deptId);
    }
}
