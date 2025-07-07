/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.log;

import io.boot.commons.log.BaseLog;
import io.boot.commons.log.enums.LogTypeEnum;
import io.boot.commons.tools.exception.ExceptionUtils;
import io.boot.commons.tools.redis.RedisKeys;
import io.boot.commons.tools.redis.RedisUtils;
import io.boot.commons.tools.utils.ConvertUtils;
import io.boot.entity.SysLogErrorEntity;
import io.boot.entity.SysLogLoginEntity;
import io.boot.entity.SysLogOperationEntity;
import io.boot.service.SysLogErrorService;
import io.boot.service.SysLogLoginService;
import io.boot.service.SysLogOperationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 从Redis队列中获取Log，保存到DB
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Slf4j
@Component
public class LogConsumer implements CommandLineRunner {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private SysLogErrorService sysLogErrorService;
    @Resource
    private SysLogLoginService sysLogLoginService;
    @Resource
    private SysLogOperationService sysLogOperationService;
    private ScheduledExecutorService scheduledService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("log-consumer-schedule-pool-%d").daemon(true).build());

    @Override
    public void run(String... args) {
        //上次任务结束后，等待10秒钟，再执行下次任务
        scheduledService.scheduleWithFixedDelay(() -> {
            try {
                receiveQueue();
            } catch (Exception e) {
                log.error("LogConsumer Error：" + ExceptionUtils.getErrorStackTrace(e));
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    private void receiveQueue() {
        String key = RedisKeys.getSysLogKey();
        //每次插入100条
        int count = 100;
        for (int i = 0; i < count; i++) {
            BaseLog log = (BaseLog) redisUtils.rightPop(key);
            if (log == null) {
                return;
            }

            //登录日志
            if (log.getType() == LogTypeEnum.LOGIN.value()) {
                SysLogLoginEntity entity = ConvertUtils.sourceToTarget(log, SysLogLoginEntity.class);
                sysLogLoginService.save(entity);
            }

            //操作日志
            if (log.getType() == LogTypeEnum.OPERATION.value()) {
                SysLogOperationEntity entity = ConvertUtils.sourceToTarget(log, SysLogOperationEntity.class);
                sysLogOperationService.save(entity);
            }

            //异常日志
            if (log.getType() == LogTypeEnum.ERROR.value()) {
                SysLogErrorEntity entity = ConvertUtils.sourceToTarget(log, SysLogErrorEntity.class);
                sysLogErrorService.save(entity);
            }
        }
    }

}