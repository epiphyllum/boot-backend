/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.oss.cloud;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.DefaultGenerateStorageClient;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.SpringContextUtils;
import io.boot.oss.exception.ModuleErrorCode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * FastDFS
 *
 * @author epiphyllum.zhou@gmail.com
 */
public class FastDFSCloudStorageService extends AbstractCloudStorageService {
    private static DefaultGenerateStorageClient defaultGenerateStorageClient;

    static {
        defaultGenerateStorageClient = (DefaultGenerateStorageClient) SpringContextUtils.getBean("defaultGenerateStorageClient");
    }

    public FastDFSCloudStorageService(CloudStorageConfig config){
        this.config = config;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String suffix) {
        StorePath storePath;
        try {
            storePath = defaultGenerateStorageClient.uploadFile("group1", inputStream, inputStream.available(), suffix);
        }catch (Exception ex){
            throw new BootException(ModuleErrorCode.OSS_UPLOAD_FILE_ERROR, ex, ex.getMessage());
        }

        return config.getFastdfsDomain() + "/" + storePath.getPath();
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, suffix);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, suffix);
    }
}