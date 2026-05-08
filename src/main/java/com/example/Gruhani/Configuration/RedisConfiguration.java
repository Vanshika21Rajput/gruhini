
    package com.example.Gruhani.Configuration;

import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import io.lettuce.core.RedisURI;
import io.lettuce.core.RedisClient;

import java.time.Duration;

    @Configuration
    public class RedisConfiguration {


        @Value("${spring.data.redis.password}")
        String redisPass;

            @Bean
            public LettuceConnectionFactory redisConnectionFactory() {
                RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration();
                serverConfig.setHostName("redis-15865.crce283.ap-south-1-2.ec2.cloud.redislabs.com");
                serverConfig.setPort(15865);
                serverConfig.setUsername("default");
                serverConfig.setPassword(redisPass);

                return new LettuceConnectionFactory(serverConfig);  // no SSL config needed
            }



        @Bean
        public RedisTemplate<String,Object> template(RedisConnectionFactory factory)
        {
          //  System.out.println("connection"+factory.getConnection());
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(factory);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            return redisTemplate;
        }
        @Bean
        public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                    .serializeValuesWith(
                            RedisSerializationContext.SerializationPair.fromSerializer(
                                    new GenericJackson2JsonRedisSerializer()
                            )
                    ).entryTtl(Duration.ofMinutes(10));

            return RedisCacheManager.builder(connectionFactory)
                    .cacheDefaults(config)
                    .build();
        }
    }



