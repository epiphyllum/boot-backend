
CREATE TABLE mp_account (
                            id bigint NOT NULL COMMENT 'id',
                            name varchar(100) COMMENT '名称',
                            app_id varchar(100) COMMENT 'AppID',
                            app_secret varchar(100) COMMENT 'AppSecret',
                            token varchar(100) COMMENT 'Token',
                            aes_key varchar(100) COMMENT 'EncodingAESKey',
                            creator bigint COMMENT '创建者',
                            create_date datetime COMMENT '创建时间',
                            updater bigint COMMENT '更新者',
                            update_date datetime COMMENT '更新时间',
                            primary key (id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='公众号账号管理';

CREATE TABLE mp_menu (
                         id bigint NOT NULL COMMENT 'id',
                         menu varchar(2000) COMMENT '菜单json数据',
                         app_id varchar(100) COMMENT 'AppID',
                         creator bigint COMMENT '创建者',
                         create_date datetime COMMENT '创建时间',
                         updater bigint COMMENT '更新者',
                         update_date datetime COMMENT '更新时间',
                         primary key (id),
                         unique key uk_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='公众号自定义菜单';

-- 微信菜单
INSERT INTO sys_menu (id, pid, url, permissions, menu_type, open_style, icon, sort, del_flag, creator, create_date, updater, update_date) VALUES (1419553543706972161, 1419551957005963266, 'mp/account', 'mp:account:all', 0, 0, 'icon-user', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu (id, pid, url, permissions, menu_type, open_style, icon, sort, del_flag, creator, create_date, updater, update_date) VALUES (1419963799817691137, 1419551957005963266, 'mp/menu', 'mp:menu:all', 0, 0, 'icon-unorderedlist', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());

-- 微信公众号菜单
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419551957005963266, 'name', 'Wechat Management', 'en-US');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419551957005963266, 'name', '微信管理', 'zh-CN');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419551957005963266, 'name', '微信管理', 'zh-TW');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419553543706972161, 'name', 'Mp Management', 'en-US');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419553543706972161, 'name', '公众号管理', 'zh-CN');
INSERT INTO sys_language (table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1419553543706972161, 'name', '公眾號管理', 'zh-TW');

