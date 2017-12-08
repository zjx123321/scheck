package com.simple.scheck.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class JredisFactory {

    @Resource
    private RedisTemplate redisTemplate;


    public RedisTemplate<Object,Object> redisTemplate(){
        RedisTemplate template = redisTemplate;
        //定义key序列化方式
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型会出现异常信息;需要我们上面的自定义key生成策略，一般没必要
        //定义value的序列化方式
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        template.setKeySerializer(redisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(redisSerializer);
        template.afterPropertiesSet();
        return redisTemplate;
    }


    public static   RedisTemplate  setRedisSerializerString(RedisTemplate redisTemplate){
        //定义key序列化方式
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型会出现异常信息;需要我们上面的自定义key生成策略，一般没必要
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        return redisTemplate;

    }

}
