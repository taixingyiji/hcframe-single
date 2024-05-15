package com.taixingyiji.single.module.auth.mapper.impl;

import com.taixingyiji.base.module.auth.entity.OsSysMenu;
import com.taixingyiji.base.module.data.module.*;
import com.taixingyiji.single.module.auth.mapper.AuthDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className AuthDaoImpl
 * @date 2021年04月25日 3:30 下午
 * @description 描述
 */
@Component
public class AuthDaoImpl implements AuthDao {

    final BaseMapper baseMapper;

    public AuthDaoImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    private static final Integer OS_ID = 8;

    @Override
    public List<Map<String, Object>> selectMenuList(OsSysMenu osSysMenu) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" SELECT OS_SYS_MENU.MENU_ID,\n" +
                "               OS_SYS_MENU.MENU_NAME,\n" +
                "               OS_SYS_MENU.PARENT_ID,\n" +
                "               OS_SYS_MENU.PATH,\n" +
                "               OS_SYS_MENU.COMPONENT,\n" +
                "               OS_SYS_MENU.IS_CACHE,\n" +
                "               OS_SYS_MENU.VISIBLE,\n" +
                "               OS_SYS_MENU.IS_FRAME,\n" +
                "               OS_SYS_MENU.MENU_TYPE,\n" +
                "               OS_SYS_MENU.ORDER_NUM,\n" +
                "               OS_SYS_MENU.MENU_STATUS,\n" +
                "               OS_SYS_MENU.PERMS,\n" +
                "               OS_SYS_MENU.AFFIX,\n" +
                "               OS_SYS_MENU.BREADCRUMB,\n" +
                "               OS_SYS_MENU.AlWAYSSHOW,\n" +
                "               OS_SYS_MENU.REMARK,\n" +
                "               OS_SYS_MENU.UPDATE_TIME,\n" +
                "               OS_SYS_MENU.CREATE_TIME,\n" +
                "               OS_SYS_MENU.ICON,\n" +
                "               OS_SYS_MENU.OS_ID,\n" +
                "               OS_SYS_MENU.VERSION,\n" +
                "               OS_SYS_MENU.DELETED\n" +
                "        FROM OS_SYS_MENU\n" +
                "        WHERE  DELETED = 1");
        if (!StringUtils.isEmpty(osSysMenu.getMenuName())) {
            stringBuilder.append(" AND MENU_NAME like '%").append(osSysMenu.getMenuName()).append("%' ");
        }
        if (!StringUtils.isEmpty(osSysMenu.getOsId())) {
            stringBuilder.append(" AND  OS_ID = ").append(osSysMenu.getOsId()).append(" ");
        }
        if (!StringUtils.isEmpty(osSysMenu.getStatus())) {
            stringBuilder.append("AND  MENU_STATUS = '").append(osSysMenu.getStatus()).append("' ");
        }
        stringBuilder.append(" ORDER BY OS_SYS_MENU.PARENT_ID ASC,\n" +
                "                 OS_SYS_MENU.ORDER_NUM ASC");
        return baseMapper.selectSql(stringBuilder.toString());
    }

    @Override
    public List<String> getUserRoleAuth(String userId) {
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("OS_REL_USER_ROLE")
                .field("OS_SYS_MENU.PATH")
                .join("OS_REL_ROLE_MENU")
                .on("ROLE_ID", "OS_REL_USER_ROLE", "ROLE_ID")
                .join("OS_SYS_MENU")
                .on("MENU_ID", "OS_REL_ROLE_MENU", "MENU_ID")
                .join("OS_SYS_ROLE")
                .on("ROLE_ID", "OS_REL_USER_ROLE", "ROLE_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_MENU.DELETED", 1)
                .andEqual("OS_SYS_MENU.MENU_STATUS", 1)
                .andEqual("OS_SYS_MENU.OS_ID", OS_ID)
                .andEqual("OS_REL_USER_ROLE.USER_ID", userId.replaceAll("\"", ""))
                .build();
        return getPaths(condition);
    }

    @Override
    public List<String> getUserSingleRoleAuth(String userId) {
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("SG_OP_IDEN")
                .field("OS_SYS_MENU.PATH")
                .join("OS_SYS_ROLE")
                .on("ROLE_CODE", "SG_OP_IDEN", "ROLE")
                .join("OS_REL_ROLE_MENU")
                .on("ROLE_ID", "OS_SYS_ROLE", "ROLE_ID")
                .join("OS_SYS_MENU")
                .on("MENU_ID", "OS_REL_ROLE_MENU", "MENU_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_MENU.DELETED", 1)
                .andEqual("SG_OP_IDEN.DELETED", 1)
                .andEqual("OS_SYS_MENU.MENU_STATUS", 1)
                .andEqual("OS_SYS_MENU.OS_ID", OS_ID)
                .andEqual("SG_OP_IDEN.USER_ID", userId.replaceAll("\"", ""))
                .build();
        return getPaths(condition);
    }

    @Override
    public List<String> getUserRoleGroupAuth(String userId) {
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("OS_REL_USER_GROUP")
                .field("OS_SYS_MENU.PATH")
                .join("OS_SYS_ROLE_GROUP")
                .on("GROUP_ID", "OS_REL_USER_GROUP", "GROUP_ID")
                .join("OS_REL_GROUP_ROLE")
                .on("GROUP_ID", "OS_REL_USER_GROUP", "GROUP_ID")
                .join("OS_SYS_ROLE")
                .on("ROLE_ID", "OS_REL_GROUP_ROLE", "ROLE_ID")
                .join("OS_REL_ROLE_MENU")
                .on("ROLE_ID", "OS_REL_GROUP_ROLE", "ROLE_ID")
                .join("OS_SYS_MENU")
                .on("MENU_ID", "OS_REL_ROLE_MENU", "MENU_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_MENU.DELETED", 1)
                .andEqual("OS_SYS_MENU.OS_ID", OS_ID)
                .andEqual("OS_SYS_MENU.MENU_STATUS", 1)
                .andEqual("OS_SYS_ROLE_GROUP.DELETED", 1)
                .andEqual("OS_REL_USER_GROUP.USER_ID", userId.replaceAll("\"", ""))
                .build();
        return getPaths(condition);
    }

    @Override
    public List<String> getOrgRoleAuth(String orgCode) {
        DataMap<Object> dataMap = DataMap.builder().tableName("GB_CAS_DEPT").fields("ID").build();
        Map<String, Object> org = baseMapper.selectOneByCondition(Condition.creatCriteria(dataMap).andEqual("CODE", orgCode).build());
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("OS_REL_DEPT_ROLE")
                .field("OS_SYS_MENU.PATH")
                .join("OS_SYS_ROLE")
                .on("ROLE_ID", "OS_REL_DEPT_ROLE", "ROLE_ID")
                .join("OS_REL_ROLE_MENU")
                .on("ROLE_ID", "OS_REL_DEPT_ROLE", "ROLE_ID")
                .join("OS_SYS_MENU")
                .on("MENU_ID", "OS_REL_ROLE_MENU", "MENU_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_MENU.DELETED", 1)
                .andEqual("OS_SYS_MENU.OS_ID", OS_ID)
                .andEqual("OS_SYS_MENU.MENU_STATUS", 1)
                .andEqual("OS_REL_DEPT_ROLE.DEPT_ID", org.get("ID")).build();
        return getPaths(condition);
    }

    @Override
    public List<String> getOrgGroupAuth(String orgCode) {
        DataMap<Object> dataMap = DataMap.builder().tableName("GB_CAS_DEPT").fields("ID").build();
        Map<String, Object> org = baseMapper.selectOneByCondition(Condition.creatCriteria(dataMap).andEqual("CODE", orgCode).build());
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("OS_REL_DEPT_GROUP")
                .field("OS_SYS_MENU.PATH")
                .join("OS_SYS_ROLE_GROUP")
                .on("GROUP_ID", "OS_REL_DEPT_GROUP", "GROUP_ID")
                .join("OS_REL_GROUP_ROLE")
                .on("GROUP_ID", "OS_REL_DEPT_GROUP", "GROUP_ID")
                .join("OS_SYS_ROLE")
                .on("ROLE_ID", "OS_REL_GROUP_ROLE", "ROLE_ID")
                .join("OS_REL_ROLE_MENU")
                .on("ROLE_ID", "OS_REL_GROUP_ROLE", "ROLE_ID")
                .join("OS_SYS_MENU")
                .on("MENU_ID", "OS_REL_ROLE_MENU", "MENU_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_MENU.DELETED", 1)
                .andEqual("OS_SYS_ROLE_GROUP.DELETED", 1)
                .andEqual("OS_SYS_MENU.OS_ID", OS_ID)
                .andEqual("OS_SYS_MENU.MENU_STATUS", 1)
                .andEqual("OS_REL_DEPT_GROUP.DEPT_ID", org.get("ID"))
                .build();
        return getPaths(condition);
    }

    @Override
    public Long getRoleOs(String userId) {
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("OS_REL_USER_ROLE")
                .field("count(OS_SYS_OS.OS_CODE)")
                .join("OS_SYS_ROLE")
                .on("ROLE_ID", "OS_REL_USER_ROLE", "ROLE_ID")
                .join("OS_REL_ROLE_OS")
                .on("ROLE_ID", "OS_SYS_ROLE", "ROLE_ID")
                .join("OS_SYS_OS")
                .on("OS_ID", "OS_REL_ROLE_OS", "OS_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_OS.DELETED", 1)
                .andEqual("OS_SYS_OS.ENABLED", 1)
                .andEqual("OS_SYS_OS.OS_ID", OS_ID)
                .andEqual("OS_REL_USER_ROLE.USER_ID", userId.replaceAll("\"", ""))
                .build();
        return baseMapper.count(condition);
    }

    @Override
    public Long getGroupOs(String userId) {
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("OS_REL_USER_GROUP")
                .field("count(OS_SYS_OS.OS_CODE)")
                .join("OS_SYS_ROLE_GROUP")
                .on("GROUP_ID", "OS_REL_USER_GROUP", "GROUP_ID")
                .join("OS_REL_GROUP_ROLE")
                .on("GROUP_ID", "OS_REL_USER_GROUP", "GROUP_ID")
                .join("OS_SYS_ROLE")
                .on("ROLE_ID", "OS_REL_GROUP_ROLE", "ROLE_ID")
                .join("OS_REL_ROLE_OS")
                .on("ROLE_ID", "OS_REL_GROUP_ROLE", "ROLE_ID")
                .join("OS_SYS_OS")
                .on("OS_ID", "OS_REL_ROLE_OS", "OS_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_OS.DELETED", 1)
                .andEqual("OS_SYS_OS.OS_ID", OS_ID)
                .andEqual("OS_SYS_OS.ENABLED", 1)
                .andEqual("OS_SYS_ROLE_GROUP.DELETED", 1)
                .andEqual("OS_REL_USER_GROUP.USER_ID", userId.replaceAll("\"", ""))
                .build();
        return baseMapper.count(condition);
    }

    @Override
    public Long getOrgOs(String orgCode) {
        DataMap<Object> dataMap = DataMap.builder().tableName("GB_CAS_DEPT").fields("ID").build();
        Map<String, Object> org = baseMapper.selectOneByCondition(Condition.creatCriteria(dataMap).andEqual("CODE", orgCode).build());
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("OS_REL_DEPT_ROLE")
                .field("count(OS_SYS_OS.OS_CODE)")
                .join("OS_SYS_ROLE")
                .on("ROLE_ID", "OS_REL_DEPT_ROLE", "ROLE_ID")
                .join("OS_REL_ROLE_OS")
                .on("ROLE_ID", "OS_REL_DEPT_ROLE", "ROLE_ID")
                .join("OS_SYS_OS")
                .on("OS_ID", "OS_REL_ROLE_OS", "OS_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_OS.DELETED", 1)
                .andEqual("OS_SYS_OS.OS_ID", OS_ID)
                .andEqual("OS_SYS_OS.ENABLED",1)
                .andEqual("OS_REL_DEPT_ROLE.DEPT_ID", org.get("ID")).build();
        return baseMapper.count(condition);
    }

    @Override
    public Long getOrgGroupOs(String orgCode) {
        DataMap<Object> dataMap = DataMap.builder().tableName("GB_CAS_DEPT").fields("ID").build();
        Map<String, Object> org = baseMapper.selectOneByCondition(Condition.creatCriteria(dataMap).andEqual("CODE", orgCode).build());
        SelectCondition selectCondition = SelectCondition
                .sqlJoinBuilder("OS_REL_DEPT_GROUP")
                .field("count(OS_SYS_OS.OS_CODE)")
                .join("OS_SYS_ROLE_GROUP")
                .on("GROUP_ID", "OS_REL_DEPT_GROUP", "GROUP_ID")
                .join("OS_REL_GROUP_ROLE")
                .on("GROUP_ID", "OS_REL_DEPT_GROUP", "GROUP_ID")
                .join("OS_SYS_ROLE")
                .on("ROLE_ID", "OS_REL_GROUP_ROLE", "ROLE_ID")
                .join("OS_REL_ROLE_OS")
                .on("ROLE_ID", "OS_REL_GROUP_ROLE", "ROLE_ID")
                .join("OS_SYS_OS")
                .on("OS_ID", "OS_REL_ROLE_OS", "OS_ID")
                .build();
        Condition condition = Condition.creatCriteria(selectCondition)
                .andEqual("OS_SYS_ROLE.DELETED", 1)
                .andEqual("OS_SYS_OS.DELETED", 1)
                .andEqual("OS_SYS_ROLE_GROUP.DELETED", 1)
                .andEqual("OS_SYS_OS.OS_ID", OS_ID)
                .andEqual("OS_SYS_OS.ENABLED",1)
                .andEqual("OS_REL_DEPT_GROUP.DEPT_ID", org.get("ID"))
                .build();
        return baseMapper.count(condition);
    }

    private List<String> getPaths(Condition condition) {
        List<Map<String, Object>> list = baseMapper.selectByCondition(condition);
        List<String> resultList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> objectMap : list) {
                resultList.add(String.valueOf(objectMap.get("PATH")));
            }
        }
        return resultList;
    }
}
