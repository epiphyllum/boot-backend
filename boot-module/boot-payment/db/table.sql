CREATE TABLE tb_order
(
    id           bigint         NOT NULL COMMENT 'id',
    order_id     bigint COMMENT '订单ID',
    product_id   bigint         NOT NULL COMMENT '产品ID',
    product_name varchar(100) COMMENT '产品名称',
    pay_amount   decimal(10, 2) NOT NULL COMMENT '支付金额',
    status       tinyint COMMENT '订单状态  -1：已取消   0：等待付款   1：已完成',
    user_id      bigint COMMENT '购买用户ID',
    pay_at       datetime COMMENT '支付时间',
    create_date  datetime COMMENT '下单时间',
    primary key (id),
    unique key uk_order_id (order_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单';

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



