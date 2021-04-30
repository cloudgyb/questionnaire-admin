package com.github.cloudgyb.questionnaire.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 系统配置信息实体类
 *
 * @author Mark
 */
@Data
@TableName("sys_config")
public class SysConfigEntity {
    @TableId
    private Long id;
    @NotBlank(message = "参数名不能为空")
    @Size(max = 50, message = "参数名不能超过50个字符")
    private String paramKey;
    @NotBlank(message = "参数值不能为空")
    @Size(max = 2000, message = "参数值不能超过2000个字符")
    private String paramValue;
    private String remark;

}
