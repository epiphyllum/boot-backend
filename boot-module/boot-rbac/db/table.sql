create table sys_user
(
    id          bigint not null comment 'id',
    username    varchar(50) comment '用户名',
    password    varchar(100) comment '密码',
    real_name   varchar(50) comment '姓名',
    head_url    varchar(200) comment '头像',
    gender      tinyint(4) unsigned comment '性别   0：男   1：女    2：保密',
    email       varchar(100) comment '邮箱',
    mobile      varchar(20) comment '手机号',

    -- begin add field: todo
    dept_id     bigint comment '部门ID: 运营是没有部门ID的',
    user_type   smallint comment '0: 运营, 1: 区域经理, 2: 代理, 3: 业务员',
    -- end add field

    super_admin tinyint unsigned comment '超级管理员   0：否   1：是',
    status      tinyint COMMENT '状态  0：停用   1：正常',
    remark      varchar(200) comment '备注',
    del_flag    tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    updater     bigint comment '更新者',
    update_date datetime comment '更新时间',
    primary key (id),
    unique key uk_username (username),
    key idx_del_flag (del_flag),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='用户管理';

-- 超级管理员: admin/admin
INSERT INTO sys_user(id, username, password, real_name, head_url, gender, email, mobile, dept_id, super_admin, status, remark, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000001, 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '超级管理员', NULL, 1, 'root@boot.io', '13512345678', NULL, 1, 1, NULL, 0, NULL, now(), NULL, now());

-- 用户token
CREATE TABLE sys_user_token
(
    id                   bigint not null comment 'id',
    user_id              bigint comment '用户ID',
    access_token         varchar(32) comment 'accessToken',
    access_token_expire  datetime comment 'accessToken 过期时间',
    refresh_token        varchar(32) comment 'refreshToken',
    refresh_token_expire datetime comment 'refreshToken 过期时间',
    create_date          datetime comment '创建时间',
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户Token';

-- 部门
create table sys_dept
(
    id          bigint not null comment 'id',
    pid         bigint comment '上级ID',
    pids        varchar(500) comment '所有上级ID，用逗号分开',
    name        varchar(50) comment '部门名称',
    leader_id   bigint COMMENT '负责人ID',
    sort        int unsigned comment '排序',
    del_flag    tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    updater     bigint comment '更新者',
    update_date datetime comment '更新时间',
    primary key (id),
    key idx_pid (pid),
    key idx_del_flag (del_flag),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='部门管理';

-- 菜单
create table sys_menu
(
    id          bigint not null comment 'id',
    pid         bigint comment '上级ID，一级菜单为0',
    url         varchar(200) comment '菜单URL',
    menu_type   tinyint unsigned comment '类型   0：菜单   1：按钮',
    open_style  tinyint unsigned COMMENT '打开方式   0：内部   1：外部',
    icon        varchar(50) comment '菜单图标',
    permissions varchar(200) comment '权限标识，如：sys:menu:save',
    sort        int(11) comment '排序',
    del_flag    tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    updater     bigint comment '更新者',
    update_date datetime comment '更新时间',
    primary key (id),
    key idx_pid (pid),
    key idx_del_flag (del_flag),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='菜单管理';

-- 角色
create table sys_role
(
    id          bigint not null comment 'id',
    name        varchar(32) comment '角色名称',
    remark      varchar(100) comment '备注',
    del_flag    tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
    dept_id     bigint comment '部门ID',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    updater     bigint comment '更新者',
    update_date datetime comment '更新时间',
    primary key (id),
    key idx_dept_id (dept_id),
    key idx_del_flag (del_flag),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='角色管理';

-- 用户角色映射
create table sys_role_user
(
    id          bigint not null comment 'id',
    role_id     bigint comment '角色ID',
    user_id     bigint comment '用户ID',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    primary key (id),
    key idx_role_id (role_id),
    key idx_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='角色用户关系';

-- 角色菜单映射
create table sys_role_menu
(
    id          bigint not null comment 'id',
    role_id     bigint comment '角色ID',
    menu_id     bigint comment '菜单ID',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    primary key (id),
    key idx_role_id (role_id),
    key idx_menu_id (menu_id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='角色菜单关系';

-- 角色数据权限
create table sys_role_data_scope
(
    id          bigint not null comment 'id',
    role_id     bigint comment '角色ID',
    dept_id     bigint comment '部门ID',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    primary key (id),
    key idx_role_id (role_id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='角色数据权限';

-- 字典类型
create table sys_dict_type
(
    id          bigint       NOT NULL COMMENT 'id',
    dict_type   varchar(100) NOT NULL COMMENT '字典类型',
    dict_name   varchar(255) NOT NULL COMMENT '字典名称',
    remark      varchar(255) COMMENT '备注',
    sort        int unsigned COMMENT '排序',
    creator     bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    updater     bigint COMMENT '更新者',
    update_date datetime COMMENT '更新时间',
    primary key (id),
    UNIQUE KEY (dict_type)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='字典类型';

-- 字典数据
create table sys_dict_data
(
    id           bigint       NOT NULL COMMENT 'id',
    dict_type_id bigint       NOT NULL COMMENT '字典类型ID',
    dict_label   varchar(255) NOT NULL COMMENT '字典标签',
    dict_value   varchar(255) COMMENT '字典值',
    remark       varchar(255) COMMENT '备注',
    sort         int unsigned COMMENT '排序',
    creator      bigint COMMENT '创建者',
    create_date  datetime COMMENT '创建时间',
    updater      bigint COMMENT '更新者',
    update_date  datetime COMMENT '更新时间',
    primary key (id),
    unique key uk_dict_type_value (dict_type_id, dict_value),
    key idx_sort (sort)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='字典数据';

-- 行政区域
CREATE TABLE sys_region
(
    id          bigint NOT NULL COMMENT 'id',
    pid         bigint COMMENT '上级ID，一级为0',
    name        varchar(100) COMMENT '名称',
    tree_level  tinyint COMMENT '层级',
    leaf        tinyint COMMENT '是否叶子节点  0：否   1：是',
    sort        bigint COMMENT '排序',
    creator     bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    updater     bigint COMMENT '更新者',
    update_date datetime COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='行政区域';

-- 系统参数
create table sys_params
(
    id          bigint not null comment 'id',
    param_code  varchar(32) comment '参数编码',
    param_value varchar(2000) comment '参数值',
    param_type  tinyint(4) unsigned default 1 comment '类型   0：系统参数   1：非系统参数',
    remark      varchar(200) comment '备注',
    del_flag    tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
    creator     bigint comment '创建者',
    create_date datetime comment '创建时间',
    updater     bigint comment '更新者',
    update_date datetime comment '更新时间',
    primary key (id),
    unique key uk_param_code (param_code),
    key idx_del_flag (del_flag),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='参数管理';

-- 登录日志
create table sys_log_login
(
    id           bigint not null comment 'id',
    operation    tinyint unsigned comment '用户操作',
    user_agent   varchar(500) comment '用户代理',
    ip           varchar(160) comment '操作IP',
    creator_name varchar(50) comment '用户名',
    creator      bigint comment '创建者',
    create_date  datetime comment '创建时间',
    primary key (id),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='登录日志';

-- 操作日志
create table sys_log_operation
(
    id             bigint              not null comment 'id',
    module         varchar(32) comment '模块名称，如：sys',
    operation      varchar(50) comment '用户操作',
    request_uri    varchar(200) comment '请求URI',
    request_method varchar(20) comment '请求方式',
    request_params text comment '请求参数',
    request_time   int unsigned        not null comment '请求时长(毫秒)',
    user_agent     varchar(500) comment '用户代理',
    ip             varchar(160) comment '操作IP',
    status         tinyint(4) unsigned not null comment '状态  0：失败   1：成功',
    creator_name   varchar(50) comment '用户名',
    creator        bigint comment '创建者',
    create_date    datetime comment '创建时间',
    primary key (id),
    key idx_module (module),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='操作日志';

-- 系统异常日志
create table sys_log_error
(
    id             bigint not null comment 'id',
    module         varchar(50) comment '模块名称，如：sys',
    request_uri    varchar(200) comment '请求URI',
    request_method varchar(20) comment '请求方式',
    request_params text comment '请求参数',
    user_agent     varchar(500) comment '用户代理',
    ip             varchar(160) comment '操作IP',
    error_info     text comment '异常信息',
    creator        bigint comment '创建者',
    create_date    datetime comment '创建时间',
    primary key (id),
    key idx_module (module),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='异常日志';

-- 菜单国际化
CREATE TABLE sys_language
(
    table_name  varchar(32)  NOT NULL COMMENT '表名',
    table_id    bigint       NOT NULL COMMENT '表主键',
    field_name  varchar(32)  NOT NULL COMMENT '字段名',
    field_value varchar(200) NOT NULL COMMENT '字段值',
    language    varchar(10)  NOT NULL COMMENT '语言',
    primary key (table_name, table_id, field_name, language),
    key idx_table_id (table_id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='国际化';

-- 新闻管理
CREATE TABLE tb_news
(
    id          bigint       NOT NULL COMMENT 'id',
    title       varchar(255) NOT NULL COMMENT '标题',
    content     mediumtext   NOT NULL COMMENT '内容',
    pub_date    datetime COMMENT '发布时间',
    dept_id     bigint COMMENT '创建者dept_id',
    creator     bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    updater     bigint COMMENT '更新者',
    update_date datetime COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='新闻管理';

-- 通知管理
CREATE TABLE sys_notice
(
    id                bigint NOT NULL COMMENT 'id',
    notice_type       int    NOT NULL COMMENT '通知类型',
    title             varchar(200) COMMENT '标题',
    content           text COMMENT '内容',
    receiver_type     tinyint unsigned COMMENT '接收者  0：全部  1：部门',
    receiver_type_ids varchar(500) COMMENT '接收者ID，用逗号分开',
    status            tinyint unsigned COMMENT '发送状态  0：草稿  1：已发布',
    sender_name       varchar(50) COMMENT '发送者',
    sender_date       datetime COMMENT '发送时间',
    creator           bigint COMMENT '创建者',
    create_date       datetime COMMENT '创建时间',
    PRIMARY KEY (id),
    key idx_create_date (create_date)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='通知管理';

-- 用户通知
CREATE TABLE sys_notice_user
(
    receiver_id bigint NOT NULL COMMENT '接收者ID',
    notice_id   bigint NOT NULL COMMENT '通知ID',
    read_status tinyint unsigned COMMENT '阅读状态  0：未读  1：已读',
    read_date   datetime COMMENT '阅读时间',
    PRIMARY KEY (receiver_id, notice_id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='我的通知';

-- 岗位
CREATE TABLE sys_post
(
    id          bigint NOT NULL COMMENT 'id',
    post_code   varchar(100) COMMENT '岗位编码',
    post_name   varchar(100) COMMENT '岗位名称',
    sort        int COMMENT '排序',
    status      tinyint COMMENT '状态  0：停用   1：正常',
    creator     bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    updater     bigint COMMENT '更新者',
    update_date datetime COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='岗位管理';

--  用户岗位关系
CREATE TABLE sys_user_post
(
    id          bigint NOT NULL COMMENT 'id',
    post_id     bigint COMMENT '岗位ID',
    user_id     bigint COMMENT '用户ID',
    creator     bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    primary key (id),
    key idx_post_id (post_id),
    key idx_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='用户岗位关系';

-- 产品
CREATE TABLE tb_product
(
    id          bigint       NOT NULL COMMENT 'id',
    name        varchar(100) NOT NULL COMMENT '产品名称',
    content     mediumtext   NOT NULL COMMENT '产品介绍',
    creator     bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    updater     bigint COMMENT '更新者',
    update_date datetime COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='产品管理';

-- 产品参数
CREATE TABLE tb_product_params
(
    id          bigint NOT NULL COMMENT 'id',
    param_name  varchar(100) COMMENT '参数名',
    param_value varchar(200) COMMENT '参数值',
    product_id  bigint COMMENT '产品ID',
    creator     bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    updater     bigint COMMENT '更新者',
    update_date datetime COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='产品参数管理';

-- excel导入
CREATE TABLE tb_excel_data
(
    id            bigint NOT NULL COMMENT 'id',
    real_name     varchar(100) COMMENT '学生姓名',
    user_identity varchar(100) COMMENT '身份证',
    address       varchar(200) COMMENT '家庭地址',
    join_date     datetime COMMENT '入学日期',
    class_name    varchar(100) COMMENT '班级名称',
    creator       bigint COMMENT '创建者',
    create_date   datetime COMMENT '创建时间',
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4 COMMENT ='Excel导入演示';

