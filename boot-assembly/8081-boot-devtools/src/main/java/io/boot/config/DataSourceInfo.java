/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.config;

import cn.hutool.core.util.StrUtil;
import io.boot.config.query.*;
import io.boot.entity.DataSourceEntity;
import io.boot.utils.DbType;
import io.boot.utils.DbUtils;
import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源信息
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
public class DataSourceInfo {
    /**
     * 数据源ID
     */
    private Long id;
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 数据库URL
     */
    private String connUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    private AbstractQuery dbQuery;

    private Connection connection;

    public DataSourceInfo(DataSourceEntity entity) {
        this.id = entity.getId();
        this.dbType = DbType.valueOf(entity.getDbType());
        this.connUrl = entity.getConnUrl();
        this.username = entity.getUsername();
        this.password = entity.getPassword();

        if(dbType == DbType.MySQL) {
            this.dbQuery = new MySqlQuery();
        }else if(dbType == DbType.Oracle) {
            this.dbQuery = new OracleQuery();
        }else if(dbType == DbType.SQLServer) {
            this.dbQuery = new SqlServerQuery();
        }else if(dbType == DbType.PostgreSQL) {
            this.dbQuery = new PostgreSqlQuery();
        }else if(dbType == DbType.DM) {
            this.dbQuery = new DmQuery();
        }

        try {
            this.connection = DbUtils.getConnection(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public DataSourceInfo(Connection connection) throws SQLException {
        this.id = 0L;
        String productName = connection.getMetaData().getDatabaseProductName();
        productName = StrUtil.subBefore(productName, " ", false);
        this.dbType = DbType.valueOf(productName);

        if(dbType == DbType.MySQL) {
            this.dbQuery = new MySqlQuery();
        }else if(dbType == DbType.Oracle) {
            this.dbQuery = new OracleQuery();
        }else if(dbType == DbType.SQLServer) {
            this.dbQuery = new SqlServerQuery();
        }else if(dbType == DbType.PostgreSQL) {
            this.dbQuery = new PostgreSqlQuery();
        }else if(dbType == DbType.DM) {
            this.dbQuery = new DmQuery();
        }

        this.connection = connection;
    }
}
