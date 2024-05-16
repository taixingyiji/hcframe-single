package com.taixingyiji.single.module.auth.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.common.utils.JudgeException;
import com.taixingyiji.base.common.utils.MD5Util;
import com.taixingyiji.base.common.utils.TokenProccessor;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.shiro.service.ShiroService;
import com.taixingyiji.redis.RedisUtil;
import com.taixingyiji.single.common.config.RedisConstant;
import com.taixingyiji.single.common.config.WxClient;
import com.taixingyiji.single.common.utils.HexUtil;
import com.taixingyiji.single.module.auth.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.taixingyiji.single.common.utils.LoginUtil.isPhone;


/**
 * @author lhc
 * @version 1.0
 * @className LoginServiceImpl
 * @date 2021年07月06日 9:35 上午
 * @description 描述
 */
@Service
public class LoginServiceImpl implements LoginService {

    final ShiroService shiroService;
    final BaseMapper baseMapper;
    final WxClient wxClient;

    private static final String AN = "SG_OP_IDEN";
    private static final String USER_TABLE = "FT_USER";
    private static final String ERR_MSG = "errmsg";
    private static final String ERR_CODE = "errcode";
    private static final int NO_CERT = 2;
    private static final int NO_INVITE_CODE = 1;
    private static final int FIRST_LOGIN = 3;

    final RedisUtil redisUtil;


    /**
     * 用户失效时间
     */
    public static int EXPIRE;

    public LoginServiceImpl(ShiroService shiroService,
                            @Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                            RedisUtil redisUtil,
                            WxClient wxClient) {
        this.shiroService = shiroService;
        this.baseMapper = baseMapper;
        this.wxClient = wxClient;
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setHost(FrameConfig config) {
        LoginServiceImpl.EXPIRE = config.getLoginTimeout() * 3600 * 1000;
    }



    @Override
    public ResultVO rqLogin(String loginName, String password) {
        JudgeException.isNull(loginName, "登录名不能为空");
        JudgeException.isNull(password, "密码不能为空");
        Map<String, Object> user = baseMapper.selectOneByCondition(USER_TABLE, Condition.creatCriteria().andEqual("USERNAME", loginName).build());
        if (user == null) {
            throw new ServiceException("用户不存在");
        } else {
            if (Integer.parseInt(String.valueOf(user.get("ENABLED"))) != 1) {
                throw new ServiceException("用户已被禁用，请联系管理员");
            } else {
                if (MD5Util.isEqual(password, (String) user.get("PASSWORD"))) {
//                if (password.equals(user.get("PASSWORD"))) {
                    TokenProccessor tokenProccessor = TokenProccessor.getInstance();
                    //生成一个token
                    String token = tokenProccessor.makeToken();
                    //过期时间
                    Date now = new Date();
                    Date expireTime = new Date(now.getTime() + EXPIRE);
//                    return shiroService.createToken("USER_ID", token, expireTime);
                    return shiroService.createToken(String.valueOf(user.get("USER_ID")), token, expireTime);
                } else {
                    throw new ServiceException("账户或密码不正确");
                }
            }
        }
    }

    @Override
    public ResultVO<Object> wxLogin(String code, String rawData) {
        JSONObject jsonObject = wxClient.auth(code);
        if (jsonObject.get(ERR_CODE) != null) {
            return ResultVO.getFailed((String) jsonObject.get(ERR_MSG));
        }
        JSONObject raw = JSONUtil.parseObj(rawData);
        String openid = (String) jsonObject.get("openid");
        String session_key = (String) jsonObject.get("session_key");
        Map<String, Object> user = baseMapper.selectOneByCondition(USER_TABLE, Condition.creatCriteria().andEqual("WX_CODE", openid).build());
        if (user == null) {
            user = new HashMap<>(1);
            user.put("CREATE_TIME", new Date());
            user.put("UPDATE_TIME", new Date());
            user.put("WX_CODE", openid);
            user.put("NICK_NAME", raw.get("nickName"));
            user.put("AVATAR", raw.get("avatarUrl"));
            String creditCode = NanoIdUtils.randomNanoId();
            user.put("CREDIT_CODE", creditCode);
            int i = baseMapper.save(USER_TABLE, "USER_ID", user);
            if (i < 1) {
                return ResultVO.getFailed("登录失败");
            }
        } else {
            if (Integer.parseInt(String.valueOf(user.get("ENABLED"))) != 1) {
                return ResultVO.getFailed("用户已禁用");
            }
        }
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + EXPIRE);
        ResultVO result = shiroService.createToken(String.valueOf(user.get("USER_ID")), "WX_" + session_key, expireTime);
        if (result.getCode() == 0) {
            Map<String, Object> map = (Map<String, Object>) result.getData();
            user = baseMapper.selectByPk(USER_TABLE, "USER_ID", user.get("USER_ID"));
            if (StringUtils.isEmpty(user.get("CERT"))) {
                map.put("isFirst", NO_CERT);
            } else {
                map.put("isFirst", 0);
            }
            map.put("CREDIT_CODE", user.get("CREDIT_CODE"));
            return ResultVO.getSuccess(map);
        } else {
            return ResultVO.getFailed("登录失败");
        }
    }

