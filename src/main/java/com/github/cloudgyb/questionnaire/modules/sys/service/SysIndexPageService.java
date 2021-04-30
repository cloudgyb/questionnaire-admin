package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.cloudgyb.questionnaire.datasource.annotation.DataSource;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTemplateDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.dao.QuestionnaireTypeDao;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplate;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireType;
import com.github.cloudgyb.questionnaire.modules.usermanager.entity.User;
import com.github.cloudgyb.questionnaire.modules.usermanager.service.UserManagerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页数据服务
 *
 * @author cloudgyb
 * 2021/3/17 18:43
 */
@Service
@DataSource("app")
public class SysIndexPageService {
    private final UserManagerService userManagerService;
    private final QuestionnaireTemplateDao questionnaireTemplateDao;
    private final QuestionnaireTypeDao questionnaireTypeDao;

    SysIndexPageService(UserManagerService userManagerService,
                        QuestionnaireTemplateDao questionnaireTemplateDao,
                        QuestionnaireTypeDao questionnaireTypeDao) {
        this.userManagerService = userManagerService;
        this.questionnaireTemplateDao = questionnaireTemplateDao;
        this.questionnaireTypeDao = questionnaireTypeDao;
    }

    public Map<String, Integer> userStatsData() {
        int total = userManagerService.count();
        int mTotal = userManagerService.count(new QueryWrapper<User>().eq("sex", 0));
        int fTotal = userManagerService.count(new QueryWrapper<User>().eq("sex", 1));
        HashMap<String, Integer> map = new HashMap<>();
        map.put("m", mTotal);
        map.put("f", fTotal);
        map.put("n", total - mTotal - fTotal);
        map.put("t", total);
        return map;
    }

    public List<Map<String, Object>> templateStatsByType() {
        List<QuestionnaireType> types = questionnaireTypeDao.selectList(null);
        List<Map<String, Object>> res = new ArrayList<>(types.size());
        for (QuestionnaireType type : types) {
            HashMap<String, Object> map = new HashMap<>();
            Integer n = questionnaireTemplateDao.selectCount(new QueryWrapper<QuestionnaireTemplate>()
                    .eq("type_id", type.getId()));
            map.put("name", type.getTypeName());
            map.put("value", n);
            res.add(map);
        }
        return res;
    }

}
