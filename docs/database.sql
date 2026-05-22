-- 智慧农场数据库初始化脚本

CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `openid` VARCHAR(64) NOT NULL COMMENT '微信openid',
  `nickname` VARCHAR(64) COMMENT '昵称',
  `avatar` VARCHAR(256) COMMENT '头像',
  `phone` VARCHAR(16) COMMENT '手机号',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0禁用，1正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `land` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(64) NOT NULL COMMENT '土地名称',
  `location` VARCHAR(256) COMMENT '位置',
  `area` DECIMAL(10,2) COMMENT '面积（平方米）',
  `price` DECIMAL(10,2) COMMENT '价格（元/年）',
  `description` TEXT COMMENT '描述',
  `images` TEXT COMMENT '图片，多个用逗号分隔',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0不可用，1可认领，2已认领',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='土地表';

CREATE TABLE IF NOT EXISTS `order` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `land_id` BIGINT NOT NULL COMMENT '土地ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
  `status` TINYINT DEFAULT 0 COMMENT '兼容旧状态：0待支付，1已支付/处理中/已完成，2已取消',
  `status_text` VARCHAR(32) DEFAULT 'PENDING_PAY' COMMENT 'OpenAPI订单状态：PENDING_PAY/PAID/PROCESSING/COMPLETED/CANCELLED',
  `service_type` VARCHAR(32) COMMENT '服务类型：WATER/FERTILIZE',
  `quantity` INT DEFAULT 1 COMMENT '服务数量',
  `remark` VARCHAR(512) COMMENT '下单备注',
  `pay_time` DATETIME COMMENT '支付时间',
  `start_time` DATETIME COMMENT '租赁开始时间',
  `end_time` DATETIME COMMENT '租赁结束时间',
  `accept_time` DATETIME COMMENT '接单时间',
  `complete_time` DATETIME COMMENT '完工时间',
  `operator_id` BIGINT COMMENT '操作管理员ID',
  `operator_name` VARCHAR(64) COMMENT '操作管理员名称',
  `complete_images` TEXT COMMENT '完工图片，多个用逗号分隔',
  `complete_remark` VARCHAR(512) COMMENT '完工备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_order_no` (`order_no`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_land_id` (`land_id`),
  INDEX `idx_status_text` (`status_text`),
  INDEX `idx_service_type` (`service_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE IF NOT EXISTS `device` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `land_id` BIGINT NOT NULL COMMENT '土地ID',
  `name` VARCHAR(64) NOT NULL COMMENT '设备名称',
  `type` VARCHAR(32) NOT NULL COMMENT '设备类型：water浇水，fertilizer施肥，camera监控',
  `sn` VARCHAR(64) COMMENT '设备编号',
  `secret` VARCHAR(128) COMMENT '设备上报密钥',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0离线，1在线',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_land_id` (`land_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';

CREATE TABLE IF NOT EXISTS `alert_rule` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `land_id` BIGINT COMMENT '土地ID，空表示全局规则',
  `metric` VARCHAR(32) NOT NULL COMMENT '指标：temperature/humidity/soilMoisture/lightIntensity',
  `min_value` DECIMAL(10,2) COMMENT '下限',
  `max_value` DECIMAL(10,2) COMMENT '上限',
  `enabled` TINYINT DEFAULT 1 COMMENT '是否启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_alert_rule_land` (`land_id`),
  INDEX `idx_alert_rule_metric` (`metric`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警规则表';

CREATE TABLE IF NOT EXISTS `alert_event` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `land_id` BIGINT NOT NULL COMMENT '土地ID',
  `metric` VARCHAR(32) NOT NULL COMMENT '指标',
  `value` DECIMAL(10,2) NOT NULL COMMENT '触发值',
  `message` VARCHAR(255) NOT NULL COMMENT '告警消息',
  `handled` TINYINT DEFAULT 0 COMMENT '是否处理',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `idx_alert_event_land` (`land_id`),
  INDEX `idx_alert_event_metric` (`metric`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警事件表';

CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(64) NOT NULL COMMENT '管理员账号',
  `password` VARCHAR(128) NOT NULL COMMENT '管理员密码',
  `password_hash` VARCHAR(255) COMMENT '管理员密码哈希',
  `role` VARCHAR(32) NOT NULL DEFAULT 'ADMIN' COMMENT '角色：SUPER_ADMIN/ADMIN',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0禁用，1正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

INSERT IGNORE INTO `admin_user` (`id`, `username`, `password`, `role`, `status`)
VALUES (1, 'admin', 'admin', 'SUPER_ADMIN', 1);

CREATE TABLE IF NOT EXISTS `payment` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `payment_no` VARCHAR(64) NOT NULL COMMENT '支付单号',
  `order_id` BIGINT COMMENT '订单ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
  `pay_type` VARCHAR(32) NOT NULL COMMENT '支付类型：CLAIM/SERVICE',
  `status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING/SUCCESS/FAILED',
  `pay_method` VARCHAR(32) NOT NULL DEFAULT 'WECHAT' COMMENT '支付方式',
  `pay_time` DATETIME COMMENT '支付时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY `uk_payment_no` (`payment_no`),
  INDEX `idx_payment_order` (`order_id`),
  INDEX `idx_payment_user` (`user_id`),
  INDEX `idx_payment_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付表';

CREATE TABLE IF NOT EXISTS `monitor_data` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `land_id` BIGINT NOT NULL COMMENT '土地ID',
  `temperature` DECIMAL(8,2) COMMENT '温度',
  `humidity` DECIMAL(8,2) COMMENT '空气湿度',
  `soil_moisture` DECIMAL(8,2) COMMENT '土壤湿度',
  `light_intensity` DECIMAL(10,2) COMMENT '光照强度',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `idx_monitor_land_time` (`land_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监测数据表';

CREATE TABLE IF NOT EXISTS `notification` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(128) NOT NULL COMMENT '标题',
  `content` VARCHAR(512) NOT NULL COMMENT '内容',
  `type` VARCHAR(32) NOT NULL COMMENT '类型：SYSTEM/SERVICE/PAY',
  `is_read` TINYINT(1) DEFAULT 0 COMMENT '是否已读',
  `extra` TEXT COMMENT '扩展JSON',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `idx_notification_user` (`user_id`),
  INDEX `idx_notification_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';
