package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.utils.Constant;
import com.github.cloudgyb.questionnaire.common.utils.MapUtils;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysMenuDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysMenuEntity;
import com.github.cloudgyb.questionnaire.modules.sys.vo.MenuTreeNodeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 系统菜单service
 *
 * @author geng
 * 2021/04/24 18:09
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuDao, SysMenuEntity> {
    private final SysUserService sysUserService;
    private final SysRoleMenuService sysRoleMenuService;

    public SysMenuService(SysUserService sysUserService,
                          SysRoleMenuService sysRoleMenuService) {
        this.sysUserService = sysUserService;
        this.sysRoleMenuService = sysRoleMenuService;
    }

    /**
     * 所有菜单List
     */
    public List<SysMenuEntity> listAll() {
        List<SysMenuEntity> menuList = this.list();
        for (SysMenuEntity sysMenuEntity : menuList) {
            SysMenuEntity parentMenuEntity = this.getById(sysMenuEntity.getParentId());
            if (parentMenuEntity != null) {
                sysMenuEntity.setParentName(parentMenuEntity.getName());
            }
        }
        return menuList;
    }

    /**
     * 获取菜单树
     */
    public List<MenuTreeNodeVO> getMenuTree() {
        ArrayList<MenuTreeNodeVO> menuTreeRoot = new ArrayList<>();
        List<SysMenuEntity> menuEntities = this.listAll();
        MenuTreeNodeVO menuTopNode = new MenuTreeNodeVO();
        menuTopNode.setId(0L);
        menuTopNode.setLabel("系统菜单");
        menuTopNode.setType(-1);
        menuTopNode.setOpen(true);
        buildTree(menuEntities, Collections.singletonList(menuTopNode));
        menuTreeRoot.add(menuTopNode);
        return menuTreeRoot;
    }

    /**
     * 递归构建菜单树
     *
     * @param menus     菜单实体列表
     * @param menuNodes 当前需要构建的菜单几点
     */
    private void buildTree(List<SysMenuEntity> menus, List<MenuTreeNodeVO> menuNodes) {
        if (menuNodes == null)
            return;
        menuNodes.sort(Comparator.comparingInt(MenuTreeNodeVO::getOrderNum));
        menuNodes.forEach(e -> {
            Long id = e.getId();
            ArrayList<MenuTreeNodeVO> children = new ArrayList<>();
            menus.forEach(e1 -> {
                if (id.equals(e1.getParentId())) {
                    MenuTreeNodeVO vo = menuEntityToMenuTreeNode(e1);
                    children.add(vo);
                }
            });
            e.setChildren(children);
            buildTree(menus, children);
        });
    }

    /**
     * 将SysMenuEntity转换成MenuTreeNodeVO
     */
    private MenuTreeNodeVO menuEntityToMenuTreeNode(SysMenuEntity e) {
        MenuTreeNodeVO node = new MenuTreeNodeVO();
        node.setId(e.getMenuId());
        node.setLabel(e.getName());
        node.setParentId(e.getParentId());
        node.setOpen(e.getOpen());
        node.setOrderNum(e.getOrderNum());
        node.setIcon(e.getIcon());
        node.setType(e.getType());
        node.setUrl(e.getUrl());
        node.setPerms(e.getPerms());
        node.setChildren(null);
        return node;
    }

    /**
     * 添加菜单
     */
    @Transactional
    public R addMenu(MenuTreeNodeVO menu) {
        SysMenuEntity menuEntity = new SysMenuEntity();
        menuTreeNodeToMenuEntity(menu, menuEntity);
        this.save(menuEntity);
        MenuTreeNodeVO vo = menuEntityToMenuTreeNode(menuEntity);
        return R.ok(vo);
    }

    /**
     * 更新菜单
     */
    @Transactional
    public R updateMenu(MenuTreeNodeVO menu) {
        SysMenuEntity menuEntity = this.getById(menu.getId());
        if (menuEntity == null)
            return R.error("菜单不存在，更新失败！");
        menuTreeNodeToMenuEntity(menu, menuEntity);
        boolean isSuccess = this.updateById(menuEntity);
        return isSuccess ? R.ok() : R.error("菜单更新失败！");
    }

    private void menuTreeNodeToMenuEntity(MenuTreeNodeVO menu, SysMenuEntity menuEntity) {
        menuEntity.setParentId(menu.getParentId());
        menuEntity.setName(menu.getLabel());
        menuEntity.setUrl(menu.getUrl());
        menuEntity.setPerms(menu.getPerms());
        menuEntity.setType(menu.getType());
        menuEntity.setIcon(menu.getIcon());
        menuEntity.setOrderNum(menu.getOrderNum());
        menuEntity.setOpen(menu.isOpen());
    }

    /**
     * 删除菜单，不支持递归删除子菜单（如果递归删除子菜单太过暴力！）
     * 如果菜单下有子菜单则无法删除
     */
    @Transactional
    public R deleteMenu(long menuId) {
        if (menuId == 1) {
            return R.error("”系统管理“菜单无法删除！");
        }
        int childrenCount = this.count(new QueryWrapper<SysMenuEntity>().eq("parent_id", menuId));
        if (childrenCount > 0) {
            return R.error("该菜单下有子菜单，无法删除！");
        }
        this.delete(menuId);
        return R.ok();
    }

    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuEntity> menuList = queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        List<SysMenuEntity> userMenuList = new ArrayList<>();
        for (SysMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    public List<SysMenuEntity> queryListParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }

    public List<SysMenuEntity> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }

    public List<SysMenuEntity> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    public void delete(Long menuId) {
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        sysRoleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) {
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList) {
        List<SysMenuEntity> subMenuList = new ArrayList<>();

        for (SysMenuEntity entity : menuList) {
            //目录
            if (entity.getType() == Constant.MenuType.CATALOG.getValue()) {
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }


}
