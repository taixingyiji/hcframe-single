package com.taixingyiji.single.module.auth.service.impl;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.common.utils.JudgeException;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.data.module.DataMap;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.single.module.auth.service.RoleGroupServie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleGroupService
 * @date 2021年04月02日 10:10 上午
 * @description 描述
 */
@Service
public class RoleGroupServiceImpl implements RoleGroupServie {

    private static final String PK_ID = "GROUP_ID";
    private static final String TABLE_NAME = "OS_SYS_ROLE_GROUP";
    private static final String ROLE_ID = "ROLE_ID";
    private static final String OS_REL_GROUP_ROLE = "OS_REL_GROUP_ROLE";
    private static final String ROLE_GROUP_ID = "ROLE_GROUP_ID";
    private static final String COMMA = ",";
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(TABLE_NAME).tablePk(PK_ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;

    public RoleGroupServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                           TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }


    @Override
    public ResultVO<Map<String,Object>> add(Map<String, Object> roleGroup) {
        return tableService.saveWithDate(TABLE_INFO,roleGroup);
    }

    @Override
    public ResultVO<Map<String, Object>> update(Map<String, Object> roleGroup, Integer version) {
        return tableService.updateWithDate(TABLE_INFO, roleGroup, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Integer> delete(String ids) {
        tableService.logicDelete(TABLE_INFO, ids);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getList(String data, WebPageInfo webPageInfo) {
        return ResultVO.getSuccess(tableService.searchSingleTables(data, TABLE_INFO, webPageInfo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> bind(Integer roleGroupId, String roleIds) {
        JudgeException.isNull(roleGroupId,"roleGroupId 不能为空");
        baseMapper.deleteByCondition(OS_REL_GROUP_ROLE, Condition.creatCriteria().andEqual(PK_ID,roleGroupId).build());
        if (StringUtils.isEmpty(roleIds)) {
            return ResultVO.getSuccess();
        }
        for (String str : roleIds.split(COMMA)) {
            Integer roleId = Integer.parseInt(str);
            DataMap<Object> dataMap = DataMap
                    .builder()
                    .pkName(ROLE_GROUP_ID)
                    .tableName(OS_REL_GROUP_ROLE)
                    .add(ROLE_ID,roleId)
                    .add(PK_ID,roleGroupId)
                    .build();
            baseMapper.save(dataMap);
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<Object> getRoles(Integer groupId) {
        JudgeException.isNull(groupId,"roleGroupId 不能为空！");
        Map<String, Object> map = new HashMap<>(1);
        map.put(PK_ID, groupId);
        List<Map<String, Object>> list = baseMapper.selectByEqual(OS_REL_GROUP_ROLE,map);
        return ResultVO.getSuccess(list);
    }

    @Override
    public ResultVO<Object> getAll() {
        return ResultVO.getSuccess( baseMapper.selectByCondition(TABLE_NAME, Condition.creatCriteria().andEqual("DELETED", 1).build()));
    }
}
