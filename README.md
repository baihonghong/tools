# Tools

开发工具类集合 - ID生成Web服务

## 功能列表

### 36位ID生成器

基于时间戳生成36位唯一ID的Web服务，通过浏览器即可访问使用。

#### 特性

- ✅ 基于时间戳，有序递增
- ✅ 36位字符串长度
- ✅ 全局唯一，无冲突
- ✅ 支持分布式环境（机器ID）
- ✅ 高性能：单机每秒400万+
- ✅ 内置Tomcat，无需外部容器
- ✅ 提供Web界面和API接口

#### 快速开始

**方式一：Maven运行**
```bash
# 编译并运行
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/tools-1.0.0.jar
```

**方式二：直接运行JAR**
```bash
# 如果已有打包好的JAR文件
java -jar tools-1.0.0.jar
```

启动后访问：
- **Web界面**：http://localhost:8080
- **API接口**：http://localhost:8080/generate

#### 使用方法

**Web界面**
- 访问 http://localhost:8080
- 点击按钮即可生成ID
- 支持批量生成（1-1000个）

**API接口**

1. **单个ID**
```bash
GET http://localhost:8080/generate

# 返回示例
{
  "id": "1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p7q8r",
  "length": 36,
  "timestamp": 1699999999999,
  "success": true
}
```

2. **批量ID**
```bash
GET http://localhost:8080/generateBatch?count=10

# 返回示例
{
  "ids": ["id1", "id2", ...],
  "count": 10,
  "success": true
}
```

#### 技术栈

- Java 11
- Spring Boot 2.7.18
- Maven

#### 项目结构

```
tools/
├── src/main/java/com/baihonghong/tools/
│   ├── ToolsApplication.java          # 启动类
│   ├── TimestampIdGenerator.java      # ID生成核心类
│   ├── IdGeneratorController.java     # API控制器
│   └── IndexController.java           # 首页控制器
├── src/main/resources/
│   ├── application.properties         # 配置文件
│   └── templates/index.html           # Web界面
├── pom.xml                            # Maven配置
└── README.md
```

## 作者

baihonghong

## 许可证

MIT License
