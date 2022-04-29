//package com.huangrx.elastic.util;
//
//import com.alibaba.fastjson.JSON;
//import com.folidaymall.merchant.merchantapi.model.enums.EquitySendRecordRecordType;
//import com.folidaymall.merchant.merchantapi.model.enums.EquityUserStatus;
//import com.folidaymall.merchant.merchantapi.util.EncryptUtil;
//import com.vip.vjtools.vjkit.time.DateFormatUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.lucene.search.TotalHits;
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.support.master.AcknowledgedResponse;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.elasticsearch.client.indices.CreateIndexResponse;
//import org.elasticsearch.client.indices.GetIndexRequest;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.core.TimeValue;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.Operator;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * @author hrenxiang
// * @version v1.0
// * @since 2022-01-12 2:56 PM
// */
//@Slf4j
//@Component
//public class ESUtils {
//
//    @Autowired
//    private RestHighLevelClient client;
//
//    /**
//     * 用于本类方法构建搜索请求
//     *
//     * @param indexName 索引名
//     * @return ES搜索请求
//     */
//    private SearchRequest buildSearchRequest(String indexName) {
//        if (StringUtils.isEmpty(indexName)) {
//            return new SearchRequest();
//        } else {
//            return new SearchRequest(indexName);
//        }
//    }
//
//    /**
//     * 解析查询结果集
//     *
//     * @param response ES搜索响应
//     * @return ES搜索
//     */
//    private SearchResponseVO analysisResult(SearchResponse response) {
//        SearchResponseVO responseVO = new SearchResponseVO();
//        SearchHits hits = response.getHits();
//        // 总命中的记录数
//        TotalHits totalHits = hits.getTotalHits();
//        log.info("ES---共查询到数据{}条", totalHits.value);
//        responseVO.setTotalHits(totalHits);
//        // 获取内层hits的_source 数据
//        List<String> data = Stream.of(hits.getHits()).map(SearchHit::getSourceAsString).collect(Collectors.toList());
//        log.info("ES---查询到的数据是：{}", data);
//        responseVO.setData(data);
//        return responseVO;
//    }
//
//    /**
//     * 判断索引是否存在
//     *
//     * @param indexName 索引名
//     * @return ture：已存在 false：不存在
//     * @throws IOException ES执行失败
//     */
//    public boolean existIndex(String indexName) throws IOException {
//        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
//        return client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
//    }
//
//    /**
//     * 如果索引不存在则创建索引，若存在则不创建
//     *
//     * @param indexName 索引名
//     * @return ture：已创建 false：未创建
//     * @throws IOException ES执行失败
//     */
//    public boolean createIndex(String indexName) throws IOException {
//        if (this.existIndex(indexName)) {
//            return true;
//        }
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
//        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//        return createIndexResponse.isAcknowledged();
//    }
//
//    /**
//     * 删除索引
//     *
//     * @param indexName 索引名
//     * @return true：已删除 false：未删除
//     * @throws Exception ES执行失败
//     */
//    public boolean deleteIndex(String indexName) throws Exception {
//        // 判断索引是否存在
//        if (!this.existIndex(indexName)) {
//            return true;
//        }
//        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
//        AcknowledgedResponse acknowledgedResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//        return acknowledgedResponse.isAcknowledged();
//    }
//
//    /**
//     * 新增文档
//     *
//     * @param indexName 索引名
//     * @param id        存储的主键
//     * @param content   文档内容
//     * @throws Exception ES执行失败
//     */
//    public boolean addDocument(String indexName, String id, String content) throws Exception {
//        if (!this.createIndex(indexName)) {
//            return false;
//        }
//
//        IndexRequest indexRequest = new IndexRequest(indexName);
//        // 设置超时时间
//        indexRequest.id(id);
//        indexRequest.timeout(TimeValue.timeValueSeconds(1));
//        // 转换为json字符串
//        indexRequest.source(content, XContentType.JSON);
//        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//        return indexResponse.status().getStatus() == 200;
//    }
//
//    /**
//     * 判断是否存在文档
//     *
//     * @param indexName 索引名
//     * @param id        存储的主键
//     * @throws Exception ES执行失败
//     */
//    public boolean isExistsDocument(String indexName, String id) throws Exception {
//        // 判断是否存在文档
//        GetRequest getRequest = new GetRequest(indexName, id);
//        // 不获取返回的_source的上下文
//        getRequest.fetchSourceContext(new FetchSourceContext(false));
//        getRequest.storedFields("_none_");
//        return client.exists(getRequest, RequestOptions.DEFAULT);
//    }
//
//    /**
//     * 获取文档
//     *
//     * @param indexName 索引名
//     * @param id        存储的主键
//     * @throws Exception ES执行失败
//     */
//    public String getDocument(String indexName, String id) throws Exception {
//        // 获取文档
//        GetRequest getRequest = new GetRequest(indexName, id);
//        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
//        return getResponse.getSourceAsString();
//    }
//
//    /**
//     * 更新文档
//     *
//     * @param indexName 索引名
//     * @param id        存储的主键
//     * @param content   更新的内容
//     * @throws Exception ES执行失败
//     */
//    public boolean updateDocument(String indexName, String id, String content) throws Exception {
//        // 更新文档
//        UpdateRequest updateRequest = new UpdateRequest(indexName, id);
//        updateRequest.timeout(TimeValue.timeValueSeconds(1));
//        updateRequest.doc(content, XContentType.JSON);
//        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
//        return updateResponse.status().getStatus() == 200;
//    }
//
//    /**
//     * 删除文档
//     *
//     * @param indexName 索引名
//     * @param id        存储的主键
//     * @throws Exception ES执行失败
//     */
//    public boolean deleteDocument(String indexName, String id) throws Exception {
//        if (!this.isExistsDocument(indexName, id)) {
//            return true;
//        }
//
//        // 删除文档
//        DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
//        deleteRequest.timeout(TimeValue.timeValueSeconds(1));
//        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
//        return deleteResponse.status().getStatus() == 200;
//    }
//
//    /**
//     * 批量插入
//     *
//     * @param indexName 索引名
//     * @param contents  新增内容
//     * @return true：已成功 false：未成功
//     * @throws IOException ES执行失败
//     */
//    public boolean saveBulk(String indexName, List<? extends ESDto> contents) throws IOException {
//        // 批量插入
//        BulkRequest bulkRequest = new BulkRequest();
//        bulkRequest.timeout(TimeValue.timeValueSeconds(1));
//        contents.forEach(content -> {
//            bulkRequest.add(
//                    new IndexRequest(indexName)
//                            .id(content.getId())
//                            .source(JSON.toJSONString(content), XContentType.JSON)
//            );
//        });
//        BulkResponse bulkItemResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
//        return !bulkItemResponse.hasFailures();
//    }
//
//    /**
//     * 分页查询数据
//     *
//     * @param indexName 索引名
//     * @throws Exception ES执行失败
//     */
//    public List<String> searchPaging(String indexName, Integer pageNum, Integer pageSize) throws Exception {
//        // 构建搜索请求
//        SearchRequest searchRequest = this.buildSearchRequest(indexName);
//        // 条件构造
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        // 第几页
//        searchSourceBuilder.from(pageNum);
//        // 每页多少条数据
//        searchSourceBuilder.size(pageSize);
//        // 匹配所有
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        // 解析结果
//        return this.analysisResult(searchResponse).getData();
//    }
//
//    /**
//     * 根据条件分页查询数据
//     *
//     * @param indexName 索引名
//     * @throws Exception ES执行失败
//     */
//    public List<String> searchByConditionsPaging(String indexName, Integer pageNum, Integer pageSize, String idCard, String mobile, String orderId,
//                                                 String checkStatus, String roomNum, String travelDate, String returnDate, String shareID,
//                                                 String productId, String userName, String isOne, String confirmationNumber, Boolean needEncrypt,
//                                                 String activityMobile, Integer activityType, Integer merchantId) throws Exception {
//        // 构建搜索请求
//        SearchRequest searchRequest = this.buildSearchRequest(indexName);
//        // 条件构造
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        // 第几页
//        searchSourceBuilder.from(pageNum);
//        // 每页多少条数据
//        searchSourceBuilder.size(pageSize);
//        // 按指定条件排序
//        searchSourceBuilder.sort("stayInTime", SortOrder.DESC);
//
//        // 匹配相关字段
//        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
//        if (StringUtils.isNotBlank(orderId)) {
//            boolQuery.must(QueryBuilders.matchQuery("confirmCode", orderId).operator(Operator.AND));
//        }
//        if (StringUtils.isNotBlank(confirmationNumber)) {
//            boolQuery.must(QueryBuilders.matchQuery("confirmNum", confirmationNumber).operator(Operator.AND));
//        }
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(shareID)) {
//            boolQuery.must(QueryBuilders.matchQuery("shareId", shareID).operator(Operator.AND));
//        }
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(idCard)) {
//            String enIdCard = EncryptUtil.XORencode(idCard, EncryptUtil.defaultKey);
//            boolQuery.must(QueryBuilders.matchQuery("idCardSix", enIdCard).operator(Operator.AND));
//        }
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(mobile)) {
//            boolQuery.must(QueryBuilders.matchQuery("phoneNum", mobile).operator(Operator.AND));
//        }
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(roomNum)) {
//            boolQuery.must(QueryBuilders.matchQuery("roomNum", roomNum).operator(Operator.AND));
//        }
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(userName)) {
//            boolQuery.must(QueryBuilders.matchQuery("userName", userName).operator(Operator.AND));
//        }
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkStatus)) {
//            if ("CHECKED IN".equals(checkStatus)) {
//                boolQuery.must(QueryBuilders.matchQuery("status", EquityUserStatus.ACTIVATED.getCode()).operator(Operator.AND));
//            }
//            if ("CHECKED OUT".equals(checkStatus)) {
//                boolQuery.must(QueryBuilders.matchQuery("status", EquityUserStatus.CHECKED_OUT.getCode()).operator(Operator.AND));
//            }
//        }
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(activityMobile)) {
//            boolQuery.must(QueryBuilders.matchQuery("activityPhone", activityMobile).operator(Operator.AND));
//        }
//        if (activityType != null) {
//            boolQuery.must(QueryBuilders.matchQuery("activityType", activityType).operator(Operator.AND));
//        }
//        try {
//            if (org.apache.commons.lang3.StringUtils.isNotBlank(travelDate)) {
//                boolQuery.must(QueryBuilders.matchQuery("stayInTime", DateFormatUtil.parseDate(DateFormatUtil.PATTERN_ISO_ON_DATE, travelDate)).operator(Operator.AND));
//            }
//            if (org.apache.commons.lang3.StringUtils.isNotBlank(returnDate)) {
//                boolQuery.must(QueryBuilders.matchQuery("actualDepartureTime", DateFormatUtil.parseDate(DateFormatUtil.PATTERN_ISO_ON_DATE, returnDate)).operator(Operator.AND));
//            }
//        } catch (ParseException e) {
//            log.warn("时间格式化异常");
//        }
//        if (merchantId != null) {
//            boolQuery.must(QueryBuilders.matchQuery("merchantId", merchantId).operator(Operator.AND));
//        } else {
//            throw new Exception("merchantId 不能为 null");
//        }
//
//        searchSourceBuilder.query(boolQuery);
//        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        // 解析结果
//        return this.analysisResult(searchResponse).getData();
//    }
//
//    /**
//     * 根据shareId查询同房间住客
//     *
//     * @param indexName 索引名
//     * @param shareId   同房间标识
//     * @return 房间中每个住客信息的字符串列表
//     * @throws IOException ES执行失败
//     */
//    public List<String> searchEquityUsersByShareId(String indexName, String shareId) throws IOException {
//        // 构建搜索请求
//        SearchRequest searchRequest = this.buildSearchRequest(indexName);
//        // 条件构造
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        // 匹配相同shareId的住客
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.must(QueryBuilders.matchQuery("shareId", shareId).operator(Operator.AND));
//        boolQuery.must(QueryBuilders.matchQuery("status", EquityUserStatus.CHECKED_OUT.getCode()).operator(Operator.AND));
//        boolQuery.must(QueryBuilders.matchQuery("valid", Boolean.TRUE));
//        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));
//        searchRequest.source(searchSourceBuilder.query(boolQuery));
//        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        // 解析结果
//        return this.analysisResult(searchResponse).getData();
//    }
//
//    /**
//     * 根据shareId查询发放的券
//     *
//     * @param indexName 索引名
//     * @param shareId   同房间标识
//     * @return 房间中发放的所有券的记录
//     * @throws IOException ES执行失败
//     */
//    public List<String> searchEquitySendRecordByShareId(String indexName, String shareId) throws IOException {
//        // 构建搜索请求
//        SearchRequest searchRequest = this.buildSearchRequest(indexName);
//        // 条件构造
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        // 匹配相同shareId的住客
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.must(QueryBuilders.matchQuery("shareId", shareId).operator(Operator.AND));
//        boolQuery.must(QueryBuilders.matchQuery("recordType", EquitySendRecordRecordType.ACTIVITY.getCode()).operator(Operator.AND));
//        boolQuery.must(QueryBuilders.matchQuery("valid", Boolean.TRUE));
//        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));
//        searchRequest.source(searchSourceBuilder.query(boolQuery));
//        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        // 解析结果
//        return this.analysisResult(searchResponse).getData();
//    }
//
//    /**
//     * 根据confirmCode查询订单相关权益
//     *
//     * @param indexName 索引名
//     * @return 订单所对应的权益列表
//     * @throws IOException ES执行失败
//     */
//    public List<String> searchUserPackagesByConfirmCode(String indexName, String confirmCode) throws IOException {
//        // 构建搜索请求
//        SearchRequest searchRequest = this.buildSearchRequest(indexName);
//        // 条件构造
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        // 匹配相同shareId的住客
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.must(QueryBuilders.matchQuery("confirmCode", confirmCode).operator(Operator.AND));
//        boolQuery.must(QueryBuilders.matchQuery("valid", Boolean.TRUE));
//        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));
//        searchRequest.source(searchSourceBuilder.query(boolQuery));
//        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        // 解析结果
//        return this.analysisResult(searchResponse).getData();
//    }
//
//}
