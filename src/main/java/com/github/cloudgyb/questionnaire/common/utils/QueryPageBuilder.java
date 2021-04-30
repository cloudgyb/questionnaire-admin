package com.github.cloudgyb.questionnaire.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.cloudgyb.questionnaire.common.xss.SQLFilter;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 分页查询参数
 *
 * @author Mark
 * @author geng
 */
public class QueryPageBuilder<T> {

    public IPage<T> getPage(Map<String, Object> pageParams) {
        return this.getPage(pageParams, null, false);
    }

    public IPage<T> getPage(Map<String, Object> pageParams, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (pageParams.get(Constant.PAGE) != null) {
            curPage = Long.parseLong((String) pageParams.get(Constant.PAGE));
        }
        if (pageParams.get(Constant.LIMIT) != null) {
            limit = Long.parseLong((String) pageParams.get(Constant.LIMIT));
        }

        //分页对象
        Page<T> page = new ElementUIPage<>(curPage, limit);

        //分页参数
        pageParams.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String) pageParams.get(Constant.ORDER_FIELD));
        String order = (String) pageParams.get(Constant.ORDER);


        //前端字段排序
        if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
            if (Constant.ASC.equalsIgnoreCase(order)) {
                return page.addOrder(OrderItem.asc(orderField));
            } else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if (StringUtils.isBlank(defaultOrderField)) {
            return page;
        }

        //默认排序
        if (isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        } else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
}
