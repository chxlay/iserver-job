package com.iserver.common.data.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MP 的配置（可选的,如果项目中配置了全局,这里就可以省略了）
 *
 * @author Alay
 * @date 2021-11-09 14:13
 */
@Slf4j
@Configuration
public class MpConfig {

    /**
     * MP 新的配置插件配置
     * 官网说明：@see {https://mp.baomidou.com/guide/page.html}
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        /**
         * 乐观锁
         */
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        /**
         * 分页插件
         */
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        /**
         * 防止全表更新与删除
         */
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        log.info("Mybatis-Plus 插件装配完毕");

        return interceptor;
    }
}
