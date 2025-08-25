# Fengkong4 - 二代征信报文生成系统

## 项目简介

Fengkong4是一个用于生成二代征信报文的Spring Boot应用程序。该系统能够随机生成符合二代征信标准的报文数据，支持JSON和XML格式输出，提供完整的REST API接口。

## 功能特性

- 🎲 **随机数据生成**：基于Java Faker生成逼真的个人信息和征信数据
- 📊 **完整报文结构**：包含个人基本信息、信贷交易、查询记录等完整征信报文结构
- 🔄 **多格式输出**：支持JSON和XML格式输出
- 🎯 **自定义生成**：支持自定义信贷记录数量和查询记录数量
- ✅ **数据验证**：内置报文数据完整性验证功能
- 🌐 **REST API**：提供完整的RESTful API接口
- 📝 **详细日志**：完整的操作日志记录

## 技术栈

- **Java 8**
- **Spring Boot 2.3.6**
- **Jackson XML** - XML序列化
- **Java Faker** - 随机数据生成
- **Lombok** - 简化代码
- **JUnit 5** - 单元测试

## 项目结构

```
fengkong4/
├── src/main/java/com/JinRui/fengkong4/
│   ├── config/           # 配置类
│   ├── controller/       # REST控制器
│   ├── entity/           # 实体类
│   ├── generator/        # 数据生成器
│   ├── service/          # 服务层
│   └── Fengkong4Application.java  # 主启动类
├── src/main/resources/
│   └── application.yaml  # 配置文件
└── src/test/java/        # 测试代码
```

## 快速开始

### 1. 启动应用

```bash
cd fengkong4
mvn spring-boot:run
```

应用将在端口8304启动。

### 2. API接口

#### 生成随机征信报文
```http
GET http://localhost:8304/api/credit-report/generate/random
```

#### 生成自定义征信报文
```http
GET http://localhost:8304/api/credit-report/generate/custom?creditRecords=3&queryRecords=5
```

#### 生成XML格式报文
```http
GET http://localhost:8304/api/credit-report/generate/xml?creditRecords=5&queryRecords=8
```

#### 验证征信报文
```http
POST http://localhost:8304/api/credit-report/validate
Content-Type: application/json

{报文JSON数据}
```

#### 获取报文模板
```http
GET http://localhost:8304/api/credit-report/template
```

#### 健康检查
```http
GET http://localhost:8304/api/credit-report/health
```

## 配置说明

### application.yaml配置

```yaml
server:
  port: 8304

credit-report:
  version: "2.0"
  institution-code: "FENGKONG001"
  data-generation:
    enable-random: true
    default-credit-records: 5
    default-query-records: 3
```

## 征信报文结构

### 主要组成部分

1. **报文头（Header）**
   - 报文版本、编号、生成时间
   - 机构代码、查询原因等

2. **个人基本信息（PersonalInfo）**
   - 姓名、身份证号、性别、出生日期
   - 学历、职业、收入等详细信息

3. **信息概要（InfoSummary）**
   - 账户数量统计
   - 信用额度、逾期情况汇总

4. **信贷交易信息（CreditTransactions）**
   - 贷款、信用卡等详细记录
   - 还款历史、账户状态等

5. **非信贷交易信息（NonCreditTransactions）**
   - 水电煤缴费记录

6. **公共信息（PublicInfos）**
   - 法院判决、行政处罚等记录

7. **查询记录（QueryRecords）**
   - 征信查询历史记录

8. **标注声明（StatementInfo）**
   - 异议标注、个人声明等

## 示例响应

### JSON格式响应
```json
{
  "success": true,
  "message": "征信报文生成成功",
  "reportNumber": "CR1640995200123",
  "generateTime": "2025-01-14 15:30:00",
  "data": {
    "header": {
      "version": "2.0",
      "reportNumber": "CR1640995200123",
      "generateTime": "2025-01-14 15:30:00",
      "institutionCode": "FENGKONG001",
      "queryReason": "贷款审批",
      "reportType": "1"
    },
    "personalInfo": {
      "name": "张三",
      "idNumber": "110101199001011234",
      "gender": "1",
      // ... 更多字段
    }
    // ... 更多数据
  }
}
```

## 开发说明

### 添加新的数据类型

1. 在`entity`包中创建新的实体类
2. 在`CreditDataGenerator`中添加对应的生成方法
3. 更新`CreditReport`主实体类
4. 在`CreditReportService`中添加相应的处理逻辑

### 自定义数据生成规则

修改`CreditDataGenerator`类中的生成方法，调整数据生成规则和概率分布。

## 注意事项

⚠️ **重要提醒**：
- 本系统生成的是模拟数据，仅用于测试和开发目的
- 请勿将生成的数据用于任何正式的征信业务
- 使用时需遵守相关法律法规和数据保护规定
- 生成的身份证号码等敏感信息均为随机生成，不对应真实人员

## 版本历史

- **v1.0.0** - 初始版本
  - 基础征信报文生成功能
  - JSON/XML格式输出
  - REST API接口
  - 数据验证功能

## 联系方式

如有问题或建议，请联系开发团队。
