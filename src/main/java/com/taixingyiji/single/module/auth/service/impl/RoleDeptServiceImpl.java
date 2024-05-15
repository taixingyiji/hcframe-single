package com.taixingyiji.single.module.auth.service.impl;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.utils.JudgeException;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.single.module.auth.service.RoleDeptService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Li Xinlei
 * @version 1.0
 * @className RoleDeptServiceImpl
 * @date 2021-4-20 17:24:17
 * @description 描述
 */
@Service
public class RoleDeptServiceImpl implements RoleDeptService {

    private static final String OS_REL_DEPT_ROLE = "OS_REL_DEPT_ROLE";
    private static final String DEPT_GROUP_ID = "DEPT_GROUP_ID";
    private static final String DEPT_ROLE_ID = "DEPT_ROLE_ID";
    private static final String OS_REL_DEPT_GROUP = "OS_REL_DEPT_GROUP";
    private static final String COMMA = ",";

    final BaseMapper baseMapper;

    final TableService tableService;

    public RoleDeptServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                               TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> roleDeptBind(String deptId, String roleIds) {
        JudgeException.isNull(deptId,"deptId 不能为空");
        OsSysTable osSysTable = OsSysTable.builder().tableName(OS_REL_DEPT_ROLE).tablePk(DEPT_ROLE_ID).build();
        Condition condition = Condition.creatCriteria().andEqual("DEPT_ID",deptId).build();
        baseMapper.deleteByCondition(OS_REL_DEPT_ROLE, condition);
        if (!StringUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds.split(COMMA)) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("DEPT_ID", deptId.replaceAll("\"",""));
                map.put("ROLE_ID", Integer.parseInt(roleId));
                tableService.save(osSysTable, map);
            }
        }
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> roleGroupBind(String deptId, String groupIds) {
        JudgeException.isNull(deptId,"deptId 不能为空");
        Condition condition = Condition.creatCriteria().andEqual("DEPT_ID",deptId).build();
        baseMapper.deleteByCondition(OS_REL_DEPT_GROUP, condition);
        OsSysTable osSysTable = OsSysTable.builder().tableName(OS_REL_DEPT_GROUP).tablePk(DEPT_GROUP_ID).build();
        if (!StringUtils.isEmpty(groupIds)) {
            for (String groupId : groupIds.split(COMMA)) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("DEPT_ID", deptId.replaceAll("\"",""));
                map.put("GROUP_ID", groupId);
                tableService.save(osSysTable, map);
            }
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<Object> getDeptRole(String deptId) {
        Condition condition = Condition.creatCriteria().andEqual("DEPT_ID",deptId).build();
        List<Map<String,Object>> list = baseMapper.selectByCondition(OS_REL_DEPT_ROLE, condition);
        return ResultVO.getSuccess(list);
    }

    @Override
    public ResultVO<Object> getDeptGroup(String deptId) {
        Condition condition = Condition.creatCriteria().andEqual("DEPT_ID",deptId).build();
        List<Map<String,Object>> list = baseMapper.selectByCondition(OS_REL_DEPT_GROUP, condition);
        return ResultVO.getSuccess(list);
    }

}
