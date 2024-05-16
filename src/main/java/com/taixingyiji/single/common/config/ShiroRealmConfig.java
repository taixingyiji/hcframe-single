package com.taixingyiji.single.common.config;

import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.shiro.service.ShiroType;
import com.taixingyiji.base.module.shiro.service.SystemRealm;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lhc
 * @date 2021-02-05
 * @decription shiro 配置类
 */
@Component
public class ShiroRealmConfig implements SystemRealm {

    final BaseMapper baseMapper;

    public ShiroRealmConfig(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper
    ) {
        this.baseMapper = baseMapper;
    }

    /**
     * 根据用户信息注入权限
     *
     * @param user 用户信息
     * @return 权限信息
     */
    @Override
    public SimpleAuthorizationInfo setAuthoriztion(Object user) {
        Map<String, Object> map = (Map<String, Object>) user;
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        Set<String> set = authService.getUserAuth(String.valueOf(map.get("USER_ID")));
//        for (String auth : set) {
//            simpleAuthorizationInfo.addStringPermission(auth);
//        }
        simpleAuthorizationInfo.addRole("admin");
        return simpleAuthorizationInfo;
    }

    /**
     * 根据用户Id查询用户信息并注入到shiro框架中
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public Object findByUserId(String userId) {
        Condition condition = Condition.creatCriteria().andEqual("USER_ID", userId).build();
        return baseMapper.selectByCondition("FT_USER", condition);
//        return baseMapper.selectByCondition("SG_OP_IDEN",condition);
    }

    /**
     * 配置拦截及放行路径
     *
     * @return 返回拦截及放行路径Map
     */
    @Override
    public LinkedHashMap<String, String> setShiroUrl() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/weixin/**", ShiroType.ANON);
        // 支付回调
        map.put("/payment/payCallback", ShiroType.ANON);
        // 用户登陆
        map.put("/ftUser/login", ShiroType.ANON);
        map.put("/ftUser/logout", ShiroType.ANON);
        map.put("/cert/login", ShiroType.ANON);
        map.put("/cert/logout", ShiroType.ANON);
        map.put("/cert/rqLogin", ShiroType.ANON);
        map.put("/cert/wxLogin", ShiroType.ANON);
        map.put("/cert/smsLogin", ShiroType.ANON);
        // 免责声明等内容
        map.put("/anOpLegal/getLegal", ShiroType.ANON);
        // 查询藏品库存
        map.put("/wxAntique/productCount", ShiroType.ANON);
        // 查询banner列表
        map.put("/anOpBanner/bannerList", ShiroType.ANON);
        // 发送验证码
        map.put("/sms/sendCode", ShiroType.ANON);
        // Vue静态资源
        map.put("/img/**", ShiroType.ANON);
        map.put("/static/**", ShiroType.ANON);
        map.put("/tinymce/**", ShiroType.ANON);
        map.put("/favicon.ico", ShiroType.ANON);
        map.put("/manifest.json", ShiroType.ANON);
        map.put("/robots.txt", ShiroType.ANON);
        map.put("/precache*", ShiroType.ANON);
        map.put("/service-worker.js", ShiroType.ANON);
        // swagger UI 静态资源
        map.put("/swagger-ui.html", ShiroType.ANON);
        map.put("/doc.html", ShiroType.ANON);
        map.put("/swagger-resources/**", ShiroType.ANON);
        map.put("/webjars/**", ShiroType.ANON);
        map.put("/v2/api-docs", ShiroType.ANON);
        map.put("/v2/api-docs-ext", ShiroType.ANON);
        map.put("/swagger/**", ShiroType.ANON);
        // druid 资源路径
        map.put("/druid/**", ShiroType.ANON);
        map.put("/rqOpUser/valid", ShiroType.ANON);
        // 检索藏品列表
        map.put("/anOpAntique/fulltext", ShiroType.ANON);
        // 检索是商品否卖完
        map.put("/anOpAntique/productCount", ShiroType.ANON);
        // 检索藏品信息
        map.put("/anOpAntique/one/**", ShiroType.ANON);
        // 检索藏品信息通过文物CODE
        map.put("/anOpAntique/oneByCode/**", ShiroType.ANON);
        // 检索藏品流转信息
        map.put("/anOpCirculation/getList", ShiroType.ANON);
        //校验文物ID是否存在
        map.put("/anOpAntique/antiqueIsExist", ShiroType.ANON);
        //文物用户校验
        map.put("/anOpAntique/antiqueIsUser", ShiroType.ANON);
        //获取区块列表
        map.put("/opBlock/**", ShiroType.ANON);
        //获取区块详细
        map.put("/opTransaction/**", ShiroType.ANON);
        //获取交易详细
        map.put("/datasubmitter/**", ShiroType.ANON);
        // 验证码
        map.put("/code/**", ShiroType.ANON);
        // 用户注册
        map.put("/personal/register", ShiroType.ANON);
        map.put("/personal/valid", ShiroType.ANON);
        map.put("/personal/validEmail", ShiroType.ANON);
        // 用户校验
        map.put("/validUser/**", ShiroType.ANON);
        map.put("/count/**", ShiroType.ANON);
        // 微信商品列表
        map.put("/wxAntique/fulltext", ShiroType.ANON);
        map.put("/anOpProduct/one/**", ShiroType.ANON);
//        map.put("/deck/**", ShiroType.ANON);

        // 其余路径均拦截
//        map.put("/**", ShiroType.ANON);
        map.put("/**", ShiroType.AUTH);
        return map;
    }
}


