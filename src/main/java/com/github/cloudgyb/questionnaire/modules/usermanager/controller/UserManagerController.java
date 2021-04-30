package com.github.cloudgyb.questionnaire.modules.usermanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.cloudgyb.questionnaire.common.annotation.SysLog;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.common.validator.ValidatorUtils;
import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.modules.usermanager.controller.form.UserForm;
import com.github.cloudgyb.questionnaire.modules.usermanager.entity.User;
import com.github.cloudgyb.questionnaire.modules.usermanager.service.UserManagerService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @author cloudgyb
 * 2021/4/13 17:08
 */
@Api(tags = "调查问卷WEB APP用户管理模块")
@RestController
@RequestMapping("/app/user")
public class UserManagerController {
    private final UserManagerService userManagerService;

    UserManagerController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @GetMapping("/list")
    public R listUser() {
        List<User> list = userManagerService.list();
        return R.ok(list);
    }

    @GetMapping("/pageList")
    public R listPageUser(@RequestParam Map<String, Object> params) {
        IPage<User> userIPage = userManagerService.pageList(params);
        return R.ok(userIPage);
    }

    @PostMapping("/add")
    @SysLog("添加用户")
    public R addUser(@RequestBody UserForm userForm) {
        ValidatorUtils.validateEntity(userForm, AddGroup.class);
        return userManagerService.addUser(userForm);
    }

    @SysLog("切换用户锁定状态")
    @PostMapping("/switchLock")
    public R lockUser(@RequestParam Long userId) {
        return userManagerService.switchLockUser(userId);
    }

}
