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
  `status` TINYINT DEFAULT 0 COMMENT '状态：0待支付，1已支付，2已取消',
  `pay_time` DATETIME COMMENT '支付时间',
  `start_time` DATETIME COMMENT '租赁开始时间',
  `end_time` DATETIME COMMENT '租赁结束时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_order_no` (`order_no`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_land_id` (`land_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE IF NOT EXISTS `device` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `land_id` BIGINT NOT NULL COMMENT '土地ID',
  `name` VARCHAR(64) NOT NULL COMMENT '设备名称',
  `type` VARCHAR(32) NOT NULL COMMENT '设备类型：water浇水，fertilizer施肥，camera监控',
  `sn` VARCHAR(64) COMMENT '设备编号',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0离线，1在线',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_land_id` (`land_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';
