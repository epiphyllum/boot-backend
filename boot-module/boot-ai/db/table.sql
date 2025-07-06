CREATE TABLE ai_model
(
    id               bigint NOT NULL COMMENT 'id',
    platform         varchar(200) NOT NULL COMMENT '所属平台',
    name             varchar(100) NOT NULL COMMENT '模型名称',
    model            varchar(100) NOT NULL COMMENT '模型标识',
    api_url          varchar(200) NOT NULL COMMENT 'API地址',
    api_key          varchar(200) COMMENT 'API秘钥',
    status           tinyint COMMENT '状态  0：停用   1：正常',
    sort             int NOT NULL COMMENT '排序',
    creator          bigint COMMENT '创建者',
    create_date      datetime COMMENT '创建时间',
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='AI模型';
CREATE TABLE ai_chat_conversation
(
    id               bigint NOT NULL COMMENT 'id',
    title            varchar(200) NOT NULL COMMENT '对话标题',
    model_id         bigint NOT NULL COMMENT '模型ID',
    model            varchar(100) NOT NULL COMMENT '模型标识',
    user_id          bigint COMMENT '用户ID',
    create_date      datetime COMMENT '创建时间',
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='AI聊天对话';
CREATE TABLE ai_chat_message
(
    id               bigint NOT NULL COMMENT 'id',
    type             varchar(50) NOT NULL COMMENT '消息类型',
    content          varchar(5000) NOT NULL COMMENT '回答内容',
    model_id         bigint NOT NULL COMMENT '模型ID',
    model            varchar(100) NOT NULL COMMENT '模型标识',
    conversation_id  bigint COMMENT '对话ID',
    create_date      datetime COMMENT '创建时间',
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='AI聊天消息';
