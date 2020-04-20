package com.hoangdieuctu.boot.elasticsearch.service;

import com.hoangdieuctu.boot.elasticsearch.model.User;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserService extends ElasticsearchWrapperService<User> {

    @Value("${es.indexes.user.name}")
    private String indexName;

    @Value("${es.indexes.user.type}")
    private String indexType;

    public UserService() {
        super(User.class);
    }

    @Override
    public String getIndexName() {
        return indexName;
    }

    @Override
    public String getIndexType() {
        return indexType;
    }

    public User index(User user) throws IOException {
        return super.index(user.getId(), user);
    }

    public List<User> getByName(String name) throws IOException {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder("name", name));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(MAX_SIZE);

        return super.search(searchSourceBuilder);
    }
}
