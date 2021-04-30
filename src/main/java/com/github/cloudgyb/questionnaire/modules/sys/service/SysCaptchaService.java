package com.github.cloudgyb.questionnaire.modules.sys.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.exception.RRException;
import com.github.cloudgyb.questionnaire.common.utils.DateUtils;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysCaptchaDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysCaptchaEntity;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * 验证码service
 *
 * @author Mark
 */
@Service
public class SysCaptchaService extends ServiceImpl<SysCaptchaDao, SysCaptchaEntity> {
    private final Producer producer;

    public SysCaptchaService(Producer producer) {
        this.producer = producer;
    }

    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new RRException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();

        SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(code);
        //5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        this.save(captchaEntity);

        return producer.createImage(code);
    }

    public boolean validate(String uuid, String code) {
        SysCaptchaEntity captchaEntity = this.getOne(new QueryWrapper<SysCaptchaEntity>()
                .eq("uuid", uuid));
        if (captchaEntity == null) {
            return false;
        }
        //删除验证码
        this.removeById(uuid);
        return captchaEntity.getCode().equalsIgnoreCase(code) &&
                captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis();
    }
}
