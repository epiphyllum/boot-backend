/**
 * Copyright (c) 2016-2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.xxl.job.properties;

import lombok.Data;

/**
 * xxl-job属性
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
public class AdminProperties {
	/**
	 * 调度中心部署地址 [选填]
	 * 如调度中心集群部署存在多个地址则用逗号分隔。
	 * 执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册
	 */
	private String addresses;
}
