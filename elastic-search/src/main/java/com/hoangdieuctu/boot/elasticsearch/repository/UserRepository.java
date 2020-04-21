package com.hoangdieuctu.boot.elasticsearch.repository;

import com.hoangdieuctu.boot.elasticsearch.model.User;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends EsWrapperRepository<User> {

    @Value("${es.indexes.user}")
    private String indexName;

    @Override
    public String getIndexName() {
        return indexName;
    }

    public User save(User user) {
        return super.index(user.getId(), user);
    }

    public List<User> getAll() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(new MatchAllQueryBuilder());
        searchSourceBuilder.size(10);

        return super.scrollSearch(searchSourceBuilder);
    }

    public List<User> getByName(String name) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder("name", name));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(100);

        return super.search(searchSourceBuilder);
    }
}
