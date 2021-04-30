package com.github.cloudgyb.questionnaire.modules.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.cloudgyb.questionnaire.common.annotation.SysLog;
import com.github.cloudgyb.questionnaire.common.utils.Constant;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.common.validator.ValidatorUtils;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysRoleEntity;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysRoleMenuService;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author geng
 */
@Api(tags = "系统角色管理")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    private final SysRoleService sysRoleService;
    private final SysRoleMenuService sysRoleMenuService;

    public SysRoleController(SysRoleService sysRoleService,
                             SysRoleMenuService sysRoleMenuService) {
        this.sysRoleService = sysRoleService;
        this.sysRoleMenuService = sysRoleMenuService;
    }

    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public R list() {
        List<SysRoleEntity> list = sysRoleService.list();
        return R.ok(list);
    }

    /**
     * 角色列表
     */
    @GetMapping("/listPage")
    @RequiresPermissions("sys:role:list")
    public R page(@RequestParam Map<String, Object> params) {
        //如果不是超级管理员，则只查询自己创建的角色列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }
        IPage<SysRoleEntity> page = sysRoleService.queryPage(params);
        return R.ok(page);
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public R info(@PathVariable("roleId") Long roleId) {
        SysRoleEntity role = sysRoleService.getById(roleId);
        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);
        return R.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public R save(@RequestBody SysRoleEntity role) {
        String remark = role.getRemark();
        if (remark != null && remark.length() > 100) {
            return R.error("角色描述不能超过100个字符！");
        }
        ValidatorUtils.validateEntity(role);
        role.setCreateUserId(getUserId());
        sysRoleService.saveRole(role);
        return R.ok();
    }

    /**
     * 修改角色
     */
    @SysLog("修改角色")
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    public R update(@RequestBody SysRoleEntity role) {
        String remark = role.getRemark();
        if (remark != null && remark.length() > 100) {
            return R.error("角色描述不能超过100个字符！");
        }
        ValidatorUtils.validateEntity(role);
        role.setCreateUserId(getUserId());
        sysRoleService.update(role);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public R delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);
        return R.ok();
    }

    /**
     * 获取角色菜单Id列表
     */
    @GetMapping("/menu/list")
    @RequiresPermissions("sys:role:info")
    public R menuIds(@RequestParam Long roleId) {
        SysRoleEntity role = sysRoleService.getById(roleId);
        if (role == null)
            return R.error("角色不存在！");
        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        return R.ok(menuIdList);
    }

    @PostMapping("/menu/save")
    public R saveMenuIds(@RequestParam Long roleId, @RequestBody Long[] menuIds) {
        sysRoleMenuService.saveOrUpdate(roleId, Arrays.asList(menuIds));
        return R.ok();
    }
}
