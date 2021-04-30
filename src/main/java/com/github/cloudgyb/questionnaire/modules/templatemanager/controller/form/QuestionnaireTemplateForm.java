package com.github.cloudgyb.questionnaire.modules.templatemanager.controller.form;

import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 调查问卷模板form
 *
 * @author cloudgyb
 * 2021/4/12 10:01
 */
@Data
public class QuestionnaireTemplateForm {
    @NotNull(message = "调查问卷模板id不能为空！", groups = {UpdateGroup.class})
    private Long id;
    @NotNull(message = "调查问卷模板类型id不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    private Long typeId;
    @NotBlank(message = "调查问卷模板名称不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Size(max = 100, message = "调查问卷模板名称长度不能超过100个字符！", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
}
