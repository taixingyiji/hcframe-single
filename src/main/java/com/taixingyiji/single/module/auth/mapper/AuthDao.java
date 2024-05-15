package com.taixingyiji.single.module.auth.mapper;

import com.taixingyiji.base.module.auth.entity.OsSysMenu;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className AuthDao
 * @date 2021年04月25日 3:30 下午
 * @description 描述
 */
public interface AuthDao {
    List<Map<String,Object>> selectMenuList(OsSysMenu osSysMenu);

    List<String> getUserRoleAuth(String userId);

    List<String> getUserSingleRoleAuth(String userId);

    List<String> getUserRoleGroupAuth(String userId);

    List<String> getOrgRoleAuth(String orgCode);

    List<String> getOrgGroupAuth(String orgCode);

    Long getRoleOs(String userId);

    Long getGroupOs(String userId);

    Long getOrgOs(String orgCode);

    Long getOrgGroupOs(String orgCode);
}
