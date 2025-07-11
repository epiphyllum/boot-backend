# 说明文档
## 1. 模块说明

### boot-commons
公共模块

| 模块                 | 描述                                                                                                            |
|--------------------|---------------------------------------------------------------------------------------------------------------|
| dependencies       | 管理公共依赖                                                                                                        |
| tools              | feign配置+web配置+redis工具bean+validation, Result, ServiceConstant, ErrorCode, 基础messages_common validation_common |
| mybatis            | 基础封装+数据范围权限                                                                                                   |
| security           | 授权封装+SecurityUser                                                                                             |
| dynamic-datasource | 数据源管理, 包含@Datasource                                                                                          |
| jimureport         | 积木报表集成配置                                                                                                      |
| lock               | 分布式锁实现:@Lock4j, lockTemplate                                                                                  |
| log                | (操作日志+登录日志+500日志)输出到redis; @LogOperation                                                                      |
| swagger            | Swagger集成配置                                                                                                   |
| xxl-job            | xxl-job节点集成配置                                                                                                 |

公共模块依赖关系:

| 模块                 | 依赖                            |
|--------------------|-------------------------------|
| dynamic-datasource |                               |
| lock               |                               |
| dependencies       |                               |
| log                | tools + security              |
| mybatis            | tools + security + datasource |`

### boot-assembly
可执行java编译项目

| 项目名称             | 描述          | URI前缀       | 使用端口 |
|------------------|-------------|-------------|------|
| boot-gateway     | 网关          | /sys        | 8080 |
| boot-devtools    | 开发工具        | /devtools   | 8081 |
| boot-monitor     | 监控          | /sys        | 8082 |
| boot-omni-app    | 主管理后台       | /sys        | 8083 |
| boot-job         | xll-job执行节点 | /sys        | 8084 |
| boot-payment-app | 支付模块        | /payment    | 8085 |
| boot-message-app | 邮件和短信       | /message    | 8086 |
| boot-oss-app     | 文件存储        | /oss        | 8087 |
| boot-ai-app      | AI模块        | /ai         | 8088 |
| boot-mp-app      | 公众号管理       | /mp         | 8089 |
| boot-magic-app   | magic-api例子 | /magic      | 8090 |
| boot-api-app     | 接口接入        | /api        | 8091 |
| boot-api-client  | 接口客户端测试     | /api-client | 8092 |
| boot-demo-app    | 演示          | /demo       | 8093 |
| boot-novo-app    | 演示例子        | /novo       | 8094 |

注意事项:
1. boot-assembly下所有模块都是springboot打包项目  
2. 每个子模块顶级包名: io.boot
3. 每个子模块下包含 io.boot.XxxApp.java, ModuleConfigImpl.java
4. 模块下boot-auth.yml用来管理需要忽略登录于权限管理的url
5. 一个assembly模块可以聚合多个boot-module下的多个业务模块
6. assembly模块下, 可通过boot-auth.yml来配置免登录访问的url


### boot-module
这里包含具体的业务模块, 单不作为专门的springboot项目打包
1. 每个模块的顶级包名: io.boot.xxx  
2. boot-xxx-api: 模块对外接口dto, enums, feign     
3. boot-xxx-service: 包含需要对外的controller, config等   
4. 每个模块可以i18n/messages_xxx, i18n/validation_xxx资源包   
5. 多个boot-xxx-service模块, 可以在boot-assembly下作为一个app打包  

| 模块名称         | 描述                         |
|--------------|----------------------------|
| boot-rbac    | 基本权限管理: 包含rbac+参数管理+全局信息管理 |
| boot-ai      | AI模块                       |
| boot-api     | 接入模块                       |
| boot-demo    | 演示                         |
| boot-flow    | 流程模块                       |
| boot-message | 邮件与短信                      |
| boot-mp      | 公众号管理                      |
| boot-novo    | 演示                         |
| boot-oss     | 文件云存储                      |
| boot-payment | 微信支付宝接入                    |
| boot-quartz  | quartz                     |

### boot-poc
概念模块, 用于做一些概念验证

