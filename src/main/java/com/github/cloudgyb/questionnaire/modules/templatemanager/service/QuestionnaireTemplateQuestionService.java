package com.github.cloudgyb.questionnaire.modules.templatemanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.datasource.annotation.DataSource;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTemplateQuestionDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTemplateQuestionOptionDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dto.TemplateQuestionDTO;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplateQuestion;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplateQuestionOption;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 调查问卷模板问题service
 *
 * @author cloudgyb
 * 2021/4/11 22:20
 */
@Service
@DataSource("app")
public class QuestionnaireTemplateQuestionService extends
        ServiceImpl<QuestionnaireTemplateQuestionDao, QuestionnaireTemplateQuestion> {
    private final QuestionnaireTemplateQuestionDao questionnaireTemplateQuestionDao;
    private final QuestionnaireTemplateQuestionOptionDao questionnaireTemplateQuestionOptionDao;

    QuestionnaireTemplateQuestionService(QuestionnaireTemplateQuestionDao questionnaireTemplateQuestionDao,
                                         QuestionnaireTemplateQuestionOptionDao questionnaireTemplateQuestionOptionDao) {
        this.questionnaireTemplateQuestionDao = questionnaireTemplateQuestionDao;
        this.questionnaireTemplateQuestionOptionDao = questionnaireTemplateQuestionOptionDao;
    }

    /**
     * 获取“访谈记录模板”下的所有问题
     *
     * @param templateId 访谈记录模板id
     * @return “问题”List
     */
    public List<QuestionnaireTemplateQuestion> getByTemplateId(long templateId) {
        return questionnaireTemplateQuestionDao.selectList(
                new QueryWrapper<QuestionnaireTemplateQuestion>()
                        .eq("template_id", templateId)
        );
    }

    public List<TemplateQuestionDTO> getDetailByTemplate(long tid) {
        List<TemplateQuestionDTO> list = null;
        List<QuestionnaireTemplateQuestion> questions = getByTemplateId(tid);
        if (questions != null) {
            list = new ArrayList<>();
            for (QuestionnaireTemplateQuestion q : questions) {
                TemplateQuestionDTO questionDTO = new TemplateQuestionDTO();
                questionDTO.setId(q.getId());
                questionDTO.setTemplateId(tid);
                questionDTO.setQuestionOrder(q.getQuestionOrder());
                questionDTO.setQuestionNum(q.getQuestionNum());
                questionDTO.setQuestionTitle(q.getQuestionTitle());
                questionDTO.setQuestionType(q.getQuestionType());
                questionDTO.setInputPlaceholder(q.getInputPlaceholder());
                List<QuestionnaireTemplateQuestionOption> questionOptions = questionnaireTemplateQuestionOptionDao
                        .selectList(new QueryWrapper<QuestionnaireTemplateQuestionOption>()
                                .eq("question_id", q.getId())
                        );
                questionOptions
                        .sort(Comparator.comparing(QuestionnaireTemplateQuestionOption::getOptionOrder)); //按照“问题选项”顺序排序
                questionDTO.setOptions(questionOptions);
                list.add(questionDTO);
            }
            //第一种排序：
            /*Collections.sort(list,(l1,l2)->
                Integer.compare(l1.getQuestionOrder(),l2.getQuestionOrder()));*/
            //第二种排序（简化）：
            //Collections.sort(list, Comparator.comparing(QuestionDTO::getQuestionOrder));
            //第三种排序(进一步简化)：
            list.sort(Comparator.comparing(TemplateQuestionDTO::getQuestionOrder)); //按照“问题顺序”排序
        }
        return list;
    }
}
