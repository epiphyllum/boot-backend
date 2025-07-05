CREATE TABLE tb_alipay_notify_log
(
    id               bigint NOT NULL COMMENT 'id',
    out_trade_no     bigint COMMENT '订单号',
    total_amount     decimal(10, 2) COMMENT '订单金额',
    buyer_pay_amount decimal(10, 2) COMMENT '付款金额',
    receipt_amount   decimal(10, 2) COMMENT '实收金额',
    invoice_amount   decimal(10, 2) COMMENT '开票金额',
    notify_id        varchar(50) COMMENT '通知校验ID',
    buyer_id         varchar(50) COMMENT '买家支付宝用户号',
    seller_id        varchar(50) COMMENT '卖家支付宝用户号',
    trade_no         varchar(50) COMMENT '支付宝交易号',
    trade_status     varchar(50) COMMENT '交易状态',
    create_date      datetime COMMENT '创建时间',
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='支付宝回调日志';

CREATE TABLE tb_wechat_notify_log
(
    id               bigint NOT NULL COMMENT 'id',
    out_trade_no     varchar(100) COMMENT '订单号',
    total            int COMMENT '订单总金额，单位为分',
    payer_total      int COMMENT '用户支付金额，单位为分',
    currency         varchar(50) COMMENT 'CNY：人民币，境内商户号仅支持人民币',
    payer_currency   varchar(50) COMMENT '用户支付币种',
    bank_type        varchar(50) COMMENT '银行类型',
    trade_state      varchar(50) COMMENT '交易状态',
    trade_state_desc varchar(500) COMMENT '交易状态描述',
    trade_type       varchar(50) COMMENT '交易类型',
    transaction_id   varchar(100) COMMENT '微信支付系统生成的订单号',
    success_time     varchar(100) COMMENT '支付完成时间',
    create_date      datetime COMMENT '创建时间',
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='微信回调日志';

-- 支付菜单
INSERT INTO sys_menu(id, pid, url, open_style, permissions, menu_type, icon, sort, del_flag, creator, create_date, updater, update_date) VALUES (1343074685589442561, 1343074487777677313, 'pay/order', 0, 'pay:order:all', 0, 'icon-unorderedlist', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, open_style, permissions, menu_type, icon, sort, del_flag, creator, create_date, updater, update_date) VALUES (1343074794440019970, 1343074487777677313, 'pay/alipaynotifylog', 0, 'pay:alipayNotifyLog:all', 0, 'icon-filedone', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, open_style, permissions, menu_type, icon, sort, del_flag, creator, create_date, updater, update_date) VALUES (1343074794440019971, 1343074487777677313, 'pay/wechatnotifylog', 0, 'pay:wechatNotifyLog:all', 0, 'icon-filedone', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());

-- 支付管理菜单
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074487777677313, 'name', 'Pay Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074487777677313, 'name', '支付管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074487777677313, 'name', '支付管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074685589442561, 'name', 'Order Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074685589442561, 'name', '订单管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074685589442561, 'name', '訂單管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019970, 'name', 'AliPay Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019970, 'name', '支付宝回调日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019970, 'name', '支付寶回調日誌', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019971, 'name', 'WeChat Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019971, 'name', '微信回调日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1343074794440019971, 'name', '微信回調日誌', 'zh-TW');


