# Fengkong4 部署指南

## 快速启动

### 方式一：使用Maven启动
```bash
cd fengkong4
mvn spring-boot:run
```

### 方式二：使用JAR包启动
```bash
cd fengkong4
java -jar target/fengkong4-1.0-SNAPSHOT.jar
```

### 方式三：使用启动脚本（Windows）
```bash
cd fengkong4
start.bat
```

## 服务验证

应用启动后，访问以下地址验证服务状态：

- **健康检查**: http://localhost:8304/api/credit-report/health
- **生成随机报文**: http://localhost:8304/api/credit-report/generate/random
- **生成自定义报文**: http://localhost:8304/api/credit-report/generate/custom?creditRecords=3&queryRecords=5

## API接口文档

### 1. 健康检查
- **URL**: `/api/credit-report/health`
- **方法**: GET
- **描述**: 检查服务运行状态

### 2. 生成随机征信报文
- **URL**: `/api/credit-report/generate/random`
- **方法**: GET
- **描述**: 生成包含随机数据的完整征信报文

### 3. 生成自定义征信报文
- **URL**: `/api/credit-report/generate/custom`
- **方法**: GET
- **参数**: 
  - `creditRecords`: 信贷记录数量（可选，默认5）
  - `queryRecords`: 查询记录数量（可选，默认8）
- **描述**: 生成自定义数量记录的征信报文

### 4. 生成XML格式报文
- **URL**: `/api/credit-report/generate/xml`
- **方法**: GET
- **参数**: 
  - `creditRecords`: 信贷记录数量（可选，默认5）
  - `queryRecords`: 查询记录数量（可选，默认8）
- **描述**: 生成XML格式的征信报文

### 5. 验证征信报文
- **URL**: `/api/credit-report/validate`
- **方法**: POST
- **Content-Type**: application/json
- **描述**: 验证征信报文数据完整性

### 6. 获取报文模板
- **URL**: `/api/credit-report/template`
- **方法**: GET
- **描述**: 获取征信报文模板示例

## 配置说明

### 端口配置
默认端口：8304
修改方式：在 `application.yaml` 中修改 `server.port` 配置

### 征信报文配置
```yaml
credit-report:
  version: "2.0"                    # 报文版本
  institution-code: "FENGKONG001"   # 机构代码
  data-generation:
    enable-random: true             # 启用随机数据生成
    default-credit-records: 5       # 默认信贷记录数量
    default-query-records: 3        # 默认查询记录数量
```

## 测试脚本

使用 `test-api.bat` 脚本快速测试所有API接口：
```bash
test-api.bat
```

## 注意事项

1. **数据安全**: 生成的数据仅用于测试，请勿用于生产环境
2. **合规性**: 使用时需遵守相关法律法规
3. **性能**: 大量数据生成可能需要较长时间
4. **内存**: 生成大量记录时注意JVM内存配置

## 常见问题

### Q: 应用启动失败
A: 检查端口8304是否被占用，或修改配置文件中的端口

### Q: API返回500错误
A: 查看应用日志，通常是数据生成过程中的异常

### Q: XML格式输出异常
A: 检查Jackson XML依赖是否正确加载

## 日志配置

应用日志级别配置：
```yaml
logging:
  level:
    com.JinRui.fengkong4: DEBUG
    org.springframework.web: INFO
```

日志文件位置：控制台输出，可根据需要配置文件输出。
