package com.iserver.cloud.job.task;

import com.iserver.cloud.job.feign.TestRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alay
 * @date 2021-10-31 21:44
 * @see {参考 https://gitee.com/chxlay/iserver-job }
 * 这个是测试 Java实体类调用的 调用的类
 */
@Component
public class SpringBeanTaskTest {
    /**
     * 这里并没有真正注入 Feign 服务测试调用，只是示例而已
     */
    @Autowired(required = false)
    private TestRemoteService remoteService;


    public void doSomething() {
        System.out.println("定时任务调用到此处了，这里可以做一些事情，比如调用 订单其他模块进行统计工作");
    }

    public void todo() {
        // 仅仅只是示例，并没有真正注入 feign 调用
        remoteService.remoteTestCall();
        System.out.println("这是第二个Method ,这里可以执行你想要执行的所有操作");
    }
}
