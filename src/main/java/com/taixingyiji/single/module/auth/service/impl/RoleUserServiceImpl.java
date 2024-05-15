package com.taixingyiji.single.module.auth.service.impl;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.utils.JudgeException;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.single.module.auth.service.RoleUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author lhc
 * @version 1.0
 * @className RoleUserServiceImpl
 * @date 2021年04月15日 9:09 上午
 * @description 描述
 */
@Service
public class RoleUserServiceImpl implements RoleUserService {

    private static final String OS_REL_USER_ROLE = "OS_REL_USER_ROLE";
    private static final String USER_GROUP_ID = "USER_GROUP_ID";
    private static final String USER_ROLE_ID = "USER_ROLE_ID";
    private static final String OS_REL_USER_GROUP = "OS_REL_USER_GROUP";
    private static final String COMMA = ",";
    private static final String  GUOBO_ID= "-3858082048188003782";

    final BaseMapper baseMapper;

    final TableService tableService;


    public RoleUserServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                               TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> roleUserBind(String userId, String roleIds) {
        JudgeException.isNull(userId,"userId 不能为空");
        OsSysTable osSysTable = OsSysTable.builder().tableName(OS_REL_USER_ROLE).tablePk(USER_ROLE_ID).build();
        Condition condition = Condition.creatCriteria().andEqual("USER_ID",userId).build();
        baseMapper.deleteByCondition(OS_REL_USER_ROLE, condition);
        if (!StringUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds.split(COMMA)) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("USER_ID", userId.replaceAll("\"",""));
                map.put("ROLE_ID", Integer.parseInt(roleId));
                tableService.save(osSysTable, map);
            }
        }
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> roleGroupBind(String userId, String groupIds) {
        JudgeException.isNull(userId,"userId 不能为空");
        Condition condition = Condition.creatCriteria().andEqual("USER_ID",userId).build();
        baseMapper.deleteByCondition(OS_REL_USER_GROUP, condition);
        OsSysTable osSysTable = OsSysTable.builder().tableName(OS_REL_USER_GROUP).tablePk(USER_GROUP_ID).build();
        if (!StringUtils.isEmpty(groupIds)) {
            for (String groupId : groupIds.split(COMMA)) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("USER_ID", userId.replaceAll("\"",""));
                map.put("GROUP_ID", groupId);
                tableService.save(osSysTable, map);
            }
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<Object> getUserRole(String userId) {
        Map<String,Object> result = getUserAuth(userId, OS_REL_USER_ROLE);
        result.put("org", getOrgRoleList(userId));
        return ResultVO.getSuccess(result);
    }

    @Override
    public ResultVO<Object> getUserGroup(String userId) {
        Map<String, Object> result = getUserAuth(userId, OS_REL_USER_GROUP);
        result.put("org", getOrgRoleGroupList(userId));
        return ResultVO.getSuccess(result);
    }

    private Map<String,Object> getUserAuth(String userId, String osRelUserGroup) {
        Condition condition = Condition.creatCriteria().andEqual("USER_ID",userId).andEqual("DELETED",1).build();
        List<Map<String,Object>> list = baseMapper.selectByCondition(osRelUserGroup, condition);
        Map<String, Object> result = new HashMap<>(2);
        result.put("user", list);
        return result;
    }

    private Set<Map<String,Object>> getOrgRoleGroupList(String userId) {
        Set<Map<String, Object>> set = getGuoboOrgRole("OS_REL_DEPT_GROUP");
        String code = getUserOrgCode(userId);
        if (code.length() == 4) {
            getDepRoleList(set, code,"OS_REL_DEPT_GROUP");
        } else {
            getDepRoleList(set, code,"OS_REL_DEPT_GROUP");
            getDepRoleList(set,code.substring(0,4),"OS_REL_DEPT_GROUP");
        }
        return set;
    }

    /**
     * @author lhc
     * @description // 获取用户所属机构编码
     * @date 2:53 下午 2021/5/19
     * @params [userId]
     * @return java.lang.String
     **/
    private String getUserOrgCode(String userId) {
        Condition condition = Condition.creatCriteria().andEqual("ID",userId).andEqual("DELETED",1).build();
        Map<String,Object> user = baseMapper.selectOneByCondition("GB_CAS_MEMBER", condition);
        return  (String) user.get("DEPT_CODE");
    }

    /**
     * @author lhc
     * @description // 获取国博机构角色
     * @date 2:53 下午 2021/5/19
     * @params []
     * @return java.util.Set<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    private Set<Map<String,Object>> getGuoboOrgRole(String tableName) {
        Condition deptCondition = Condition.creatCriteria().andEqual("DEPT_ID",GUOBO_ID).andEqual("DELETED",1).build();
        List<Map<String,Object>>  guoboList= baseMapper.selectByCondition(tableName, deptCondition);
        return new HashSet<>(guoboList);
    }
    /**
     * @author lhc
     * @description // 获取机构及父级机构所拥有的权限
     * @date 2:55 下午 2021/5/19
     * @params [userId]
     * @return java.util.Set<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    private Set<Map<String, Object>> getOrgRoleList(String userId) {
        Set<Map<String, Object>> set = getGuoboOrgRole("OS_REL_DEPT_ROLE");
        String code = getUserOrgCode(userId);
        if (code.length() == 4) {
            getDepRoleList(set, code,"OS_REL_DEPT_ROLE");
        } else {
            getDepRoleList(set, code,"OS_REL_DEPT_ROLE");
            getDepRoleList(set,code.substring(0,4),"OS_REL_DEPT_ROLE");
        }
        List<Object> objectList = getRoleByGroup(userId);
        if (objectList != null && objectList.size() > 0) {
            Condition condition = Condition.creatCriteria().andIn("GROUP_ID",getRoleByGroup(userId)).build();
            List<Map<String,Object>> list = baseMapper.selectByCondition("OS_REL_GROUP_ROLE", condition);
            set.addAll(list);
        }
        return set;
    }

    private List<Object> getRoleByGroup(String userId) {
        Condition condition = Condition.creatCriteria().andEqual("USER_ID",userId).andEqual("DELETED",1).build();
        List<Map<String,Object>> list = baseMapper.selectByCondition(OS_REL_USER_GROUP, condition);
        Set<Map<String, Object>> set = new HashSet<>(list);
        set.addAll(getOrgRoleGroupList(userId));
        List<Object> objectList = new ArrayList<>();
        for (Map<String, Object> map : set) {
            objectList.add(map.get("GROUP_ID"));
        }
        return objectList;
    }

    /**
     * @author lhc
     * @description // 获取机构所绑定的角色
     * @date 2:54 下午 2021/5/19
     * @params [set, code]
     * @return void
     **/
    private void getDepRoleList(Set<Map<String, Object>> set, String code,String tableName) {
        Condition deptCondition;
        Map<String, Object> org = baseMapper.selectOneByCondition("GB_CAS_DEPT", Condition.creatCriteria().andEqual("CODE", code).andEqual("DELETED", 1).build());
        if (org != null && !org.isEmpty()) {
            deptCondition = Condition.creatCriteria().andEqual("DEPT_ID", org.get("ID")).andEqual("DELETED", 1).build();
            List<Map<String, Object>> roleList = baseMapper.selectByCondition(tableName, deptCondition);
            set.addAll(roleList);
        }
    }
}
