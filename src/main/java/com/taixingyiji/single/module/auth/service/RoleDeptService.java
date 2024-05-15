package com.taixingyiji.single.module.auth.service;

import com.taixingyiji.base.common.ResultVO;

/**
 *
 * @author Li Xinlei
 * @version 1.0
 * @className RoleDeptService
 * @date 2021-4-20 17:16:50
 * @description 描述
 */

public interface RoleDeptService {
    ResultVO<Object> roleDeptBind(String deptId, String roleIds);

    ResultVO<Object> roleGroupBind(String deptId, String groupIds);

    ResultVO<Object> getDeptRole(String deptId);

    ResultVO<Object> getDeptGroup(String deptId);
}
