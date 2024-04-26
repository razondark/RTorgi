package com.razondark.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class TorgiConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                //.errorHandler(new RestTemplateErrorHandler())
                .build();
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        //return new TransactionAwareCacheManagerProxy(cacheManager);
        return cacheManager;
    }

    @Bean
    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(60, TimeUnit.MINUTES)
                .recordStats();
    }
}
