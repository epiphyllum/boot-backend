drop table if exists magic_api_file;
create table magic_api_file
(
    file_path    varchar(512) NOT NULL,
    file_content mediumtext,
    primary key (file_path)
) engine = InnODB
  default charset = utf8mb4;

drop table if exists magic_api_backup;
create table magic_api_backup
(
    `id`          varchar(32) NOT NULL COMMENT '原对象ID',
    `tag`         varchar(32) DEFAULT NULL COMMENT '标签',
    `type`        varchar(32) DEFAULT NULL COMMENT '类型',
    `name`        varchar(64) DEFAULT NULL COMMENT '原名称',
    `content`     mediumtext COMMENT '备份内容',
    `create_date` bigint(13)  NOT NULL COMMENT '备份时间',
    `create_by`   varchar(64) DEFAULT NULL COMMENT '操作人',
    primary key (`id`, `create_date`)
) engine = InnoDB default charset = utf8mb4