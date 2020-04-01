package com.example.sentinel.sentineldemo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.sentinel.sentineldemo.service.TestSentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: lida
 * @Date: 2020/4/1 0001 上午 11:44
 * @Description:
 */
@Controller
@RequestMapping("test")
public class TestSentinelController {

    private static final String KEY = "queryOne";

    @Autowired
    private TestSentinelService testSentinelService;

    /**
     * 代码不加任何限流 熔断
     *
     * @return
     */
    @RequestMapping("/getValue_0")
    @ResponseBody
    @SentinelResource("queryZero")
    public String getValue_0(@RequestParam("key") String key) {
        return testSentinelService.getValue_0(key);
    }


    /**
     * 限流实现方式一: 抛出异常的方式定义资源
     *
     * @param key
     * @return
     */
    @RequestMapping("/getValue_1")
    @ResponseBody
    public String getValue_1(@RequestParam("key") String key) {
        Entry entry = null;
        // 资源名
        String resourceName = KEY;
        try {
            // entry可以理解成入口登记
            entry = SphU.entry(resourceName);
            // 被保护的逻辑, 这里为查询接口
            return testSentinelService.getValue_1(key);
        } catch (BlockException blockException) {
            // 接口被限流的时候, 会进入到这里
            return "接口限流, 返回空";
        } finally {
            // SphU.entry(xxx) 需要与 entry.exit() 成对出现,否则会导致调用链记录异常
            if (entry != null) {
                entry.exit();
            }
        }
    }

    /**
     * 限流实现方式二: 注解定义资源
     *
     * @param key
     * @return
     */
    @RequestMapping("/getValue_2")
    @ResponseBody
    public String getValue_2(@RequestParam("key") String key) {
        String res = testSentinelService.getValue_2(key);
        return res;
    }

}
