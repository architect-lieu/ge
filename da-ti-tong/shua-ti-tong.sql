-- ============================================
-- 答题通数据库建表脚本
-- 数据库名: shua-ti-tong
-- 生成时间: 2024
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `shua-ti-tong` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `shua-ti-tong`;

-- ============================================
-- 1. 管理员表
-- ============================================
DROP TABLE IF EXISTS `dtt_admin`;
CREATE TABLE `dtt_admin` (
  `admin_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `admin_name` VARCHAR(255) DEFAULT NULL COMMENT '管理员名称',
  `admin_password` VARCHAR(255) DEFAULT NULL COMMENT '管理员密码',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- ============================================
-- 2. 用户表
-- ============================================
DROP TABLE IF EXISTS `dtt_customer`;
CREATE TABLE `dtt_customer` (
  `user_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `nick_name` VARCHAR(255) DEFAULT NULL COMMENT '昵称',
  `head_picture` VARCHAR(500) DEFAULT NULL COMMENT '头像',
  `openid` VARCHAR(255) DEFAULT NULL COMMENT '微信openid',
  `union_id` VARCHAR(255) DEFAULT NULL COMMENT '微信union_id',
  `mobilephone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `session_key` VARCHAR(255) DEFAULT NULL COMMENT '微信session_key',
  `password` VARCHAR(255) DEFAULT NULL COMMENT '密码',
  `ms_code` INT(11) DEFAULT NULL COMMENT '短信验证码',
  `ms_code_time` DATETIME DEFAULT NULL COMMENT '短信验证码生成时间',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `vip_flag` INT(11) DEFAULT 0 COMMENT '会员标识',
  `fist_vip_pay_time` DATETIME DEFAULT NULL COMMENT '第一次开通vip时间',
  `last_vip_pay_time` DATETIME DEFAULT NULL COMMENT '最后一次续费时间',
  `vip_expiration_time` DATETIME DEFAULT NULL COMMENT 'vip过期时间',
  `integral` INT(11) DEFAULT 0 COMMENT '积分',
  `source` VARCHAR(255) DEFAULT NULL COMMENT '用户初次来源',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  KEY `idx_openid` (`openid`),
  KEY `idx_mobilephone` (`mobilephone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 3. 分类表
-- ============================================
DROP TABLE IF EXISTS `dtt_category`;
CREATE TABLE `dtt_category` (
  `category_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` VARCHAR(255) NOT NULL COMMENT '分类名称',
  `parent_id` BIGINT(20) DEFAULT NULL COMMENT '父节点ID',
  `parent_flag` TINYINT(1) DEFAULT 0 COMMENT '父节点标识',
  `level` INT(11) DEFAULT NULL COMMENT '几级分类',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`category_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- ============================================
-- 4. 科目表
-- ============================================
DROP TABLE IF EXISTS `dtt_subject`;
CREATE TABLE `dtt_subject` (
  `subject_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '科目ID',
  `subject_name` VARCHAR(255) NOT NULL COMMENT '科目名称',
  `category_id` BIGINT(20) NOT NULL COMMENT '分类ID',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`subject_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='科目表';

-- ============================================
-- 5. 章节表
-- ============================================
DROP TABLE IF EXISTS `dtt_chapter`;
CREATE TABLE `dtt_chapter` (
  `chapter_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '章节ID',
  `chapter_name` VARCHAR(255) NOT NULL COMMENT '章节名称',
  `category_id` BIGINT(20) NOT NULL COMMENT '所属分类',
  `subject_id` BIGINT(20) DEFAULT NULL COMMENT '科目ID',
  `true_question_chapter_flag` TINYINT(1) DEFAULT 0 COMMENT '是否是真题章节',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`chapter_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节表';

-- ============================================
-- 6. 试题集表
-- ============================================
DROP TABLE IF EXISTS `dtt_paper`;
CREATE TABLE `dtt_paper` (
  `paper_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '卷子ID',
  `paper_name` VARCHAR(255) NOT NULL COMMENT '卷子名',
  `chapter_id` BIGINT(20) NOT NULL COMMENT '所属章节ID',
  `question_num` BIGINT(20) DEFAULT 0 COMMENT '题目数量',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`paper_id`),
  KEY `idx_chapter_id` (`chapter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试题集表';

-- ============================================
-- 7. 试题集题目关联表
-- ============================================
DROP TABLE IF EXISTS `dtt_paper_question`;
CREATE TABLE `dtt_paper_question` (
  `paper_question_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `paper_id` BIGINT(20) NOT NULL COMMENT '试题集ID',
  `question_id` BIGINT(20) NOT NULL COMMENT '问题ID',
  PRIMARY KEY (`paper_question_id`),
  KEY `idx_paper_id` (`paper_id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试题集题目关联表';

-- ============================================
-- 8. 题目表
-- ============================================
DROP TABLE IF EXISTS `dtt_question`;
CREATE TABLE `dtt_question` (
  `question_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '问题ID',
  `category_id` BIGINT(20) NOT NULL COMMENT '分类ID',
  `category_pid` BIGINT(20) DEFAULT NULL COMMENT '科目ID',
  `question_title` TEXT NOT NULL COMMENT '问题',
  `question_type_code` VARCHAR(50) DEFAULT NULL COMMENT '问题类型编码',
  `question_type_name` VARCHAR(100) DEFAULT NULL COMMENT '问题类型名称',
  `options` JSON DEFAULT NULL COMMENT '选项(JSON格式)',
  `right_options` JSON DEFAULT NULL COMMENT '正确选项(JSON格式)',
  `difficulty` VARCHAR(20) DEFAULT NULL COMMENT '难度：简单 中等 困难',
  `right_answer` TEXT DEFAULT NULL COMMENT '正确回答[填空或者简答题]',
  `analysis` TEXT DEFAULT NULL COMMENT '问题解析',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`question_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_category_pid` (`category_pid`),
  KEY `idx_question_type_code` (`question_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目表';

-- ============================================
-- 9. 题型表
-- ============================================
DROP TABLE IF EXISTS `dtt_question_type`;
CREATE TABLE `dtt_question_type` (
  `question_type_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '题型ID',
  `question_type_name` VARCHAR(100) NOT NULL COMMENT '题型名称',
  `question_type_code` VARCHAR(50) NOT NULL COMMENT '题型编码',
  `question_num` BIGINT(20) DEFAULT 0 COMMENT '问题数量',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`question_type_id`),
  UNIQUE KEY `uk_question_type_code` (`question_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题型表';

-- ============================================
-- 10. 文档表
-- ============================================
DROP TABLE IF EXISTS `dtt_document`;
CREATE TABLE `dtt_document` (
  `doc_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '文档ID',
  `doc_name` VARCHAR(255) NOT NULL COMMENT '文档名称',
  `hot_flag` TINYINT(1) DEFAULT 0 COMMENT '热门标识',
  `free_flag` TINYINT(1) DEFAULT 0 COMMENT '免费标识',
  `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面',
  `doc_download_url` VARCHAR(500) NOT NULL COMMENT '下载路径',
  `category_id` BIGINT(20) NOT NULL COMMENT '类型ID',
  `category_pid` BIGINT(20) NOT NULL COMMENT '父分类ID',
  `type` VARCHAR(50) DEFAULT NULL COMMENT '文档类型',
  `download_flag` VARCHAR(50) DEFAULT NULL COMMENT '下载标识',
  `download_url_key` VARCHAR(500) NOT NULL COMMENT '用于获取临时下载地址的文件key',
  `tags` JSON DEFAULT NULL COMMENT '文档标签(JSON格式)',
  `preview_images` JSON DEFAULT NULL COMMENT '预览图片(JSON格式)',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`doc_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_category_pid` (`category_pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档表';

-- ============================================
-- 11. 考试配置表
-- ============================================
DROP TABLE IF EXISTS `dtt_exam_config`;
CREATE TABLE `dtt_exam_config` (
  `examination_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '考试ID',
  `category_id` BIGINT(20) NOT NULL COMMENT '类型ID',
  `question_type` VARCHAR(50) NOT NULL COMMENT '题型',
  `question_num` INT(11) NOT NULL COMMENT '题数',
  `score` INT(11) NOT NULL COMMENT '分数',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`examination_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试配置表';

-- ============================================
-- 12. 考试记录表
-- ============================================
DROP TABLE IF EXISTS `dtt_exam_record`;
CREATE TABLE `dtt_exam_record` (
  `exam_record_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '考试记录ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `score` INT(11) NOT NULL COMMENT '分数(总分)',
  `exam_data` JSON DEFAULT NULL COMMENT '考试数据(JSON格式)',
  `error_num` INT(11) NOT NULL COMMENT '错误数',
  `success_num` INT(11) NOT NULL COMMENT '成功数',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`exam_record_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试记录表';

-- ============================================
-- 13. 考试信息表
-- ============================================
DROP TABLE IF EXISTS `dtt_exam_info`;
CREATE TABLE `dtt_exam_info` (
  `exam_info_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '考试信息ID',
  `exam_info_title` VARCHAR(255) DEFAULT NULL COMMENT '考试信息标题',
  `exam_info_content` JSON DEFAULT NULL COMMENT '考试信息内容(JSON格式)',
  `category_id` BIGINT(20) NOT NULL COMMENT '分类ID',
  `subject_id` BIGINT(20) NOT NULL COMMENT '科目ID',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`exam_info_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试信息表';

-- ============================================
-- 14. 错题记录表
-- ============================================
DROP TABLE IF EXISTS `dtt_error_question_record`;
CREATE TABLE `dtt_error_question_record` (
  `error_record_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '错题记录ID',
  `question_id` BIGINT(20) NOT NULL COMMENT '问题ID',
  `category_id` BIGINT(20) NOT NULL COMMENT '问题的分类ID',
  `status` INT(11) NOT NULL COMMENT '答题状态：1对 2错',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `question_type_name` VARCHAR(100) DEFAULT NULL COMMENT '题型',
  `answer` JSON DEFAULT NULL COMMENT '答案(JSON格式)',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`error_record_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_question_id` (`question_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='错题记录表';

-- ============================================
-- 15. 用户收藏表
-- ============================================
DROP TABLE IF EXISTS `dtt_user_collect`;
CREATE TABLE `dtt_user_collect` (
  `collect_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `question_id` BIGINT(20) DEFAULT NULL COMMENT '问题ID',
  `category_id` BIGINT(20) DEFAULT NULL COMMENT '分类ID',
  `question_type_name` VARCHAR(100) DEFAULT NULL COMMENT '题型名称',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `collect_type` INT(11) NOT NULL COMMENT '收藏类型：1收藏分类 2收藏问题',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`collect_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_question_id` (`question_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_collect_type` (`collect_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

-- ============================================
-- 16. 笔记表
-- ============================================
DROP TABLE IF EXISTS `dtt_notes`;
CREATE TABLE `dtt_notes` (
  `notes_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '笔记ID',
  `notes_content` TEXT NOT NULL COMMENT '笔记内容',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `question_id` BIGINT(20) NOT NULL COMMENT '问题ID',
  `category_id` BIGINT(20) NOT NULL COMMENT '问题分类ID',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`notes_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_question_id` (`question_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='笔记表';

-- ============================================
-- 17. 支付订单表
-- ============================================
DROP TABLE IF EXISTS `dtt_pay_order`;
CREATE TABLE `dtt_pay_order` (
  `pay_order_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '支付订单ID',
  `out_trade_no` VARCHAR(100) DEFAULT NULL COMMENT '支付订单生成的序列号',
  `pay_status` TINYINT(1) DEFAULT 0 COMMENT '1支付 0未支付',
  `pay_order_type` VARCHAR(50) DEFAULT NULL COMMENT '会员类型',
  `price` DECIMAL(10,2) DEFAULT NULL COMMENT '价格',
  `vip_price_type` VARCHAR(50) DEFAULT NULL COMMENT '会员价格类型',
  `real_price` DECIMAL(10,2) DEFAULT NULL COMMENT '真实价格',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `openid` VARCHAR(255) DEFAULT NULL COMMENT '用户标识',
  `prepay_id` VARCHAR(255) DEFAULT NULL COMMENT '请求支付返回的，小程序根据这个去支付',
  `refund_flag` TINYINT(1) DEFAULT 0 COMMENT '退款标识',
  `result_body` TEXT DEFAULT NULL COMMENT '调用微信方返回的数据体',
  `vip_code` INT(11) DEFAULT NULL COMMENT 'VIP代码',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`pay_order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_out_trade_no` (`out_trade_no`),
  KEY `idx_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付订单表';

-- ============================================
-- 18. VIP配置表
-- ============================================
DROP TABLE IF EXISTS `dtt_vip_config`;
CREATE TABLE `dtt_vip_config` (
  `vip_config_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'VIP配置ID',
  `vip_type` VARCHAR(50) NOT NULL COMMENT 'vip类型：普通会员 会员 超级会员',
  `month_price` DECIMAL(10,2) NOT NULL COMMENT '价格',
  `quarter_price` DECIMAL(10,2) NOT NULL COMMENT '季度价格',
  `year_price` DECIMAL(10,2) NOT NULL COMMENT '年价格',
  `month_search_question_num` INT(11) DEFAULT NULL COMMENT '每月可以搜题次数',
  `month_download_doc_num` INT(11) DEFAULT NULL COMMENT '每月文档可以下载次数',
  `category_num` INT(11) DEFAULT NULL COMMENT '可刷题库数',
  `collect_category_num` INT(11) DEFAULT NULL COMMENT '可以收藏的题库数',
  `exercise_record_max_day` INT(11) DEFAULT NULL COMMENT '练习记录保存最大天数',
  `high_frequency_error` TINYINT(1) DEFAULT 0 COMMENT '高频错题权限 1有 0无',
  `mock_examination` TINYINT(1) DEFAULT 0 COMMENT '模拟考试权限',
  `mock_examination_record` TINYINT(1) DEFAULT 0 COMMENT '模拟考试权限记录',
  `option_out_of_order` TINYINT(1) DEFAULT 0 COMMENT '选项乱序',
  `error_priority` TINYINT(1) DEFAULT 0 COMMENT '错题优先',
  `undone_priority` TINYINT(1) DEFAULT 0 COMMENT '未做优先',
  `question_without_ads` TINYINT(1) DEFAULT 0 COMMENT '做题没广告',
  `search_without_ads` TINYINT(1) DEFAULT 0 COMMENT '搜题没广告',
  `data_statistic` TINYINT(1) DEFAULT 0 COMMENT '做题统计',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`vip_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VIP配置表';

-- ============================================
-- 19. 消息表
-- ============================================
DROP TABLE IF EXISTS `dtt_message`;
CREATE TABLE `dtt_message` (
  `message_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `message_content` TEXT NOT NULL COMMENT '消息内容',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ============================================
-- 20. 消息客户端关联表
-- ============================================
DROP TABLE IF EXISTS `dtt_message_client`;
CREATE TABLE `dtt_message_client` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `client_id` BIGINT(20) DEFAULT NULL COMMENT '客户ID',
  `message_id` BIGINT(20) DEFAULT NULL COMMENT '消息ID',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_client_id` (`client_id`),
  KEY `idx_message_id` (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息客户端关联表';

-- ============================================
-- 21. 反馈表
-- ============================================
DROP TABLE IF EXISTS `dtt_feedback`;
CREATE TABLE `dtt_feedback` (
  `feedback_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `feedback_content` TEXT NOT NULL COMMENT '反馈内容',
  `type` VARCHAR(50) NOT NULL COMMENT '反馈类型-前端选择',
  `use_flag` INT(11) DEFAULT NULL COMMENT '哪里的反馈-对应错题 小程序反馈',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `question_id` BIGINT(20) DEFAULT NULL COMMENT '问题ID-错题的反馈会有',
  `category_id` BIGINT(20) DEFAULT NULL COMMENT '分类ID-错题的反馈会有',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`feedback_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='反馈表';

-- ============================================
-- 22. 机构表
-- ============================================
DROP TABLE IF EXISTS `dtt_organization`;
CREATE TABLE `dtt_organization` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `org_name` VARCHAR(255) NOT NULL COMMENT '机构名称',
  `org_code` VARCHAR(100) NOT NULL COMMENT '机构编码',
  `active_code` VARCHAR(255) DEFAULT NULL COMMENT '机构的激活码',
  `max_active_times` INT(11) NOT NULL COMMENT '最大激活次数',
  `vip_expiration_time` DATETIME NOT NULL COMMENT 'vip过期时间',
  `vip_flag` INT(11) NOT NULL COMMENT 'vip标记 哪种类型的vip',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_code` (`org_code`),
  KEY `idx_active_code` (`active_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='机构表';

-- ============================================
-- 23. 机构用户关联表
-- ============================================
DROP TABLE IF EXISTS `dtt_org_user`;
CREATE TABLE `dtt_org_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '激活记录ID',
  `org_id` BIGINT(20) DEFAULT NULL COMMENT '机构ID',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_org_id` (`org_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='机构用户关联表';

-- ============================================
-- 24. 文档下载搜索记录表
-- ============================================
DROP TABLE IF EXISTS `dtt_doc_download_record`;
CREATE TABLE `dtt_doc_download_record` (
  `doc_download_search_record_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
  `doc_id` BIGINT(20) DEFAULT NULL COMMENT '文档ID',
  `type` VARCHAR(50) DEFAULT NULL COMMENT '类型',
  `keyword` JSON DEFAULT NULL COMMENT '关键词(JSON格式)',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`doc_download_search_record_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_doc_id` (`doc_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档下载搜索记录表';

-- ============================================
-- 初始化数据（可选）
-- ============================================

-- 插入默认管理员账号（密码需要根据实际情况加密）
INSERT INTO `dtt_admin` (`admin_name`, `admin_password`, `create_time`, `update_time`) 
VALUES ('admin', 'admin', NOW(), NOW());

-- 插入VIP配置数据
-- 普通会员
INSERT INTO `dtt_vip_config` (
    `vip_type`, `month_price`, `quarter_price`, `year_price`,
    `month_search_question_num`, `month_download_doc_num`, `category_num`, `collect_category_num`,
    `exercise_record_max_day`, `high_frequency_error`, `mock_examination`, `mock_examination_record`,
    `option_out_of_order`, `error_priority`, `undone_priority`, `question_without_ads`,
    `search_without_ads`, `data_statistic`, `create_time`, `update_time`
) VALUES (
    '普通会员', 0.00, 0.00, 0.00,
    10, 5, 3, 3,
    7, 0, 0, 0,
    0, 0, 0, 0,
    0, 0, NOW(), NOW()
);

-- 会员
INSERT INTO `dtt_vip_config` (
    `vip_type`, `month_price`, `quarter_price`, `year_price`,
    `month_search_question_num`, `month_download_doc_num`, `category_num`, `collect_category_num`,
    `exercise_record_max_day`, `high_frequency_error`, `mock_examination`, `mock_examination_record`,
    `option_out_of_order`, `error_priority`, `undone_priority`, `question_without_ads`,
    `search_without_ads`, `data_statistic`, `create_time`, `update_time`
) VALUES (
    '会员', 29.90, 79.90, 299.90,
    100, 50, 10, 10,
    30, 1, 1, 1,
    1, 1, 1, 1,
    1, 1, NOW(), NOW()
);

-- 超级会员
INSERT INTO `dtt_vip_config` (
    `vip_type`, `month_price`, `quarter_price`, `year_price`,
    `month_search_question_num`, `month_download_doc_num`, `category_num`, `collect_category_num`,
    `exercise_record_max_day`, `high_frequency_error`, `mock_examination`, `mock_examination_record`,
    `option_out_of_order`, `error_priority`, `undone_priority`, `question_without_ads`,
    `search_without_ads`, `data_statistic`, `create_time`, `update_time`
) VALUES (
    '超级会员', 49.90, 129.90, 499.90,
    -1, -1, -1, -1,
    365, 1, 1, 1,
    1, 1, 1, 1,
    1, 1, NOW(), NOW()
);

-- ============================================
-- 脚本执行完成
-- ============================================


