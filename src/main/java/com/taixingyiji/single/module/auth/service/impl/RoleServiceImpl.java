package com.taixingyiji.single.module.auth.service.impl;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.single.module.auth.mapper.RoleDao;
import com.taixingyiji.single.module.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className RoleServiceImpl
 * @date 2021年03月26日 9:42 上午
 * @description 描述
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final String PK_ID = "ROLE_ID";
    private static final String TABLE_NAME = "OS_SYS_ROLE";
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(TABLE_NAME).tablePk(PK_ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;

    final RoleDao roleDao;

    public RoleServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                           TableService tableService,
                           RoleDao roleDao) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
        this.roleDao = roleDao;
    }

    @Override
    public ResultVO<Object> addRole(Map<String, Object> role) {
        tableService.saveWithDate(TABLE_INFO, role);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<Map<String, Object>> updateRole(Map<String, Object> role, Integer version) {
        return tableService.updateWithDate(TABLE_INFO, role, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> deleteRole(String ids) {
        tableService.logicDelete(TABLE_INFO, ids);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getRoleList(String data, WebPageInfo webPageInfo) {
        return ResultVO.getSuccess(tableService.searchSingleTables(data, TABLE_INFO, webPageInfo));
    }

    @Override
    public ResultVO<Object> validCode(String code) {
        Condition condition = Condition.creatCriteria().andEqual("ROLE_CODE", code).andEqual("DELETED",1).build();
        Long i = baseMapper.count(TABLE_NAME, condition);
        if (i > 0L) {
            return ResultVO.getFailed("角色编码不能重复");
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<List<Map<String, Object>>> getAll() {
        return ResultVO.getSuccess(baseMapper.selectByCondition(TABLE_NAME, Condition.creatCriteria().andEqual("DELETED", 1).build()));
    }
}
