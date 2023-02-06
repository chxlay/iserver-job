package com.iserver.common.data.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * 实体属性值自动装填处理器（可选的,如果项目中配置了全局,这里就可以省略了）
 *
 * @author Alay
 * @date 2021-09-02 01:59
 * @since
 */
@Slf4j
@Component
public class MpMetaObjectHandler implements MetaObjectHandler {


    @PostConstruct
    public void printInfo() {
        log.info("Mybatis-plus 属性填充配置类准备加载");
    }

    /**
     * 当执行Insert的时候被拦截
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
    }

    /**
     * 当执行Update的时候被拦截
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
    }
}
