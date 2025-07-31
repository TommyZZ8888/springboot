package com.www.demoes.domain;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author admin
 * @date 2021-03-30 19:36
 */
@Data
@Document(indexName = "jd_data")
public class EsEntity {
    private String id;
    private String title;

}
