package com.github.cloudgyb.questionnaire.modules.sys.form;

import lombok.Data;

/**
 * 登录表单
 *
 * @author Mark
 */
@Data
public class SysLoginForm {
    private String username;
    private String password;
    private String captcha;
    private String uuid;
}
