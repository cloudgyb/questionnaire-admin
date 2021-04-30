package com.github.cloudgyb.questionnaire.modules.templatemanager.service;

import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.datasource.annotation.DataSource;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTemplateDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTemplateQuestionOptionDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dto.QuestionnaireTemplateDTO;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dto.TemplateQuestionDTO;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplate;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplateQuestionOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 调查问卷模板设计service
 *
 * @author cloudgyb
 * 2021/4/17 10:13
 */
@Service
@DataSource("app")
public class QuestionnaireTemplateDesignService {
    private final QuestionnaireTemplateDao questionnaireTemplateDao;
    private final QuestionnaireTemplateQuestionService templateQuestionService;
    private final QuestionnaireTemplateQuestionOptionDao templateQuestionOptionDao;

    QuestionnaireTemplateDesignService(QuestionnaireTemplateDao questionnaireTemplateDao,
                                       QuestionnaireTemplateQuestionService templateQuestionService, QuestionnaireTemplateQuestionOptionDao templateQuestionOptionDao) {
        this.questionnaireTemplateDao = questionnaireTemplateDao;
        this.templateQuestionService = templateQuestionService;
        this.templateQuestionOptionDao = templateQuestionOptionDao;
    }


    @Transactional
    public R deleteQuestion(Long tId, Long qId) {
        boolean b = templateQuestionService.removeById(qId);
        if (b) { //将模板的问题数-1
            QuestionnaireTemplate questionnaireTemplate = questionnaireTemplateDao.selectById(tId);
            questionnaireTemplate.setQuestionCount(questionnaireTemplate.getQuestionCount() - 1);
            questionnaireTemplateDao.updateById(questionnaireTemplate);
        }
        return b ? R.ok() : R.error("问题不存在，删除失败！");
    }

    @Transactional
    public R deleteQuestionOption(Long oid) {
        int n = templateQuestionOptionDao.deleteById(oid);
        return n > 0 ? R.ok() : R.error("选项不存在，删除失败！");
    }

    @Transactional
    public R saveTemplate(QuestionnaireTemplateDTO dto) {
        boolean b = validateDTO(dto);
        if (!b)
            return R.error("参数错误，报存失败！");
        //更新调查问卷模板信息
        QuestionnaireTemplate template = questionnaireTemplateDao.selectById(dto.getId());
        if (template == null)
            return R.error("调查问卷不存在，保存失败！");
        template.setName(dto.getName());
        List<TemplateQuestionDTO> questionDTOs = dto.getQuestions();
        template.setQuestionCount(questionDTOs == null ? 0 : questionCount(questionDTOs));
        questionnaireTemplateDao.updateById(template);
        //更新问题
        if (questionDTOs == null)
            return R.ok();
        for (TemplateQuestionDTO questionDTO : questionDTOs) {
            questionDTO.setTemplateId(template.getId());
            if (questionDTO.getId() == null) { //新添加的问题
                templateQuestionService.save(questionDTO);
            } else {
                templateQuestionService.updateById(questionDTO);
            }
            //3、对单选和多选题更新选项
            if (questionDTO.getQuestionType() == 3 || questionDTO.getQuestionType() == 4)
                updateQuestionOptions(questionDTO.getOptions(), questionDTO.getId());
        }
        return R.ok();
    }

    private int questionCount(List<TemplateQuestionDTO> questionDTOs) {
        int n = 0;
        for (TemplateQuestionDTO questionDTO : questionDTOs) {
            if (questionDTO.getQuestionType() != 5)
                n++;
        }
        return n;
    }

    private void updateQuestionOptions(List<QuestionnaireTemplateQuestionOption> options, long questionId) {
        if (options == null)
            return;
        for (QuestionnaireTemplateQuestionOption option : options) {
            option.setQuestionId(questionId);
            if (option.getId() != null) {
                templateQuestionOptionDao.updateById(option);
            } else {
                templateQuestionOptionDao.insert(option);
            }
        }
    }

    private boolean validateDTO(QuestionnaireTemplateDTO dto) {
        if (dto == null || dto.getId() == null)
            return false;
        String templateName = dto.getName();
        return StringUtils.hasText(templateName);
    }
}
