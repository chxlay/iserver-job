package com.iserver.cloud.job.task;

import org.springframework.stereotype.Component;

/**
 * 这个是测试 REST 调用的类
 *
 * @author Alay
 * @date 2021-10-31 21:58
 */
@Component
public class RestTaskTest {

    public void doSomething() {
        System.out.println("定时任务调用到此处了，这里可以做一些事情，比如调用 发送生日祝福");
    }

    public void todo() {
        System.out.println("这是第二个Method ,这里可以执行你想要执行的所有操作");
    }
}
