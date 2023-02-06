package com.iserver.common.core.lang;

/**
 * 唯一性实体类
 *
 * @author Alay
 * @date 2023-01-03 22:39
 */
public interface UniqueEntity<T> {
    /**
     * Equals 方法
     *
     * @param source
     * @return
     */
    boolean isEquals(T source);

    /**
     * 是否式自己
     *
     * @param source
     * @return
     */
    boolean isSelf(T source);


    default boolean equalsAndExcludeSelf(T source) {
        if (null == source) return false;
        return !this.isSelf(source) && this.isEquals(source);
    }
}
