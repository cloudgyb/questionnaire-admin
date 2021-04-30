package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.utils.QueryPageBuilder;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysLogDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysLogEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysLogService")
public class SysLogService extends ServiceImpl<SysLogDao, SysLogEntity> {

    public IPage<SysLogEntity> queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        return this.page(
                new QueryPageBuilder<SysLogEntity>().getPage(params),
                new QueryWrapper<SysLogEntity>()
                        .like(StringUtils.isNotBlank(username),
                                "username", username)
        );
    }

    public void clearLog() {
        this.remove(null);
    }
    
    public void delete(Long id) {
        this.removeById(id);
    }

    public void deleteMany(List<Long> ids) {
        this.removeByIds(ids);
    }


}
