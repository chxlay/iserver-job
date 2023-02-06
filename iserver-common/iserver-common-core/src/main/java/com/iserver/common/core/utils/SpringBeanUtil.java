package com.iserver.common.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


/**
 * @author Alay
 * @date 2021-08-04 23:58
 * @project be-helpful
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.context = applicationContext;
    }

    /**
     * 通过 名称获取实例
     *
     * @param beanName
     * @return
     * @throws BeansException
     */
    public static Object getBeanByName(String beanName) throws BeansException {
        return context.getBean(beanName);
    }

    /**
     * 通过类获取实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * 销毁 Bean
     *
     * @param beanName 指定Bean 的名称销毁
     */
    public static void destroyBean(String beanName) {
        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) context;
        BeanDefinitionRegistry beanDefinitionRegistry = (DefaultListableBeanFactory) configurableContext.getBeanFactory();
        beanDefinitionRegistry.removeBeanDefinition(beanName);
    }

}