package com.github.cloudgyb.questionnaire.modules.usermanager.controller.form;

import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户表单
 *
 * @author cloudgyb
 * 2021/3/17 10:43
 */
@Data
public class UserForm {
    @NotBlank(message = "用户名不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    private String username;
    @NotBlank(message = "密码不能为空！", groups = AddGroup.class)
    private String password;
    private String realName;
    private String phone;
    private String email;
    private int age;
    private int sex;
}
