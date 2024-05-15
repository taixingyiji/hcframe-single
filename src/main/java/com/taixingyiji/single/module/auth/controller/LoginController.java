package com.taixingyiji.single.module.auth.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.module.shiro.service.ShiroService;
import com.taixingyiji.single.common.config.WxClient;
import com.taixingyiji.single.common.log.annotate.LoginAnno;
import com.taixingyiji.single.module.auth.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhc
 * @version 1.0
 * @className LoginController
 * @date 2021年07月06日 9:34 上午
 * @description 描述
 */
@RequestMapping("cert")
@RestController
@Api(tags = "证书登录")
public class LoginController {

    final LoginService loginService;
    final ShiroService shiroService;
    final WxClient wxClient;

    public LoginController(LoginService loginService,
                           ShiroService shiroService,
                           WxClient wxClient) {
        this.loginService = loginService;
        this.shiroService = shiroService;
        this.wxClient = wxClient;
    }

    @PostMapping("rqLogin")
//    @LoginAnno(operateType = "登录")
    public ResultVO<Object> rqLogin(String loginName, String password) {
//        return ResultVO.getSuccess();
        return loginService.rqLogin(loginName, password);
    }

    @ApiOperation(value = "手机号短信登录")
    @PostMapping("smsLogin")
//    @LoginAnno(operateType = "登录")
    public ResultVO<Object> smsLogin(String code, String phone) {
//        return ResultVO.getSuccess();
        return loginService.smsLogin(code, phone);
    }

    @ApiOperation(value = "微信登录")
    @PostMapping("wxLogin")
    @LoginAnno(operateType = "微信登录")
    public ResultVO<Object> wxLogin(String code, String rawData) {
        return loginService.wxLogin(code, rawData);
    }

    @PostMapping("logout")
    public ResultVO<Object> logout(HttpServletRequest request) {
        return ResultVO.getSuccess(shiroService.logout(request.getHeader("X-Access-Token")));
    }

    @GetMapping("userinfo")
    public ResultVO<Object> getUserinfo() {
        return loginService.getUserinfo();
    }

    @GetMapping("cert")
    public ResultVO<Object> getUserCertInfo(String loginName){
        return loginService.getUserCertInfo(loginName);
    }
}
