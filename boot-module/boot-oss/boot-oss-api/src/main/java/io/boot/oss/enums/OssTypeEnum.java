/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.oss.enums;

/**
 * OSS类型枚举
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.1.0
 */
public enum OssTypeEnum {
    /**
     * 七牛云
     */
    QINIU(1),
    /**
     * 阿里云
     */
    ALIYUN(2),
    /**
     * 腾讯云
     */
    QCLOUD(3),
    /**
     * FASTDFS
     */
    FASTDFS(4),
    /**
     * 本地
     */
    LOCAL(5),
    /**
     * MinIO
     */
    MINIO(6);

    private int value;

    OssTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}