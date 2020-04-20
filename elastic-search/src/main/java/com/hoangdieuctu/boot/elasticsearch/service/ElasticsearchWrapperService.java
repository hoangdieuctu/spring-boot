package com.hoangdieuctu.boot.elasticsearch.service;

import com.hoangdieuctu.boot.elasticsearch.exception.EsException;
import com.hoangdieuctu.boot.elasticsearch.repository.ElasticsearchRepository;
import com.hoangdieuctu.boot.elasticsearch.util.JsonUtil;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ElasticsearchWrapperService<T> {

    protected static final int MAX_SIZE = 10000;

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    private JsonUtil jsonUtil = new JsonUtil();

    private Class<T> _class;

    public ElasticsearchWrapperService(Class<T> _class) {
        if (_class == null) {
            throw new EsException("The parameter _class and _index could no be null");
        }
        this._class = _class;
    }

    public abstract String getIndexName();

    public abstract String getIndexType();

    public T index(String id, T obj) throws IOException {
        Optional<String> opt = jsonUtil.toJson(obj);
        IndexResponse response = elasticsearchRepository.index(getIndexName(), getIndexType(), id, opt.get());
        return get(response.getId());
    }

    public T get(String id) throws IOException {
        GetResponse response = elasticsearchRepository.get(getIndexName(), getIndexType(), id);
        String src = response.getSourceAsString();
        Optional<T> opt = jsonUtil.toObject(src, _class);
        return opt.orElse(null);
    }

    public List<T> search(SearchSourceBuilder searchSourceBuilder) throws IOException {
        SearchResponse response = elasticsearchRepository.search(getIndexName(), searchSourceBuilder);
        SearchHits searchHits = response.getHits();
        return parseSearchResponse(searchHits);
    }

    public List<T> scrollSearch(SearchSourceBuilder searchSourceBuilder) throws IOException {
        List<T> results = new ArrayList<>();

        Scroll scroll = new Scroll(TimeValue.timeValueSeconds(5));
        SearchResponse response = elasticsearchRepository.search(getIndexName(), searchSourceBuilder, scroll);

        String scrollId = response.getScrollId();
        SearchHits searchHits = response.getHits();
        while (searchHits.getHits() != null && searchHits.getHits().length > 0) {
            List<T> scrollResults = parseSearchResponse(searchHits);
            results.addAll(scrollResults);

            SearchResponse scrollResponse = elasticsearchRepository.scrollSearch(scrollId, scroll);
            scrollId = scrollResponse.getScrollId();
            searchHits = scrollResponse.getHits();
        }
        elasticsearchRepository.clearScrollSearch(scrollId);

        return results;
    }

    public T update(String id, T obj) throws IOException {
        Optional<String> opt = jsonUtil.toJson(obj);
        UpdateResponse response = elasticsearchRepository.update(getIndexName(), getIndexType(), id, opt.get());
        String src = response.getGetResult().sourceAsString();
        return jsonUtil.toObject(src, _class).orElse(null);
    }

    public void delete(String id) throws IOException {
        elasticsearchRepository.delete(getIndexName(), getIndexType(), id);
    }

    private List<T> parseSearchResponse(SearchHits searchHits) {
        List<T> results = new ArrayList<>();
        if (searchHits == null) {
            return results;
        }

        SearchHit[] hits = searchHits.getHits();
        if (hits == null || hits.length == 0) {
            return results;
        }

        for (SearchHit hit : hits) {
            String src = hit.getSourceAsString();
            Optional<T> opt = jsonUtil.toObject(src, _class);
            opt.ifPresent(results::add);
        }

        return results;
    }
}
