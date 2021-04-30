package com.github.cloudgyb.questionnaire.modules.templatemanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.constant.TemplateStatusEnum;
import com.github.cloudgyb.questionnaire.common.utils.QueryPageBuilder;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.datasource.annotation.DataSource;
import com.github.cloudgyb.questionnaire.modules.templatemanager.controller.form.QuestionnaireTemplateForm;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTemplateDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dto.QuestionnaireTemplateDTO;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dto.TemplateQuestionDTO;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 调查问卷模板管理service
 *
 * @author cloudgyb
 * 2021/4/11 22:20
 */
@Service
@DataSource("app")
public class QuestionnaireTemplateManagerService extends ServiceImpl<QuestionnaireTemplateDao, QuestionnaireTemplate> {
    private final QuestionnaireTemplateDao questionnaireTemplateDao;
    private final QuestionnaireTemplateQuestionService questionService;
    private final QuestionnaireTemplateIndexService templateIndexService;

    QuestionnaireTemplateManagerService(QuestionnaireTemplateDao questionnaireTemplateDao,
                                        QuestionnaireTemplateQuestionService questionService,
                                        QuestionnaireTemplateIndexService templateIndexService) {
        this.questionnaireTemplateDao = questionnaireTemplateDao;
        this.questionService = questionService;
        this.templateIndexService = templateIndexService;
    }

    public List<QuestionnaireTemplate> list(Long typeId) {
        return questionnaireTemplateDao.
                selectList(new QueryWrapper<QuestionnaireTemplate>()
                        .eq("type_id", typeId));
    }

    public IPage<QuestionnaireTemplate> pageList(Map<String, Object> params) {
        String typeId = (String) params.get("typeId");
        String templateName = (String) params.get("templateName");
        return this.page(new QueryPageBuilder<QuestionnaireTemplate>().getPage(params),
                new QueryWrapper<QuestionnaireTemplate>()
                        .eq(StringUtils.hasText(typeId), "type_id", typeId)
                        .like(StringUtils.hasText(templateName), "name", templateName));
    }

    @Transactional
    public R add(QuestionnaireTemplateForm form) {
        QuestionnaireTemplate questionnaireTemplate = new QuestionnaireTemplate();
        questionnaireTemplate.setTypeId(form.getTypeId());
        questionnaireTemplate.setName(form.getName());
        questionnaireTemplate.setAuthorId(0); //管理员创建的模板恒为零
        questionnaireTemplate.setQuestionCount(0); //新创建的模板没有问题
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        questionnaireTemplate.setCreateDate(timestamp);
        questionnaireTemplate.setStatus(TemplateStatusEnum.CREATED.getStatus());
        int n = questionnaireTemplateDao.insert(questionnaireTemplate);
        return n > 0 ? R.ok() : R.error("添加失败！");
    }

    @Transactional
    public R update(QuestionnaireTemplateForm form) {
        QuestionnaireTemplate template = questionnaireTemplateDao.selectById(form.getId());
        template.setTypeId(form.getTypeId());
        template.setName(form.getName());
        int n = questionnaireTemplateDao.updateById(template);
        //如果是发布状态，则重新索引
        if (template.getStatus() == TemplateStatusEnum.PUBLISHED.getStatus())
            this.templateIndexService.index(template);
        return n > 0 ? R.ok() : R.error("修改失败！");
    }

    @Transactional
    public R delete(Long tid) {
        int n = questionnaireTemplateDao.deleteById(tid);
        //删除索引
        templateIndexService.deleteIndex(tid);
        return n > 0 ? R.ok() : R.error("删除失败！");
    }

    public R detail(Long tid) {
        QuestionnaireTemplate t = questionnaireTemplateDao.selectById(tid);
        QuestionnaireTemplateDTO dto = null;
        if (t != null) {
            dto = new QuestionnaireTemplateDTO();
            dto.setId(tid);
            dto.setName(t.getName());
            dto.setAuthorId(t.getAuthorId());
            dto.setCreateDate(t.getCreateDate());
            dto.setPublishDate(t.getPublishDate());
            dto.setQuestionCount(t.getQuestionCount());
            dto.setTypeId(t.getTypeId());
            dto.setStatus(t.getStatus());
            List<TemplateQuestionDTO> questionDTO = questionService.getDetailByTemplate(tid);
            dto.setQuestions(questionDTO);
        }
        return R.ok(dto);
    }

    /**
     * 发布调查问卷模板
     *
     * @param tid 模板id
     */
    @Transactional
    public R publish(Long tid) {
        QuestionnaireTemplate template = questionnaireTemplateDao.selectById(tid);
        if (template == null)
            return R.error("调查问卷模板不存在！");
        template.setStatus(TemplateStatusEnum.PUBLISHED.getStatus());
        template.setPublishDate(new Timestamp(System.currentTimeMillis()));
        this.updateById(template);
        this.templateIndexService.index(template);
        return R.ok();
    }

    /**
     * 下线调查问卷模板，下线后对用户不可见（逻辑删除）
     *
     * @param tid 模板id
     */
    @Transactional
    public R switchOffline(Long tid) {
        QuestionnaireTemplate t = questionnaireTemplateDao.selectById(tid);
        if (t == null)
            return R.error("调查问卷模板不存在！");
        if (t.getStatus() == TemplateStatusEnum.CREATED.getStatus())
            return R.error("调查问卷模板未发布，无法进行上下线操作！");
        //调查问卷模板下线操作
        if (t.getStatus() == TemplateStatusEnum.PUBLISHED.getStatus()) {
            t.setStatus(TemplateStatusEnum.OFFLINE.getStatus());
            //删除索引
            this.templateIndexService.deleteIndex(tid);
        }
        //调查问卷模板上线操作
        else if (t.getStatus() == TemplateStatusEnum.OFFLINE.getStatus()) {
            t.setStatus(TemplateStatusEnum.PUBLISHED.getStatus());
            this.templateIndexService.index(t);
        }
        this.updateById(t);
        return R.ok();
    }
}
