/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package io.boot.config;

import apijson.orm.AbstractSQLExecutor;
import apijson.orm.SQLConfig;
import io.boot.commons.dynamic.datasource.config.DynamicDataSource;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class DemoSQLExecutor extends AbstractSQLExecutor<Long> {
    private static final String TAG = "DemoSQLExecutor";

    @Resource
    private DynamicDataSource dataSource;

    @Override
    public Connection getConnection(SQLConfig<Long> config) throws Exception {
        Connection c = dataSource.getConnection();
        return c;
    }
}
