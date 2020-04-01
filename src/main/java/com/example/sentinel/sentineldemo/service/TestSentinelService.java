package com.example.sentinel.sentineldemo.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lida
 * @Date: 2020/4/1 0001 上午 11:45
 * @Description: 商品查询接口
 */
@Component
@Slf4j
public class TestSentinelService {

    private static final String KEY = "queryTwo";

    /**
     * 代码不加任何限流 熔断
     *
     * @param key
     * @return
     */
    public String getValue_0(String key) {
        System.out.println("获取Value:" + key);
        return "return value :" + key;
    }


    /**
     * 抛出异常的方式定义资源
     *
     * @param key
     * @return
     */
    public String getValue_1(String key) {
        System.out.println("获取Value:" + key);
        return "return value :" + key;
    }

    /**
     * 注解定义资源
     *
     * @param key
     * @return
     */
    @SentinelResource(value = KEY, blockHandler = "blockHandlerMethod", fallback = "queryFallback")
    public String getValue_2(String key) {
        // 模拟调用服务出现异常
        if ("0".equals(key)) {
            throw new RuntimeException();
        }
        return "query value success, " + key;
    }

    public String blockHandlerMethod(String key, BlockException e) {
        return "queryValue error, blockHandlerMethod res: " + key;
    }

    public String queryFallback(String key, Throwable e) {
        return "queryValue error, return fallback res: " + key;
    }

    /**
     * 初始化限流配置
     */
    @PostConstruct
    public void initDegradeRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(KEY);
        // QPS控制在2以内
        rule1.setCount(2);
        // QPS限流
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }


}
