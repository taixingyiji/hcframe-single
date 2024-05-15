package com.taixingyiji.single.module.auth.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.module.auth.entity.OsSysMenu;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.single.module.auth.service.AuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author lhc
 * @version 1.0
 * @className AuthController
 * @date 2021年04月21日 11:04 上午
 * @description 描述
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    final AuthService authService;

    final BaseMapper baseMapper;

    public AuthController(AuthService authService,
                          @Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper) {
        this.authService = authService;
        this.baseMapper = baseMapper;
    }

    @GetMapping()
    public ResultVO<Object> get() {
        return ResultVO.getSuccess(authService.getUserAuth("6478745544359298274"));
    }

    @GetMapping("menu")
    public ResultVO getMenuResult() {
        Map<String, Object> map = (Map<String, Object>)SecurityUtils.getSubject().getPrincipal();
        if (map == null) {
            return ResultVO.getNoLogin();
        }
        List<OsSysMenu> menus;
        Set<String> set;
//        set = authService.getAllAuth();
//        menus = authService.getMenuResult();
        if ("ADMIN_USER".equals(map.get("LOGIN_NAME"))) {
            set = authService.getAllAuth();
            menus = authService.getMenuResult();
        } else {
            set = authService.getUserAuth(String.valueOf(map.get("USER_ID")));
            menus = authService.getUserMenuResult(set);
        }
        if (menus == null || menus.size() == 0) {
            return ResultVO.getSuccess(new ArrayList<>());
        }
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("menu", authService.formatMenu(menus));
        resultMap.put("auth", set);
        return ResultVO.getSuccess(resultMap);
    }

    @GetMapping("menuList")
    @RequiresPermissions(value = {"system:auth:function:list","menu"},logical = Logical.OR)
    public ResultVO<List<Map<String,Object>>> getMenuList(OsSysMenu osSysMenu) {
        return ResultVO.getSuccess(authService.getMenuResultList(osSysMenu));
    }
}
