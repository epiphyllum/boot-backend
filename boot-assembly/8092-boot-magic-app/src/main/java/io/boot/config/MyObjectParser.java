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

import apijson.NotNull;
import apijson.RequestMethod;
import apijson.orm.AbstractObjectParser;
import apijson.orm.Join;
import apijson.orm.SQLConfig;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpSession;

import java.util.List;


/**对象解析器，用来简化 Parser
 * @author Lemon
 */
public class MyObjectParser extends AbstractObjectParser<Long> {

    public MyObjectParser(HttpSession session, @NotNull JSONObject request, String parentPath, SQLConfig arrayConfig
            , boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
        super(request, parentPath, arrayConfig, isSubquery, isTable, isArrayMainTable);
    }

    @Override
    public SQLConfig newSQLConfig(RequestMethod method, String table, String alias, JSONObject request, List<Join> joinList, boolean isProcedure) throws Exception {
        return MySQLConfig.newSQLConfig(method, table, alias, request, joinList, isProcedure);
    }

}
