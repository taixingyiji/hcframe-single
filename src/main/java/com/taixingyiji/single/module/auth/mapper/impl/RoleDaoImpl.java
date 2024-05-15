package com.taixingyiji.single.module.auth.mapper.impl;

import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.single.module.auth.mapper.RoleDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleDaoImpl
 * @date 2021年04月02日 4:23 下午
 * @description 描述
 */
@Component
public class RoleDaoImpl implements RoleDao {

    final BaseMapper baseMapper;


    public RoleDaoImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }


    @Override
    public List<Map<String, Object>> getAllList() {
        String sql = "SELECT ROLE_ID,ROLE_NAME FROM OS_SYS_ROLE";
        return baseMapper.selectSql(sql);
    }
}