    @Override
    public ResultVO<Object> getUserinfo() {
        Map<String, Object> user = (Map<String, Object>) SecurityUtils.getSubject().getPrincipal();
//        SysCert sysCert = SystemUserUtil.getSysCert();
//        user.put("CHAIN_CODE_VERSION", sysCert.getChainCodeVersion());
//        String phone = (String) user.get("PHONE");
//        if (!StringUtils.isEmpty(phone)) {
//            String result = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
//            user.put("PHONE", result);
//        }
//        if (StringUtils.isEmpty(user.get("IDEN_CODE"))) {
//            user.put("isVerify", 0);
//        } else {
//            user.put("isVerify", 1);
//        }
//        user.remove("IDEN_CODE");
//        user.remove("REAL_NAME");
        return ResultVO.getSuccess(user);
    }

    @Override
    public ResultVO<Object> getUserCertInfo(String loginName) {
        JudgeException.isNull(loginName, "loginName 不能为空");
        Condition condition;
        if (loginName.contains("@")) {
            condition = Condition.creatCriteria().andEqual("EMAIL", loginName).andEqual("DELETED", 1).build();
        } else {
            condition = Condition.creatCriteria().andEqual("CREDIT_CODE", loginName).andEqual("DELETED", 1).build();
        }
        Map<String, Object> user = baseMapper.selectOneByCondition(USER_TABLE, Condition.creatCriteria().andEqual("CREDIT_CODE", loginName).build());
        JudgeException.isNull(user, "用户不存在");
        Map<String, Object> result = new HashMap<>(3);
        result.put("cert", user.get("CERT"));
        result.put("certName", user.get("CERT_NAME"));
        result.put("creditCode", user.get("CREDIT_CODE"));
        return ResultVO.getSuccess(result);
    }

    @Override
    public ResultVO<Object> smsLogin(String code, String phone) {
        if (!isPhone(phone)) {
            return ResultVO.getFailed("电话号码校验失败");
        }
        int isFirst = 0;
        JudgeException.isNull(code, "code 不能为空");
        JudgeException.isNull(code, "phone 不能为空");
        String smsStr = (String) redisUtil.get(RedisConstant.SMS + phone);
        JudgeException.isNull(smsStr, "登录失败");
//        SmsEntity smsEntity = JSONUtil.toBean(smsStr,SmsEntity.class);
//        if (!code.equals(smsEntity.getCode())) {
//            return ResultVO.getFailed("验证码不正确");
//        }
        redisUtil.del(RedisConstant.SMS + phone);
        Map<String, Object> user = baseMapper.selectOneByCondition(USER_TABLE, Condition.creatCriteria().andEqual("PHONE", phone).build());
        if (user == null) {
            String handlePhone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
            user = new HashMap<>(1);
            user.put("CREATE_TIME", new Date());
            user.put("UPDATE_TIME", new Date());
            user.put("NICK_NAME", "用户" + handlePhone);
            String creditCode = NanoIdUtils.randomNanoId();
            user.put("CREDIT_CODE", creditCode);
            user.put("PHONE", phone);
            int i = baseMapper.save(USER_TABLE, "USER_ID", user);
            isFirst = FIRST_LOGIN;
            if (i < 1) {
                return ResultVO.getFailed("登录失败");
            }
        } else {
            if (Integer.parseInt(String.valueOf(user.get("ENABLED"))) != 1) {
                return ResultVO.getFailed("用户已禁用");
            }
        }
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + EXPIRE);
        ResultVO result = shiroService.createToken(String.valueOf(user.get("USER_ID")), NanoIdUtils.randomNanoId(), expireTime);
        if (result.getCode() == 0) {
            Map<String, Object> map = (Map<String, Object>) result.getData();
            user = baseMapper.selectByPk(USER_TABLE, "USER_ID", user.get("USER_ID"));
            map.put("isFirst", isFirst);
            map.put("CREDIT_CODE", user.get("CREDIT_CODE"));
            return ResultVO.getSuccess(map);
        } else {
            return ResultVO.getFailed("登录失败");
        }
    }
}
