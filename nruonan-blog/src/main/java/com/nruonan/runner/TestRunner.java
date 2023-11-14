package com.nruonan.runner;

import com.nruonan.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Nruonan
 * @description
 */
@Component
public class TestRunner implements CommandLineRunner {
    @Resource
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        String key = "";
    }
}
