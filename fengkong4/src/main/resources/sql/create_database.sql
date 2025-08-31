-- 创建MySQL数据库和表结构脚本
-- 数据库：fengkong4_mysql
-- 用于征信报文数据的MySQL存储

-- 创建数据库
CREATE DATABASE IF NOT EXISTS fengkong4_mysql DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fengkong4_mysql;

-- 1. 征信报文主记录表
CREATE TABLE IF NOT EXISTS credit_reports (
    id VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '主键ID',
    credit_report_id VARCHAR(64) COMMENT '征信报文ID',
    id_number VARCHAR(32) COMMENT '身份证号码',
    name VARCHAR(64) COMMENT '姓名',
    report_number VARCHAR(64) COMMENT '报文编号',
    generate_time VARCHAR(32) COMMENT '生成时间',
    institution_code VARCHAR(32) COMMENT '机构代码',
    query_reason VARCHAR(128) COMMENT '查询原因',
    report_type VARCHAR(32) COMMENT '报文类型',
    report_query_time VARCHAR(32) COMMENT '报告查询时间',
    has_valid_credit_info TINYINT(1) DEFAULT 0 COMMENT '是否查询到有效征信记录',
    first_credit_month_diff INT COMMENT '首次贷款/贷记卡距今月份数',
    credit_score INT COMMENT '征信评分',
    score_time DATETIME COMMENT '评分时间',
    score_status VARCHAR(32) COMMENT '评分状态',
    ai_response_time BIGINT COMMENT '大模型响应时间（毫秒）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    INDEX idx_id_number (id_number),
    INDEX idx_name (name),
    INDEX idx_credit_report_id (credit_report_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='征信报文主记录表';

-- 2. 个人基本信息表
CREATE TABLE IF NOT EXISTS personal_infos (
    id VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '主键ID',
    credit_report_id VARCHAR(64) COMMENT '征信报文ID',
    name VARCHAR(64) COMMENT '姓名',
    id_type VARCHAR(32) COMMENT '证件类型',
    id_number VARCHAR(32) COMMENT '证件号码',
    gender VARCHAR(16) COMMENT '性别',
    birth_date VARCHAR(32) COMMENT '出生日期',
    marital_status VARCHAR(32) COMMENT '婚姻状况',
    education VARCHAR(32) COMMENT '学历',
    degree VARCHAR(32) COMMENT '学位',
    employment_status VARCHAR(32) COMMENT '就业状况',
    nationality VARCHAR(32) COMMENT '国籍',
    email VARCHAR(128) COMMENT '电子邮箱',
    address VARCHAR(256) COMMENT '通讯地址',
    mobile VARCHAR(32) COMMENT '手机号码',
    company_name VARCHAR(128) COMMENT '单位名称',
    company_address VARCHAR(256) COMMENT '单位地址',
    company_phone VARCHAR(32) COMMENT '单位电话',
    occupation VARCHAR(64) COMMENT '职业',
    position VARCHAR(64) COMMENT '职务',
    annual_income VARCHAR(32) COMMENT '年收入',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    INDEX idx_credit_report_id (credit_report_id),
    INDEX idx_id_number (id_number),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='个人基本信息表';

-- 3. 信贷交易信息明细表
CREATE TABLE IF NOT EXISTS credit_transactions (
    id VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '主键ID',
    credit_report_id VARCHAR(64) COMMENT '征信报文ID',
    management_org VARCHAR(128) COMMENT '管理机构',
    account_id VARCHAR(64) COMMENT '账户标识',
    account_type VARCHAR(32) COMMENT '账户类型',
    currency VARCHAR(16) COMMENT '币种',
    open_date VARCHAR(32) COMMENT '开立日期',
    expire_date VARCHAR(32) COMMENT '到期日期',
    credit_limit BIGINT COMMENT '信用额度/贷款金额',
    shared_credit_limit BIGINT COMMENT '共享授信额度',
    account_status VARCHAR(32) COMMENT '账户状态',
    repayment_frequency VARCHAR(32) COMMENT '还款频率',
    repayment_periods INT COMMENT '还款期数',
    current_repayment BIGINT COMMENT '本月应还款',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    INDEX idx_credit_report_id (credit_report_id),
    INDEX idx_account_id (account_id),
    INDEX idx_account_type (account_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='信贷交易信息明细表';

-- 4. 信息概要表
CREATE TABLE IF NOT EXISTS info_summaries (
    id VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '主键ID',
    credit_report_id VARCHAR(64) COMMENT '征信报文ID',
    credit_account_count INT DEFAULT 0 COMMENT '信贷交易账户数',
    credit_card_count INT DEFAULT 0 COMMENT '贷记卡账户数',
    quasi_credit_card_count INT DEFAULT 0 COMMENT '准贷记卡账户数',
    loan_account_count INT DEFAULT 0 COMMENT '贷款账户数',
    total_credit_limit BIGINT DEFAULT 0 COMMENT '信用额度总额',
    used_credit_limit BIGINT DEFAULT 0 COMMENT '已使用信用额度',
    avg_repayment_last_6_months BIGINT DEFAULT 0 COMMENT '最近6个月平均应还款',
    overdue_account_count INT DEFAULT 0 COMMENT '逾期账户数',
    overdue_amount BIGINT DEFAULT 0 COMMENT '逾期金额',
    bad_debt_account_count INT DEFAULT 0 COMMENT '呆账账户数',
    bad_debt_amount BIGINT DEFAULT 0 COMMENT '呆账金额',
    loan_overdue_count_last_5_years INT DEFAULT 0 COMMENT '最近5年内贷款逾期次数',
    credit_card_overdue_count_last_2_years INT DEFAULT 0 COMMENT '最近2年内贷记卡逾期次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    INDEX idx_credit_report_id (credit_report_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='信息概要表';

-- 5. 查询记录表
CREATE TABLE IF NOT EXISTS query_records (
    id VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '主键ID',
    credit_report_id VARCHAR(64) COMMENT '征信报文ID',
    query_date VARCHAR(32) COMMENT '查询日期',
    query_org VARCHAR(128) COMMENT '查询机构',
    query_reason VARCHAR(128) COMMENT '查询原因',
    query_type VARCHAR(32) COMMENT '查询类型',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    INDEX idx_credit_report_id (credit_report_id),
    INDEX idx_query_date (query_date),
    INDEX idx_query_org (query_org),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='查询记录表';

-- 6. 公共信息明细表
CREATE TABLE IF NOT EXISTS public_infos (
    id VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '主键ID',
    credit_report_id VARCHAR(64) COMMENT '征信报文ID',
    info_type VARCHAR(32) COMMENT '信息类型',
    record_org VARCHAR(128) COMMENT '记录机关',
    record_date VARCHAR(32) COMMENT '记录日期',
    publish_date VARCHAR(32) COMMENT '公布日期',
    closure_type VARCHAR(32) COMMENT '结案方式',
    closure_date VARCHAR(32) COMMENT '结案日期',
    amount BIGINT COMMENT '涉及金额',
    description TEXT COMMENT '内容描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    INDEX idx_credit_report_id (credit_report_id),
    INDEX idx_info_type (info_type),
    INDEX idx_record_date (record_date),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公共信息明细表';

-- 7. 非信贷交易信息明细表
CREATE TABLE IF NOT EXISTS non_credit_transactions (
    id VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '主键ID',
    credit_report_id VARCHAR(64) COMMENT '征信报文ID',
    business_type VARCHAR(32) COMMENT '业务类型',
    business_org VARCHAR(128) COMMENT '业务机构',
    open_date VARCHAR(32) COMMENT '开户日期',
    account_status VARCHAR(32) COMMENT '账户状态',
    payment_history_24_months VARCHAR(256) COMMENT '最近24个月缴费状态',
    overdue_amount BIGINT DEFAULT 0 COMMENT '欠费金额',
    overdue_months INT DEFAULT 0 COMMENT '欠费月数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',
    INDEX idx_credit_report_id (credit_report_id),
    INDEX idx_business_type (business_type),
    INDEX idx_business_org (business_org),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='非信贷交易信息明细表';
