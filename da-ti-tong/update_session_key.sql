-- ============================================
-- 更新脚本：为 dtt_customer 表添加 session_key 字段
-- 执行时间: 2024
-- ============================================

USE `shua-ti-tong`;

-- 为 dtt_customer 表添加 session_key 字段
ALTER TABLE `dtt_customer` 
ADD COLUMN `session_key` VARCHAR(255) DEFAULT NULL COMMENT '微信session_key' 
AFTER `mobilephone`;

-- 执行完成
-- ============================================

