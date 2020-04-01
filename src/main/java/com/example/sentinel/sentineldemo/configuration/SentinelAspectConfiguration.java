package com.example.sentinel.sentineldemo.configuration;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lida
 * @Date: 2020/4/1 0001 上午 11:27
 * @Description:Sentinel切面类,支持注解
 */
@Configuration
public class SentinelAspectConfiguration {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect(){
        return  new SentinelResourceAspect();
    }
}
