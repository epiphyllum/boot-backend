/**
 * Copyright (c) 2016-2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 演示xxl-job定时任务
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Slf4j
@Component
public class TestJob {
    /**
     * 任务示例
     */
    @XxlJob("testHandler")
    public ReturnT<String> testHandler(String param) throws Exception {
        log.info("xxl-job 正在执行，参数：{}", param);
        return ReturnT.SUCCESS;
    }
}
