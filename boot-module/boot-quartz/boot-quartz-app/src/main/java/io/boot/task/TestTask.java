/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Slf4j
@Component("testTask")
public class TestTask implements ITask {

    @Override
    public void run(String params) {
        log.debug("TestTask定时任务正在执行，参数为：{}", params);
    }
}