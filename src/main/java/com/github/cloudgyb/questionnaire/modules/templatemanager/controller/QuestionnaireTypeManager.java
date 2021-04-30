package com.github.cloudgyb.questionnaire.modules.templatemanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.common.validator.ValidatorUtils;
import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.common.validator.group.UpdateGroup;
import com.github.cloudgyb.questionnaire.modules.templatemanager.controller.form.QuestionnaireTypeForm;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireType;
import com.github.cloudgyb.questionnaire.modules.templatemanager.service.QuestionnaireTypeManagerService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 调查问卷类型管理
 *
 * @author cloudgyb
 * 2021/4/11 15:06
 */
@Api(tags = "调查问卷模板类别管理模块")
@RestController
@RequestMapping("/template/type")
public class QuestionnaireTypeManager {
    private final QuestionnaireTypeManagerService questionnaireTypeManagerService;

    QuestionnaireTypeManager(QuestionnaireTypeManagerService questionnaireTypeManagerService) {
        this.questionnaireTypeManagerService = questionnaireTypeManagerService;
    }

    @GetMapping("/list")
    public R list() {
        List<QuestionnaireType> data = questionnaireTypeManagerService.list();
        return R.ok(data);
    }

    @GetMapping("/pageList")
    public R pageList(@RequestParam Map<String, Object> params) {
        IPage<QuestionnaireType> page = questionnaireTypeManagerService.page(params);
        return R.ok(page);
    }

    @PostMapping("/add")
    public R add(@RequestBody QuestionnaireTypeForm form) {
        ValidatorUtils.validateEntity(form, AddGroup.class);
        return questionnaireTypeManagerService.add(form);
    }

    @PostMapping("/update")
    public R update(@RequestBody QuestionnaireTypeForm form) {
        ValidatorUtils.validateEntity(form, UpdateGroup.class);
        return questionnaireTypeManagerService.update(form);
    }

    @PostMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        return questionnaireTypeManagerService.delete(id);
    }

}
