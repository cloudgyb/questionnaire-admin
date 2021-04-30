package com.github.cloudgyb.questionnaire.modules.sys.controller;

import com.github.cloudgyb.questionnaire.common.annotation.SysLog;
import com.github.cloudgyb.questionnaire.common.exception.RRException;
import com.github.cloudgyb.questionnaire.common.utils.Constant;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.common.validator.ValidatorUtils;
import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.common.validator.group.UpdateGroup;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysMenuEntity;
import com.github.cloudgyb.questionnaire.modules.sys.service.ShiroService;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysMenuService;
import com.github.cloudgyb.questionnaire.modules.sys.vo.MenuTreeNodeVO;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author Mark
 * @author geng
 */
@Api(tags = "系统菜单管理")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
    private final SysMenuService sysMenuService;
    private final ShiroService shiroService;

    public SysMenuController(SysMenuService sysMenuService,
                             ShiroService shiroService) {
        this.sysMenuService = sysMenuService;
        this.shiroService = shiroService;
    }

    /**
     * 导航菜单
     */
    @GetMapping("/nav")
    public R nav() {
        List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
        Set<String> permissions = shiroService.getUserPermissions(getUserId());
        R ok = R.ok();
        ok.put("menuList", menuList);
        return ok.put("permissions", permissions);
    }

    /**
     * 菜单树
     */
    @GetMapping("/tree")
    @RequiresPermissions("sys:menu:list")
    public R tree() {
        List<MenuTreeNodeVO> menuTree = sysMenuService.getMenuTree();
        return R.ok(menuTree);
    }

    @SysLog("添加菜单")
    @PostMapping("/add")
    @RequiresPermissions("sys:menu:add")
    public R add(@RequestBody MenuTreeNodeVO menu) {
        ValidatorUtils.validateEntity(menu, AddGroup.class);
        verifyMenu(menu);
        return sysMenuService.addMenu(menu);
    }


    @SysLog("修改菜单")
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public R save(@RequestBody MenuTreeNodeVO menu) {
        ValidatorUtils.validateEntity(menu, UpdateGroup.class);
        verifyMenu(menu);
        return sysMenuService.updateMenu(menu);
    }

    /**
     * 删除
     */
    @SysLog("删除菜单")
    @PostMapping("/delete/{menuId}")
    @RequiresPermissions("sys:menu:delete")
    public R delete(@PathVariable("menuId") long menuId) {
        return sysMenuService.deleteMenu(menuId);
    }

    /**
     * 验证菜单参数是否正确
     */
    private void verifyMenu(MenuTreeNodeVO menu) {
        if (StringUtils.isBlank(menu.getLabel())) {
            throw new RRException("菜单名称不能为空");
        }

        if (menu.getParentId() == null) {
            throw new RRException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RRException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new RRException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new RRException("上级菜单只能为菜单类型");
            }
        }
    }
}
