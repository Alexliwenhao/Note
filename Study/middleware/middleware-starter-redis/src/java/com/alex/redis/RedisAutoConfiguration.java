package com.alex.redis;

import com.alex.redis.lock.LockAdvice;
import com.alex.redis.lock.LockPointcut;
import org.redisson.api.RedissonClient;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author liwenhao
 * @date 2023/5/17 16:15
 */
@Configuration
public class RedisAutoConfiguration {


    @Bean
    public DefaultPointcutAdvisor lockAdvisor(RedissonClient redissonClient){
        return new DefaultPointcutAdvisor(new LockPointcut(), new LockAdvice(redissonClient));
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setStringSerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.string());
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setStringSerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.string());
        return template;
    }

    @Bean
    public RedisService redisService(RedissonClient redissonClient){
        return new RedisService(redissonClient);
    }



}
