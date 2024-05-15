package com.taixingyiji.single.module.auth.service;

import com.taixingyiji.base.module.auth.entity.OsSysMenu;
import com.taixingyiji.base.module.auth.vo.RouterVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lhc
 * @version 1.0
 * @className AuthService
 * @date 2021年04月21日 10:41 上午
 * @description 描述
 */
public interface AuthService {

    List<String> getUserRoleAuth(String userId);

    List<String> getUserRoleGroupAuth(String userId);

    List<String> getOrgRoleAuth(String orgCode);

    List<String> getOrgGroupAuth(String orgCode);

    Set<String> getUserAuth(String userId);

    List<OsSysMenu> getMenuResult();

    List<RouterVo> formatMenu(List<OsSysMenu> menus);

    List<OsSysMenu> getUserMenuResult(Set<String> set);

    Set<String> getAllAuth();

    List<Map<String,Object>> getMenuResultList(OsSysMenu osSysMenu);

    Long getUserOs(String userId);

}
