-- 应用接入: isv 或者是商户接入
drop table if exists t_app;
create table t_app
(
    id               bigint not null comment 'id',
    name             varchar(100) comment '名称',
    platform_public  varchar(2048) comment '平台公钥',
    platform_private varchar(2048) comment '平台私钥',
    client_public    varchar(2048) comment '客户端公钥',
    app_id           varchar(64) comment '接入ID',
    isv_id           bigint comment '代理商(ISV)ID, isv_id, merchant_id有且仅有一个',
    merchant_id      bigint comment '商户ID',
    white_ip         varchar(256) comment '白名单',
    permissions      json comment '接口权限',

    creator          bigint comment '创建者',
    create_date      datetime comment '创建时间',
    updater          bigint comment '更新者',
    update_date      datetime comment '更新时间',
    primary key (id)
) engine = InnoDB
  default character set utf8mb4 comment ='接入信息';
