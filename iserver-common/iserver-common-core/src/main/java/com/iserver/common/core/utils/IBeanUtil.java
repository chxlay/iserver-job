package com.iserver.common.core.utils;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Alay
 * @date 2021-08-17 10:48
 */
public class IBeanUtil {

    /**
     * Setter 函数获取
     *
     * @param clazz
     * @param property
     * @return
     */
    public static Method setMethod(Class<?> clazz, String property) {
        String firstUpCase = IStrUtil.firstUpCase(property);
        return method(clazz, "set" + firstUpCase);
    }

    /**
     * 指定名称获取函数
     *
     * @param clazz
     * @param methodName
     * @return
     */
    public static Method method(Class<?> clazz, String methodName) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; ++i) {
                Method wMeth = descriptors[i].getWriteMethod();
                if (wMeth != null && wMeth.getParameterTypes().length == 1 && wMeth.getName().equals(methodName)) {
                    return wMeth;
                }
            }
            throw new RuntimeException("方法名: [ " + methodName + " ]不存在");
        } catch (IntrospectionException e) {
            throw new RuntimeException("方法名: [ " + methodName + " ]不存在");
        }
    }


}
