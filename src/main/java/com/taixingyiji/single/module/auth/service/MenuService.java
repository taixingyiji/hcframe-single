package com.taixingyiji.single.module.auth.service;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author wewe
 * @date 2021年4月14日
 * @description 功能级权限增删改查
 */
public interface MenuService {

	ResultVO<Object> addMenu(Map<String, Object> data);

	ResultVO<Object> deleteMenu(List<Long> ids);

	ResultVO<Map<String, Object>> updateMenu(Map<String, Object> data, Integer version);

	ResultVO<PageInfo<Map<String, Object>>> getMenuList(String data, WebPageInfo webPageInfo);

	ResultVO<Object> addRoleMenu(Long roleId, List<String> menuIds);

	ResultVO<Object> getMenuTree();

	ResultVO<Object> getSelectedMenu(Long roldId);

	ResultVO<Object> checkPath(Map<String, Object> data);

	ResultVO<Object> getOsList();

}
