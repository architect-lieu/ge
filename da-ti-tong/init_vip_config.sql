-- ============================================
-- VIP配置初始化脚本
-- 执行时间: 2024
-- ============================================

USE `shua-ti-tong`;

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

-- 执行完成
-- ============================================
-- 说明：
-- -1 表示无限制
-- 0 表示无权限
-- 1 表示有权限
-- ============================================

