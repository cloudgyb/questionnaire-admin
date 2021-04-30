package com.github.cloudgyb.questionnaire.modules.usermanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.cloudgyb.questionnaire.common.constant.UserStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * 注册用户实体类
 *
 * @author cloudgyb
 * 2021/4/13 17:00
 */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    @JsonIgnore
    private transient String password;
    @JsonIgnore
    private transient String passwordSalt;
    private String realName;
    private String phone;
    private String email;
    private int age;
    private int sex;
    private int isVip;
    private Date createDate;
    /**
     * 用户状态
     *
     * @see UserStatusEnum
     */
    private int status;

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }
}
