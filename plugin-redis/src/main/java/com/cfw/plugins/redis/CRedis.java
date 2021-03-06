package com.cfw.plugins.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by Duskrain on 2017/3/17.
 */
@Component
public class CRedis {
    //@Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void set(String key,String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }

    public void set(String key,String value,long timeout){
        this.set(key,value);
        stringRedisTemplate.expire(key,timeout, TimeUnit.SECONDS);
    }

    public void set(String key,String value,Long timeout, TimeUnit unit){
        this.set(key,value);
        stringRedisTemplate.expire(key,timeout,unit);
    }

    public long incr(String key){
        return this.stringRedisTemplate.opsForValue().increment(key,1L);
    }

    public void expire(String key,long timeout){
        this.expire(key,timeout,TimeUnit.SECONDS);
    }

    public void expire(String key,long timeout,TimeUnit unit){
        this.stringRedisTemplate.expire(key,timeout,unit);
    }
}
