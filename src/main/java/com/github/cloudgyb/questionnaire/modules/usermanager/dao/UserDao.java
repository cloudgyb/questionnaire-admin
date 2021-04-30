package com.github.cloudgyb.questionnaire.modules.usermanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cloudgyb.questionnaire.modules.usermanager.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 注册用户Dao
 *
 * @author cloudgyb
 * 2021/4/13 17:01
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
}
