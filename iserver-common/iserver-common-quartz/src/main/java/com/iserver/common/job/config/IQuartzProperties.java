package com.iserver.common.job.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * quartz 配置属性对象化（一般情况下并不需要）
 *
 * @author Alay
 * @date 2021-10-31 19:57
 */
@Data
@ConfigurationProperties(value = "spring.quartz.properties.org")
public class IQuartzProperties {

    private SchedulerProperties scheduler;
    private JobStoreProperties jobStore;
    private ThreadPoolProperties threadPool;
    /**
     * 数据库方式
     */
    private String jobStoreType = "jdbc";


    /**
     * 调度器 scheduler 属性相关配置
     */
    @Data
    public static class SchedulerProperties {
        /**
         * 调度表示名,集群中每一个实例都必须使用相同的名称
         */
        private String instanceName;
        /**
         * 集群中实例Id,必须保证全局唯一,AUTO 为自动给集群中每一个节点分配一个唯一ID
         */
        private String instanceId;
    }


    /**
     * 调度器 scheduler 属性相关配置
     */
    @Data
    public static class JobStoreProperties {
        /**
         * 持久化方式，设置为 数据库持久化方式
         */
        private String clazz;
        /**
         * 数据库代理类,一般 StdJDBCDelegate 能满足大部分的数据库
         */
        private String driverDelegateClass;
        /**
         * 数据库表前缀，默认 QRTZ_
         */
        private String tablePrefix;
        /**
         * 是否加入集群
         */
        private boolean isClustered;
        /**
         * 调度实例失效的检查时间间隔 ms 毫秒,集群间心跳检查时间间隔
         */
        private Integer clusterCheckinInterval;
        /**
         * JobDataMap 是否限制只存 String 类型,默认为false,即不限制
         */
        private boolean useProperties;
        /**
         * 事务设置，是否托管事务JDBC链接，当设置为 TRUE 时,此属性告知 Quartz,在非托管 JDBC 链接上调用 setTransactionIsolation
         */
        private boolean txIsolationLevelReadCommitted = true;
    }


    /**
     * Quartz 线程池相关配置
     */
    @Data
    public static class ThreadPoolProperties {
        /**
         * Quartz 线程池的实现，也可以自定义（一般使用 SimpleThreadPool 即可满足几所所有让你的需求）
         */
        private String clazz;
        /**
         * 线程总数，一般设置 1 -100 的整数，根据系统资源自行配置
         */
        private Integer threadCount;
        /**
         * 线程的优先级（可以是 Thread.MIN_PRIORITY(即1)和Thread.MAX_PRIORITY(即10)之间的任意 int类型，默认值 5）
         */
        private Integer threadPriority;

        private boolean threadsInheritContextClassLoaderOfInitializingThread = true;

        /**
         * 自定义线程池设置：核心线程池大小
         */
        private int corePoolSize = 8;
        /**
         * 自定义线程池设置：最大线程池大小
         */
        private int maxPoolSize = 10;
        /**
         * 自定义线程池设置：线程池队列容量
         */
        private int queueCapacity = 8;


    }
}
