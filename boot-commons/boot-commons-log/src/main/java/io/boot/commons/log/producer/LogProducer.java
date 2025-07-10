/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.log.producer;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import io.boot.commons.log.BaseLog;
import io.boot.commons.tools.redis.RedisKeys;
import io.boot.commons.tools.redis.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 日志通过redis队列，异步保存到数据库
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@Component
public class LogProducer {
    @Resource
    private RedisUtils redisUtils;
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("log-producer-pool").build();
    ExecutorService pool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 保存Log到Redis消息队列
     */
    public void saveLog(BaseLog log) {
        String key = RedisKeys.getSysLogKey();

        //异步保存到队列
        pool.execute(() -> redisUtils.leftPush(key, log, RedisUtils.NOT_EXPIRE));
    }
}