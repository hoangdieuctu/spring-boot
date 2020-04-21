package com.hoangdieuctu.boot.elasticsearch.repository;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ElasticsearchRepository {

    private static Logger logger = LoggerFactory.getLogger(ElasticsearchRepository.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public IndexResponse index(String indexName, String id, String obj) throws IOException {
        logger.debug("Source: {}", obj);

        IndexRequest indexRequest = new IndexRequest(indexName)
                .id(id)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
                .source(obj, XContentType.JSON);
        return restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    public GetResponse get(String indexName, String id) throws IOException {
        logger.debug("Get: {}", id);

        GetRequest getRequest = new GetRequest(indexName, id);
        return restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
    }

    public SearchResponse search(String indexName, SearchSourceBuilder searchSourceBuilder) throws IOException {
        return search(indexName, searchSourceBuilder, null);
    }

    public SearchResponse search(String indexName, SearchSourceBuilder searchSourceBuilder, Scroll scroll) throws IOException {
        logger.debug("Search: {}", searchSourceBuilder);

        SearchRequest request = new SearchRequest(indexName).source(searchSourceBuilder);
        if (scroll != null) {
            request.scroll(scroll);
        }
        return restHighLevelClient.search(request, RequestOptions.DEFAULT);
    }

    public UpdateResponse update(String indexName, String id, String source) throws IOException {
        logger.debug("Update: id={}, source={}", id, source);

        UpdateRequest updateRequest = new UpdateRequest()
                .index(indexName)
                .id(id)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
                .fetchSource(true)
                .doc(source, XContentType.JSON);
        return restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    }

    public DeleteResponse delete(String indexName, String id) throws IOException {
        logger.debug("Delete: {}", id);

        DeleteRequest delRequest = new DeleteRequest(indexName, id)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        return restHighLevelClient.delete(delRequest, RequestOptions.DEFAULT);
    }

    public SearchResponse scrollSearch(String scrollId, Scroll scroll) throws IOException {
        logger.debug("Scroll: {}", scroll);

        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(scroll);
        return restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
    }

    public void clearScrollSearch(String scrollId) throws IOException {
        logger.debug("Clear: {}", scrollId);

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
    }
}
