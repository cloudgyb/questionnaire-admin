package com.github.cloudgyb.questionnaire.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 与Element UI绑定的分页
 *
 * @author cloudgyb
 * 2021/4/15 16:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElementUIPage<T> extends Page<T> {
    private Integer[] pageSizes = {10, 20, 50, 100};
    private String layout = "sizes ,prev, pager, next, jumper,total";

    public ElementUIPage(long current, long size) {
        super(current, size, 0);
    }
}
