package com.www.demoes.config;

import com.alibaba.fastjson.JSONObject;
import com.www.demoes.domain.EsEntity;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Elasticsearch 工具类
 *
 * @author admin
 * @date 2021-03-30 19:29
 */
@Component
//@ConditionalOnProperty(prefix = "es", name = "enable", havingValue = "true") // 只有当 es.enable=true 时才加载此 Bean
//@ConditionalOnBean(RestHighLevelClient.class) // 确保 RestHighLevelClient Bean 存在
public class EsUtil {

    private static final Logger logger = LoggerFactory.getLogger(EsUtil.class);

    // 从配置文件中获取索引名称和配置
    @Value("${es.index.jd-data.name:jd_data}")
    private String indexName;

    @Value("${es.index.jd-data.init-on-startup:false}")
    private boolean initIndexOnStartup;

    @Value("${es.index.jd-data.config-path:elasticsearch/es_index.json}")
    private String indexConfigPath;

    private final RestHighLevelClient client;
    private final ResourceLoader resourceLoader;

    // 通过构造函数注入 RestHighLevelClient 和 ResourceLoader
    public EsUtil(RestHighLevelClient client, ResourceLoader resourceLoader) {
        this.client = client;
        this.resourceLoader = resourceLoader;
    }

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
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("es判断索引 {} 是否存在时发生IO异常：{}", index, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("es判断索引 {} 是否存在时发生未知异常：{}", index, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 创建索引
     *
     * @param index 索引名称
     * @throws IOException 如果读取配置文件或创建索引失败
     */
    public void createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        boolean exist = indexExist(index);
        if (!exist){
            client.indices().create(request, RequestOptions.DEFAULT);
            return;
        }


        Resource resource = resourceLoader.getResource("classpath:" + indexConfigPath);
        if (!resource.exists()) {
            logger.warn("Elasticsearch 索引配置文件 {} 不存在，将使用默认设置创建索引。", indexConfigPath);
            // 如果配置文件不存在，可以考虑不设置 source，让ES使用默认设置
            client.indices().create(request, RequestOptions.DEFAULT);
            return;
        }

        try (InputStream in = resource.getInputStream();
             Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            String jsonConfig = scanner.useDelimiter("\\A").next(); // 读取整个文件内容
            request.source(jsonConfig, XContentType.JSON);
            client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("读取 Elasticsearch 索引配置文件 {} 失败：{}", indexConfigPath, e.getMessage(), e);
            throw e; // 重新抛出以便 init 方法捕获
        } catch (Exception e) {
            logger.error("创建 Elasticsearch 索引 {} 失败：{}", index, e.getMessage(), e);
            throw new IOException("创建索引失败", e); // 封装为 IOException
        }
    }

    /**
     * 添加或更新数据
     *
     * @param index  索引名称
     * @param entity 包含ID和数据的实体
     * @return IndexResponse
     */
    public IndexResponse insertOrUpdateOne(String index, EsEntity entity) {
        if (entity == null || StringUtils.isBlank(entity.getId()) || entity.getData() == null) {
            logger.warn("尝试插入或更新数据时，实体、ID或数据为空，跳过操作。");
            return null;
        }
        IndexRequest request = new IndexRequest(index);
        request.id(entity.getId());
        request.source(JSONObject.toJSONString(entity.getData()), XContentType.JSON);
        try {
            return client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("es添加/更新数据失败，索引: {}，ID: {}，错误信息: {}", index, entity.getId(), e.getMessage(), e);
        }
        return null;
    }

    /**
     * 执行ES查询
     *
     * @param index             索引名称
     * @param searchSourceBuilder 查询源构建器
     * @return SearchResponse 查询结果
     */
    public SearchResponse search(String index, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest request = new SearchRequest(index);
        request.source(searchSourceBuilder);
        try {
            return client.search(request, RequestOptions.DEFAULT);
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
     * @return BulkResponse
     */
    public BulkResponse insertBatch(String index, List<EsEntity> list) {
        if (list == null || list.isEmpty()) {
            logger.warn("尝试批量插入数据时列表为空，跳过操作。");
            return null;
        }
        BulkRequest request = new BulkRequest();
        for (EsEntity item : list) {
            if (StringUtils.isNotBlank(item.getId()) && item.getData() != null) {
                String _json = JSONObject.toJSONString(item.getData());
                String _id = item.getId();
                request.add(new IndexRequest(index).id(_id).source(_json, XContentType.JSON));
            } else {
                logger.warn("批量插入时发现无效的 EsEntity (ID或数据为空)，已跳过该项: {}", item);
            }
        }
        if (request.numberOfActions() == 0) {
            logger.info("没有有效的文档可供批量插入，操作终止。");
            return null;
        }
        try {
            return client.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("es批量添加数据失败，索引: {}，错误信息: {}", index, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据查询条件批量删除文档 (通过 QueryBuilders.matchAllQuery())
     *
     * @param index 索引名称
     * @return BulkByScrollResponse
     */
    public BulkByScrollResponse deleteBatchByIndex(String index) {
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(index);
            // 查询所有（match_all），如果要根据条件删除，需要修改这里的 QueryBuilder
            QueryBuilder builder = QueryBuilders.matchAllQuery();
            request.setQuery(builder);
            return client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("es批量删除失败，索引: {}，错误信息: {}", index, e.getMessage(), e);
        }
        return null;
    }
}