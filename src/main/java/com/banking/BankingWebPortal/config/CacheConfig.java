package com.banking.BankingWebPortal.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("otpAttempts"));  //cache name
        cacheManager.setCaffeine(caffeineConfig());
        return cacheManager;
    }

    public Caffeine<Object, Object> caffeineConfig(){
        return Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)    // Cache entries expire after 15 minutes
                .maximumSize(100)    // Maximum of 100 entries in the cache
                .recordStats();    // For monitoring cache statistic (Optional)
    }
}
