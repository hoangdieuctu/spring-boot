package com.hoangdieuctu.boot.elasticsearch.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build()))
                        .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                                .setConnectTimeout(10000).setSocketTimeout(60000)));
    }
}
