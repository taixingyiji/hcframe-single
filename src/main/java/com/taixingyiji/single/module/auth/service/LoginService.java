package com.taixingyiji.single.module.auth.service;

import com.taixingyiji.base.common.ResultVO;

/**
 * @author lhc
 * @version 1.0
 * @className LoginService
 * @date 2021年07月06日 9:35 上午
 * @description 描述
 */
public interface LoginService {

    ResultVO<Object> rqLogin(String loginName, String password);

    ResultVO<Object> wxLogin(String code, String rawData);

    ResultVO<Object> getUserinfo();

    ResultVO<Object> getUserCertInfo(String loginName);

    ResultVO<Object> smsLogin(String code, String phone);
}
