package com.iserver.common.job.config;

import com.iserver.common.job.support.IJobFactory;
import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author Alay
 * @date 2021-10-31 16:10
 * @see {cron表达式 https://www.cnblogs.com/Alay/p/15488175.html}
 * @see {参考 https://gitee.com/chxlay/iserver-job }
 */
@Configuration
@EnableConfigurationProperties(value = IQuartzProperties.class)
public class IQuartzConfig {

    @Autowired
    private QuartzProperties quartzProperties;
    @Autowired
    private IQuartzProperties iQuartzProperties;
    @Autowired(required = false)
    private List<SchedulerFactoryBeanCustomizer> customizers;


    /**
     * 根据配置文件的属性配置集群的示例工厂
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        // Job 实例属性封装的工厂类
        factoryBean.setJobFactory(new IJobFactory());
        // 配置文件属性配置
        Properties properties = this.parseProperties(quartzProperties.getProperties());
        factoryBean.setQuartzProperties(properties);

        factoryBean.setAutoStartup(true);
        // 延时5秒启动
        factoryBean.setStartupDelay(5);

        // 集群配置
        if (this.customizers != null) {
            Iterator var2 = this.customizers.iterator();
            while (var2.hasNext()) {
                SchedulerFactoryBeanCustomizer customizer = (SchedulerFactoryBeanCustomizer) var2.next();
                customizer.customize(factoryBean);
            }
        }

        // 如果需要自定义线程池
        // factoryBean.setTaskExecutor(this.schedulerThreadPool());

        return factoryBean;
    }

    /**
     * 配置调度器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Scheduler scheduler() {
        // SchedulerFactoryBean 配置中已经配置了 Scheduler,如果需要自定义配置,这再此配置即可
        Scheduler scheduler = null;
        return scheduler;
    }


    /**
     * 初始化监听器
     *
     * @return
     */
    //@Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    /**
     * 自定义线程池配置（如果需要,大部分情况使用 SimpleThreadPool 即可满足）
     *
     * @return
     */
    // @Bean
    @ConditionalOnMissingBean
    public Executor schedulerThreadPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(iQuartzProperties.getThreadPool().getCorePoolSize());
        taskExecutor.setMaxPoolSize(iQuartzProperties.getThreadPool().getMaxPoolSize());
        taskExecutor.setQueueCapacity(iQuartzProperties.getThreadPool().getQueueCapacity());
        return taskExecutor;
    }



    /**
     * 属性配置接卸为 Properties 实例
     *
     * @param source
     * @return
     */
    private Properties parseProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

}
