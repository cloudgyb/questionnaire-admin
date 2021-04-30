package com.github.cloudgyb.questionnaire.modules.sys.form;

import lombok.Data;

/**
 * 修改密码表单
 *
 * @author Mark
 */
@Data
public class PasswordForm {
    /**
     * 原密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;

}
