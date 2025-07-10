-- 客户端接入
drop table if exists t_client;
create table t_client (
    id          bigint NOT NULL comment 'id',
    name        varchar(100) comment '名称',
    platform_public varchar(2048) comment '平台公钥',
    platform_private varchar(2048) comment '平台私钥',
    client_public varchar(2048) comment '客户端公钥',
    app_id      varchar(100) comment 'AppID',
    app_secret  varchar(100) comment 'AppSecret',
    token       varchar(100) comment 'Token',
    aes_key     varchar(100) comment 'EncodingAESKey',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    updater     bigint comment '更新者',
    update_date datetime comment '更新时间',
    primary key (id)
) engine = InnoDB
  default character set utf8mb4 comment ='客户端API接入信息';

-- 终端接入信息
drop table if exists t;
