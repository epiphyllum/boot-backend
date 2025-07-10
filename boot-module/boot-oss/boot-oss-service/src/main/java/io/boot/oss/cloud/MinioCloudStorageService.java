/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.oss.cloud;

import io.minio.*;

import io.boot.commons.tools.exception.BootException;
import io.boot.oss.exception.ModuleErrorCode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * MinIO 存储
 *
 * @author epiphyllum.zhou@gmail.com
 */
public class MinioCloudStorageService extends AbstractCloudStorageService {
    private MinioClient minioClient;

    public MinioCloudStorageService(CloudStorageConfig config){
        this.config = config;
        //初始化
        init();
    }

    private void init(){
        minioClient = MinioClient.builder().endpoint(config.getMinioEndPoint())
            .credentials(config.getMinioAccessKey(), config.getMinioSecretKey()).build();
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            //如果BucketName不存在，则创建
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(config.getMinioBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(config.getMinioBucketName()).build());
            }

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(config.getMinioBucketName())
                    .object(path)
                    .stream(inputStream, inputStream.available(), -1)
                    .build()
            );

        } catch (Exception e) {
            throw new BootException(ModuleErrorCode.OSS_UPLOAD_FILE_ERROR, e, "");
        }

        return config.getMinioEndPoint() + "/" + config.getMinioBucketName() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getMinioPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getMinioPrefix(), suffix));
    }
}
