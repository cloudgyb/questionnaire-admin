package com.github.cloudgyb.questionnaire.modules.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.cloudgyb.questionnaire.common.annotation.SysLog;
import com.github.cloudgyb.questionnaire.common.utils.Constant;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.common.validator.Assert;
import com.github.cloudgyb.questionnaire.common.validator.ValidatorUtils;
import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.common.validator.group.UpdateGroup;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysUserEntity;
import com.github.cloudgyb.questionnaire.modules.sys.form.PasswordForm;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysUserRoleService;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark
 * @author geng
 */
@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;

    public SysUserController(SysUserService sysUserService,
                             SysUserRoleService sysUserRoleService) {
        this.sysUserService = sysUserService;
        this.sysUserRoleService = sysUserRoleService;
    }

    /**
     * 当前登录用户所管理的所有用户列表，
     * 只有超级管理员才能查看所有用户，其他普通管理员只能查询出它创建的用户
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        //只有超级管理员，才能查看所有管理员列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }
        IPage<SysUserEntity> page = sysUserService.queryPage(params);
        return R.ok(page);
    }

    @PostMapping("/switchLock")
    @RequiresPermissions("sys:user:lock")
    public R switchLock(@RequestParam Long userId) {
        Long currLoginUserId = getUserId();
        if (currLoginUserId.equals(userId))
            return R.error("你不能锁定你自己！");
        return sysUserService.switchStatus(userId);
    }


    /**
     * 获取当前登录的用户信息
     */
    @GetMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    public R password(@RequestBody PasswordForm form) {
        Assert.isBlank(form.getNewPassword(), "新密码不为能空");
        //sha256加密
        String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.getById(userId);
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);
        return R.ok(user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);
        List<Long> roleIdList = user.getRoleIdList();
        if (roleIdList == null || roleIdList.size() == 0)
            return R.error("请选择用户角色！");
        user.setCreateUserId(getUserId());
        sysUserService.saveUser(user);
        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);
        List<Long> roleIdList = user.getRoleIdList();
        if (roleIdList == null || roleIdList.size() == 0)
            return R.error("请选择用户角色！");
        user.setCreateUserId(getUserId());
        return sysUserService.update(user);
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }
        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }
        sysUserService.deleteBatch(userIds);
        return R.ok();
    }
}
