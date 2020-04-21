package com.hoangdieuctu.boot.elasticsearch.repository;

import com.hoangdieuctu.boot.elasticsearch.exception.EsException;
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
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class EsWrapperRepository<T> {

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    private JsonUtil jsonUtil;
    private Class<T> _class;

    public EsWrapperRepository() {
        this._class = getGenericTypeClass();
        this.jsonUtil = new JsonUtil();
    }

    private Class<T> getGenericTypeClass() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            return (Class<T>) clazz;
        } catch (Exception e) {
            throw new IllegalStateException("Class is not parametrized with generic type.");
        }
    }

    public abstract String getIndexName();

    public T index(String id, T obj) {
        try {
            Optional<String> opt = jsonUtil.toJson(obj);
            IndexResponse response = elasticsearchRepository.index(getIndexName(), id, opt.get());
            return get(response.getId());
        } catch (Exception ex) {
            throw new EsException("Error while indexing object", ex);
        }
    }

    public T get(String id) {
        try {
            GetResponse response = elasticsearchRepository.get(getIndexName(), id);
            String src = response.getSourceAsString();
            Optional<T> opt = jsonUtil.toObject(src, _class);
            return opt.orElse(null);
        } catch (Exception ex) {
            throw new EsException("Error while getting: " + id, ex);
        }
    }

    public List<T> search(SearchSourceBuilder searchSourceBuilder) {
        try {
            SearchResponse response = elasticsearchRepository.search(getIndexName(), searchSourceBuilder);
            return parseSearchResponse(response);
        } catch (Exception ex) {
            throw new EsException("Error while searching", ex);
        }
    }

    public List<T> scrollSearch(SearchSourceBuilder searchSourceBuilder) {
        List<T> results = new ArrayList<>();
        Scroll scroll = new Scroll(TimeValue.timeValueSeconds(5));

        try {
            SearchResponse response = elasticsearchRepository.search(getIndexName(), searchSourceBuilder, scroll);
            while (true) {
                List<T> scrollResults = parseSearchResponse(response);
                if (scrollResults.isEmpty()) {
                    break;
                }

                results.addAll(scrollResults);
                response = elasticsearchRepository.scrollSearch(response.getScrollId(), scroll);
            }
            clearScroll(response.getScrollId());

            return results;
        } catch (Exception ex) {
            throw new EsException("Error while scrolling search", ex);
        }
    }

    public T update(String id, T obj) {
        try {
            Optional<String> opt = jsonUtil.toJson(obj);
            UpdateResponse response = elasticsearchRepository.update(getIndexName(), id, opt.get());
            String src = response.getGetResult().sourceAsString();
            return jsonUtil.toObject(src, _class).orElse(null);
        } catch (Exception ex) {
            throw new EsException("Error while updating: " + id, ex);
        }
    }

    public void delete(String id) {
        try {
            elasticsearchRepository.delete(getIndexName(), id);
        } catch (Exception ex) {
            throw new EsException("Error while deleting: " + id, ex);
        }
    }

    private List<T> parseSearchResponse(SearchResponse response) {
        List<T> results = new ArrayList<>();
        if (response == null) {
            return results;
        }

        SearchHits searchHits = response.getHits();
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

    private void clearScroll(String scrollId) throws IOException {
        elasticsearchRepository.clearScrollSearch(scrollId);
    }
}
