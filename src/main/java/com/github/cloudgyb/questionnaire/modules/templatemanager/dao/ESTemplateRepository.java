package com.github.cloudgyb.questionnaire.modules.templatemanager.dao;

import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.ESTemplate;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Elasticsearch 调查问卷模板索引仓库
 *
 * @author cloudgyb
 * 2021/4/22 13:24
 */
public interface ESTemplateRepository extends ElasticsearchRepository<ESTemplate, Long> {

    @Highlight(
            fields = {
                    @HighlightField(name = "name"),
                    @HighlightField(name = "questions.questionTitle")
            }
    )
    @Query("{\"bool\":{\"should\":[{\"match_phrase\":{\"name\":{\"query\":\"?0\"}}},{\"nested\":{\"path\":\"questions\",\"query\":{\"bool\":{\"must\":[{\"match_phrase\":{\"questions.questionTitle\":\"?0\"}}]}}}}]}}")
    SearchHits<ESTemplate> find(String key);

}