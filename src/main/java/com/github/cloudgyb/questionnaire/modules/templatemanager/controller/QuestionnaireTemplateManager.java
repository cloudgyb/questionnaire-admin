package com.github.cloudgyb.questionnaire.modules.templatemanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.common.validator.ValidatorUtils;
import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.common.validator.group.UpdateGroup;
import com.github.cloudgyb.questionnaire.modules.templatemanager.controller.form.QuestionnaireTemplateForm;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplate;
import com.github.cloudgyb.questionnaire.modules.templatemanager.service.QuestionnaireTemplateManagerService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 调查问卷模板管理
 *
 * @author cloudgyb
 * 2021/4/11 22:20
 */
@Api(tags = "调查问卷模板管理模块")
@RestController
@RequestMapping("/template")
public class QuestionnaireTemplateManager {
    private final QuestionnaireTemplateManagerService questionnaireTemplateManagerService;

    QuestionnaireTemplateManager(QuestionnaireTemplateManagerService questionnaireTemplateManagerService) {
        this.questionnaireTemplateManagerService = questionnaireTemplateManagerService;
    }

    @GetMapping("/list")
    public R list(@RequestParam Long typeId) {
        List<QuestionnaireTemplate> list = questionnaireTemplateManagerService.list(typeId);
        return R.ok(list);
    }

    @GetMapping("/pageList")
    public R list(@RequestParam Map<String, Object> params) {
        IPage<QuestionnaireTemplate> page = questionnaireTemplateManagerService.pageList(params);
        return R.ok(page);
    }

    @PostMapping("/add")
    public R add(@RequestBody QuestionnaireTemplateForm form) {
        ValidatorUtils.validateEntity(form, AddGroup.class);
        return questionnaireTemplateManagerService.add(form);
    }

    @PostMapping("/update")
    public R update(@RequestBody QuestionnaireTemplateForm form) {
        ValidatorUtils.validateEntity(form, UpdateGroup.class);
        return questionnaireTemplateManagerService.update(form);
    }

    @PostMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        return questionnaireTemplateManagerService.delete(id);
    }

    @GetMapping("/detail")
    public R getAllQuestions(@RequestParam Long tid) {
        return questionnaireTemplateManagerService.detail(tid);
    }

    @PostMapping("/publish")
    public R publish(@RequestParam Long tid) {
        return questionnaireTemplateManagerService.publish(tid);
    }

    @PostMapping("/switchOffline")
    public R switchOffline(@RequestParam Long tid) {
        return questionnaireTemplateManagerService.switchOffline(tid);
    }

}
