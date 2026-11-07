# Tools

开发工具类集合

## 功能列表

### 1. TimestampIdGenerator - 时间戳ID生成器

基于时间戳生成36位唯一ID的工具类。

#### 特性

- ✅ 基于时间戳，有序递增
- ✅ 36位字符串长度
- ✅ 全局唯一
- ✅ 高性能（单机每秒可生成400万+ID）
- ✅ 支持分布式（通过机器ID区分）

#### 使用方法

```java
// 创建生成器（默认机器ID为1）
TimestampIdGenerator generator = new TimestampIdGenerator();

// 或者指定机器ID（范围：0-1023）
TimestampIdGenerator generator = new TimestampIdGenerator(1L);

// 生成ID
String id = generator.generateId();
// 输出示例：1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p7q8r
```

#### 设计原理

采用 Snowflake 算法变种：

1. **时间戳部分**：41位，精确到毫秒
2. **机器ID部分**：10位，支持1024个节点
3. **序列号部分**：12位，每毫秒可生成4096个ID
4. **Base36编码**：将数字转换为36位字符串

#### 编译运行

```bash
# 编译
mvn clean compile

# 运行测试
mvn exec:java -Dexec.mainClass="com.baihonghong.tools.TimestampIdGenerator"
```

## 技术栈

- Java 8+
- Maven

## 作者

baihonghong

## 许可证

MIT License
