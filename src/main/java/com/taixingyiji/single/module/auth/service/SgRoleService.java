package com.taixingyiji.single.module.auth.service;

import com.taixingyiji.base.common.ResultVO;

import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className SgRoleService
 * @date 2021年07月09日 4:08 下午
 * @description 描述
 */
public interface SgRoleService {
    ResultVO<Object> getRoleList();

    ResultVO<Map<String, Object>> putRoleToUser(Integer roleCode, Integer userId, Integer version, String tran);
}
