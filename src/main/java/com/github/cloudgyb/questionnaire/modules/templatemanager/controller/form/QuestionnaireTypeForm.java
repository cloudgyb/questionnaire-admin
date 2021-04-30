package com.github.cloudgyb.questionnaire.modules.templatemanager.controller.form;

import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 问卷类型创建form
 *
 * @author cloudgyb
 * 2021/4/11 16:05
 */
@Data
public class QuestionnaireTypeForm {
    @NotNull(message = "缺少id参数！", groups = UpdateGroup.class)
    private Long id;
    @NotBlank(message = "类型名称不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Size(max = 100, message = "类型名称太长，不能超过100个字符！", groups = {AddGroup.class, UpdateGroup.class})
    private String typeName;
    @NotBlank(message = "类型描述不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Size(max = 255, message = "类型描述太长，不能超过255个字符！", groups = {AddGroup.class, UpdateGroup.class})
    private String typeDesc;
}
