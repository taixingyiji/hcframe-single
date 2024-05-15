package com.taixingyiji.single.module.auth.service;

import com.taixingyiji.base.common.ResultVO;

/**
 * @author lhc
 * @version 1.0
 * @className RoleUserService
 * @date 2021年04月15日 9:09 上午
 * @description 描述
 */
public interface RoleUserService {
    ResultVO<Object> roleUserBind(String userId, String roleIds);

    ResultVO<Object> roleGroupBind(String userId, String groupIds);

    ResultVO<Object> getUserRole(String userId);

    ResultVO<Object> getUserGroup(String userId);
}
