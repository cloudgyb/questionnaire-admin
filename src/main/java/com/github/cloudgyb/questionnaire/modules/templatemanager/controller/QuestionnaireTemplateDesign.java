package com.github.cloudgyb.questionnaire.modules.templatemanager.controller;

import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dto.QuestionnaireTemplateDTO;
import com.github.cloudgyb.questionnaire.modules.templatemanager.service.QuestionnaireTemplateDesignService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * 调查问卷模板设计
 *
 * @author cloudgyb
 * 2021/4/17 9:55
 */
@Api(tags = "调查问卷模板设计模块")
@RestController
@RequestMapping("/template/design")
public class QuestionnaireTemplateDesign {
    private final QuestionnaireTemplateDesignService questionnaireTemplateDesignService;

    QuestionnaireTemplateDesign(QuestionnaireTemplateDesignService questionnaireTemplateDesignService) {
        this.questionnaireTemplateDesignService = questionnaireTemplateDesignService;
    }

    @PostMapping("/delQuestion")
    public R deleteQuestion(@RequestParam Long tid, @RequestParam Long qid) {
        return questionnaireTemplateDesignService.deleteQuestion(tid, qid);
    }

    @PostMapping("/delOption")
    public R deleteQuestionOption(@RequestParam Long oid) {
        return questionnaireTemplateDesignService.deleteQuestionOption(oid);
    }

    @PostMapping("/save")
    public R saveTemplate(@RequestBody QuestionnaireTemplateDTO dto) {
        return questionnaireTemplateDesignService.saveTemplate(dto);
    }
}
