/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.dynamic.datasource.config;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 多数据源上下文
 */
public class DynamicContextHolder {
    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = ThreadLocal.withInitial(ArrayDeque::new);

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String peek() {
        return CONTEXT_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     *
     * @param dataSource 数据源名称
     */
    public static void push(String dataSource) {
        CONTEXT_HOLDER.get().push(dataSource);
    }

    /**
     * 清空当前线程数据源
     */
    public static void poll() {
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            CONTEXT_HOLDER.remove();
        }
    }

}