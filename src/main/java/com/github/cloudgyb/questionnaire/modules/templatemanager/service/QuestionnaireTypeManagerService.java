package com.github.cloudgyb.questionnaire.modules.templatemanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.utils.QueryPageBuilder;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.datasource.annotation.DataSource;
import com.github.cloudgyb.questionnaire.modules.templatemanager.controller.form.QuestionnaireTypeForm;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTemplateDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTypeDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplate;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 调查问卷类型管理service
 *
 * @author cloudgyb
 * 2021/4/11 15:07
 */
@Service
@DataSource("app")
public class QuestionnaireTypeManagerService extends ServiceImpl<QuestionnaireTypeDao, QuestionnaireType> {
    private final QuestionnaireTypeDao questionnaireTypeDao;
    private final QuestionnaireTemplateDao questionnaireTemplateDao;

    QuestionnaireTypeManagerService(QuestionnaireTypeDao questionnaireTypeDao,
                                    QuestionnaireTemplateDao questionnaireTemplateDao) {
        this.questionnaireTypeDao = questionnaireTypeDao;
        this.questionnaireTemplateDao = questionnaireTemplateDao;
    }

    public List<QuestionnaireType> list() {
        return questionnaireTypeDao.selectList(null);
    }

    public IPage<QuestionnaireType> page(Map<String, Object> paramMap) {
        String typeName = (String) paramMap.get("typeName");
        return this.page(new QueryPageBuilder<QuestionnaireType>().getPage(paramMap),
                new QueryWrapper<QuestionnaireType>()
                        .like(StringUtils.hasText(typeName),
                                "type_name", typeName));
    }

    @Transactional
    public R add(QuestionnaireTypeForm form) {
        QuestionnaireType questionnaireType = new QuestionnaireType();
        questionnaireType.setTypeName(form.getTypeName());
        questionnaireType.setTypeDesc(form.getTypeDesc());
        int n = questionnaireTypeDao.insert(questionnaireType);
        return n > 0 ? R.ok(questionnaireType.getId()) : R.error("创建失败！");
    }

    public R update(QuestionnaireTypeForm form) {
        QuestionnaireType questionnaireType = new QuestionnaireType();
        questionnaireType.setId(form.getId());
        questionnaireType.setTypeName(form.getTypeName());
        questionnaireType.setTypeDesc(form.getTypeDesc());
        int n = questionnaireTypeDao.updateById(questionnaireType);
        return n > 0 ? R.ok() : R.error("更新失败！");
    }

    @Transactional
    public R delete(Long id) {
        Integer count = questionnaireTemplateDao.selectCount(
                new QueryWrapper<QuestionnaireTemplate>().eq("type_id", id));
        if (count > 0) {
            return R.error("该调查问卷分类下有调查问卷模板，无法删除！");
        }
        int i = questionnaireTypeDao.deleteById(id);
        return i > 0 ? R.ok() : R.error("删除失败！");
    }


}
