package com.www.demoes.config;

import com.www.demoes.domain.EsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Elasticsearch 工具类
 *
 * @author admin
 * @date 2021-03-30 19:29
 */
@Component
public class EsUtil {

    private static final Logger logger = LoggerFactory.getLogger(EsUtil.class);

    @Value("${es.index.jd-data.name:jd_data}")
    private String indexName;

    @Value("${es.index.jd-data.init-on-startup:false}")
    private boolean initIndexOnStartup;

    @Value("${es.index.jd-data.config-path:elasticsearch/es_index.json}")
    private String indexConfigPath;

    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 项目启动初始化索引
     * 使用 @PostConstruct 确保在依赖注入完成后执行。
     * 注意：@PostConstruct 不应抛出检查异常，需要内部处理。
     */
//    @PostConstruct
    public void init() {
        if (initIndexOnStartup) {
            try {
                if (!indexExist(indexName)) {
                    logger.info("Elasticsearch 索引 {} 不存在，开始创建...", indexName);
                    createIndex(indexName);
                    logger.info("Elasticsearch 索引 {} 创建成功。", indexName);
                } else {
                    logger.info("Elasticsearch 索引 {} 已存在，跳过创建。", indexName);
                }
            } catch (IOException e) {
                logger.error("初始化 Elasticsearch 索引 {} 失败：{}", indexName, e.getMessage(), e);
            }
        }
    }

    /**
     * 判断索引是否存在
     *
     * @param index 索引名称
     * @return 如果索引存在则返回 true，否则返回 false
     */
    public boolean indexExist(String index) {
        IndexOperations indexOperations = operations.indexOps(IndexCoordinates.of(index));
        return indexOperations.exists();
    }

    /**
     * 创建索引
     *
     * @param index 索引名称
     * @throws IOException 如果读取配置文件或创建索引失败
     */
    public void createIndex(String index) throws IOException {
        IndexOperations indexOperations = operations.indexOps(IndexCoordinates.of(index));

        if (indexOperations.exists()) {
            logger.info("索引 {} 已存在，跳过创建。", index);
            return;
        }

        Resource resource = resourceLoader.getResource("classpath:" + indexConfigPath);
        if (!resource.exists()) {
            logger.warn("Elasticsearch 索引配置文件 {} 不存在，将使用默认设置创建索引。", indexConfigPath);
            indexOperations.create(); // Create with default settings
            return;
        }

        try (InputStream in = resource.getInputStream();
             Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            String jsonConfig = scanner.useDelimiter("\\A").next(); // Read whole file content
            // Parse JSON string to Map for settings and mappings
            Map<String, Object> indexSettingsAndMappings = objectMapper.readValue(jsonConfig, Map.class);

            // Extract settings and mappings if they are top-level keys
            Map<String, Object> settings = (Map<String, Object>) indexSettingsAndMappings.get("settings");
            Map<String, Object> mappings = (Map<String, Object>) indexSettingsAndMappings.get("mappings");

            if (settings != null && mappings != null) {
                indexOperations.create(Document.from(settings), Document.from(mappings));
            } else if (settings != null) {
                indexOperations.create(Document.from(settings));
            } else if (mappings != null) {
                indexOperations.create(Document.from(mappings));
            } else {
                logger.warn("Elasticsearch index config file {} does not contain 'settings' or 'mappings' top-level keys. Creating with default settings.", indexConfigPath);
                indexOperations.create();
            }

        } catch (IOException e) {
            logger.error("读取 Elasticsearch 索引配置文件 {} 失败：{}", indexConfigPath, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("创建 Elasticsearch 索引 {} 失败：{}", index, e.getMessage(), e);
            throw new IOException("创建索引失败", e);
        }
    }

    /**
     * 添加或更新数据
     *
     * @param index  索引名称
     * @param entity 包含ID和数据的实体
     * @return String ID of the inserted/updated document
     */
    public String insertOrUpdateOne(String index, EsEntity entity) {
        if (entity == null || entity.getId() == null || entity.getTitle() == null) {
            logger.warn("尝试插入或更新数据时，实体、ID或数据为空，跳过操作。");
            return null;
        }

        IndexQuery query = new IndexQueryBuilder()
                .withId(entity.getId())
                .withObject(entity.getTitle())
                .build();
        try {
            return operations.index(query, IndexCoordinates.of(index));
        } catch (Exception e) {
            logger.error("es添加/更新数据失败，索引: {}，ID: {}，错误信息: {}", index, entity.getId(), e.getMessage(), e);
        }
        return null;
    }

    /**
     * 执行ES查询
     *
     * @param index             索引名称
     * @param criteria          Criteria object for building the query
     * @param clazz             返回结果的实体类类型
     * @param <T>               实体类泛型
     * @return List<T> 查询结果列表
     */
    public <T> List<T> search(String index, Criteria criteria, Class<T> clazz) {
        CriteriaQuery searchQuery = new CriteriaQuery(criteria);
        try {
            return operations.search(searchQuery, clazz, IndexCoordinates.of(index))
                    .stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("es查询失败，索引: {}，错误信息: {}", index, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 批量新增
     *
     * @param index 索引名称
     * @param list  数据列表
     * @return List of document IDs successfully inserted
     */
    public Object insertBatch(String index, List<EsEntity> list) {
        if (list == null || list.isEmpty()) {
            logger.warn("尝试批量插入数据时列表为空，跳过操作。");
            return null;
        }

        List<IndexQuery> queries = list.stream()
                .filter(item -> item.getId() != null && item.getTitle() != null)
                .map(item -> new IndexQueryBuilder().withId(item.getId()).withObject(item.getTitle()).build())
                .collect(Collectors.toList());

        if (queries.isEmpty()) {
            logger.info("没有有效的文档可供批量插入，操作终止。");
            return null;
        }

        try {
             operations.bulkIndex(queries, IndexCoordinates.of(index));
        } catch (Exception e) {
            logger.error("es批量添加数据失败，索引: {}，错误信息: {}", index, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据查询条件批量删除文档 (通过 Criteria)
     *
     * @param index 索引名称
     * @param criteria The Criteria object to use for deletion (e.g., new Criteria())
     * @return Long number of documents deleted
     */
    public void deleteByQuery(String index, Criteria criteria) {
        Query searchQuery = new CriteriaQuery(criteria);
        try {
             operations.delete(searchQuery, EsEntity.class, IndexCoordinates.of(index));
        } catch (Exception e) {
            logger.error("es批量删除失败，索引: {}，错误信息: {}", index, e.getMessage(), e);
        }
    }

}