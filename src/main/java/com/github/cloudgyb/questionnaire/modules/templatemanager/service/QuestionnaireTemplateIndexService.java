package com.github.cloudgyb.questionnaire.modules.templatemanager.service;

import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.ESTemplateRepository;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTemplateDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.ESTemplate;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.ESTemplateQuestion;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplate;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplateQuestion;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 调查问卷模板索引服务（Elasticsearch实现）
 *
 * @author cloudgyb
 * 2021/4/22 13:26
 */
@Service
public class QuestionnaireTemplateIndexService {
    private final QuestionnaireTemplateDao templateDao;
    private final QuestionnaireTemplateQuestionService templateQuestionService;
    private final ESTemplateRepository esTemplateRepository;

    public QuestionnaireTemplateIndexService(QuestionnaireTemplateDao templateDao,
                                             QuestionnaireTemplateQuestionService templateQuestionService,
                                             ESTemplateRepository esTemplateRepository) {
        this.templateDao = templateDao;
        this.templateQuestionService = templateQuestionService;
        this.esTemplateRepository = esTemplateRepository;
    }

    /**
     * 将调查问卷模板索引到ES
     *
     * @param tid 模板id
     */
    public void index(Long tid) {
        QuestionnaireTemplate template = templateDao.selectById(tid);
        if (template == null)
            return;
        this.index(template);
    }

    public void index(QuestionnaireTemplate template) {
        final ESTemplate esTemplate = new ESTemplate();
        BeanUtils.copyProperties(template, esTemplate);
        final List<QuestionnaireTemplateQuestion> questionList = templateQuestionService
                .getByTemplateId(template.getId());

        final ArrayList<ESTemplateQuestion> qList = new ArrayList<>();
        if (questionList != null) {
            questionList.forEach(q -> {
                final ESTemplateQuestion esTemplateQuestion = new ESTemplateQuestion();
                BeanUtils.copyProperties(q, esTemplateQuestion);
                qList.add(esTemplateQuestion);
            });
        }
        esTemplate.setQuestions(qList);
        this.index(esTemplate);
    }

    public void index(ESTemplate esTemplate) {
        esTemplateRepository.save(esTemplate);
    }


    public void deleteIndex(Long tid) {
        QuestionnaireTemplate template = templateDao.selectById(tid);
        if (template == null)
            return;
        this.esTemplateRepository.deleteById(tid);
    }

    public void deleteIndex(QuestionnaireTemplate template) {
        this.esTemplateRepository.deleteById(template.getId());
    }
}
