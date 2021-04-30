package com.github.cloudgyb.questionnaire.modules.sys.vo;

import com.github.cloudgyb.questionnaire.common.validator.group.AddGroup;
import com.github.cloudgyb.questionnaire.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 菜单树VO(此类属性对应Element UI中el-tree组件)
 *
 * @author cloudgyb
 * 2021/4/23 21:05
 */
@Data
public class MenuTreeNodeVO {
    @NotNull(message = "菜单ID不能为空！", groups = {UpdateGroup.class})
    private Long id;
    @NotNull(message = "菜单父ID不能为空！", groups = {AddGroup.class})
    private Long parentId;
    private String icon;
    @NotBlank(message = "菜单名称不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Size(max = 10, message = "菜单名称不能超过10个字符！")
    private String label;
    private String url;
    private int orderNum;
    /**
     * 是否展开子节点菜单
     */
    private boolean open;
    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    @NotNull(message = "菜单类型不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    private Integer type;
    private String perms;
    private List<MenuTreeNodeVO> children;
}
