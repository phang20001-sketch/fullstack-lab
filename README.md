# fullstack-lab

React + Spring Boot 前后端分离学习项目。

## 技术栈

- 前端：React 19、TypeScript、Vite 8、pnpm
- 后端：Java 21、Spring Boot 4.1.1、MyBatis-Plus 3.5.17、Maven
- 中间件：PostgreSQL 15、Redis 3.2.12、RabbitMQ 4.3.5
- Redis 客户端：Redisson 4.6.1
- 数据库连接池：Druid 1.2.28
- 数据库迁移：Flyway

## 目录结构

```text
fullstack-lab/
├─ frontend/                       React 前端
├─ backend/                        Spring Boot 后端
│  └─ src/main/resources/
│     ├─ application.yml           公共配置
│     ├─ application-dev.yml       开发环境配置
│     ├─ application-prod.yml      生产环境配置
│     ├─ mapper/                    MyBatis XML SQL
│     └─ db/migration/              Flyway SQL
└─ README.md
```

`dev` 是默认环境。`application-prod.yml` 包含正式环境默认连接信息，已由根目录 `.gitignore` 排除；部署时仍建议使用环境变量覆盖密码。

## 本机 Java 和 Maven

已经在 `D:\develop` 中独立安装以下工具：

- Eclipse Temurin JDK 21：`D:\develop\jdk-21`
- Apache Maven 3.9.16：`D:\develop\apache-maven-3.9.16`

安装程序已经写入当前用户的 `JAVA_HOME`、`MAVEN_HOME` 和 `Path`。重新打开 PowerShell 后可以直接执行：

```powershell
java -version
mvn -version
```

如果只想在当前 PowerShell 窗口临时设置，可以执行：

```powershell
$env:JAVA_HOME = 'D:\develop\jdk-21'
$env:MAVEN_HOME = 'D:\develop\apache-maven-3.9.16'
$env:Path = "$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:Path"
```

Maven 全局配置位于 `D:\develop\apache-maven-3.9.16\conf\settings.xml`，已配置阿里云公共仓库 `https://maven.aliyun.com/repository/public` 作为所有远程仓库的镜像。本地依赖仓库位于 `D:\develop\.m2\repository`；原始配置备份为同目录下的 `settings.xml.bak`。

## 开发环境

开发环境默认连接：

| 服务 | 默认地址 | 默认账号 |
|---|---|---|
| PostgreSQL 15 | `127.0.0.1:5432/fullstack_lab` | `postgres / postgres` |
| Redis 3.2.12 | `127.0.0.1:6379`，数据库 `0` | 默认无密码 |
| RabbitMQ 4.3.5 | `127.0.0.1:5672`，虚拟主机 `/` | `guest / guest` |

以上开发环境连接值已经直接写在 `backend/src/main/resources/application-dev.yml` 中，无需在 PowerShell 中逐条设置环境变量。数据库连接由 Druid 管理，初始连接数为 2，最小空闲连接数为 2，最大活动连接数为 10。

启动后端：

```powershell
cd D:\test\fullstack-lab\backend
.\mvnw.cmd spring-boot:run
```

启动前端：

```powershell
cd D:\test\fullstack-lab\frontend
pnpm dev
```

访问 `http://localhost:5173`。Vite 会把 `/api` 和 `/actuator` 请求代理到 `http://localhost:8080`。

## 生产环境

生产环境连接信息保存在不会提交的 `application-prod.yml` 中，也可以通过以下环境变量显式设置：

```powershell
$env:SPRING_PROFILES_ACTIVE = 'prod'

$env:DB_URL = 'jdbc:postgresql://<服务器地址>:5432/fullstack_lab'
$env:DB_USERNAME = 'postgres'
$env:DB_PASSWORD = '<服务器密码>'
$env:DB_POOL_INITIAL_SIZE = '5'
$env:DB_POOL_MIN_IDLE = '5'
$env:DB_POOL_MAX_ACTIVE = '20'

$env:REDIS_HOST = '<服务器地址>'
$env:REDIS_PORT = '6379'
$env:REDIS_DATABASE = '0'
$env:REDIS_PASSWORD = '<服务器密码>'
$env:REDIS_SSL_ENABLED = 'false'

$env:RABBITMQ_HOST = '<服务器地址>'
$env:RABBITMQ_PORT = '5672'
$env:RABBITMQ_USERNAME = 'guest'
$env:RABBITMQ_PASSWORD = '<服务器密码>'
$env:RABBITMQ_VIRTUAL_HOST = '/'
$env:RABBITMQ_SSL_ENABLED = 'false'
```

构建并启动：

```powershell
cd D:\test\fullstack-lab\backend
.\mvnw.cmd clean package
java -jar .\target\backend-0.0.1-SNAPSHOT.jar
```

生产部署时应由 Nginx 或网关将 `/api` 转发到后端，不要使用 Vite 开发代理。

## 版本注意事项

- Redis 由 Redisson 管理。Redis 3.2.12 没有 ACL 用户名功能，项目只配置密码；避免使用 Redis Streams、ACL 等较新版本功能。
- RabbitMQ 的 `guest` 用户通常只允许本机连接。远程开发或生产环境请创建独立用户和虚拟主机。
- RabbitMQ 4.3.5 支持 Erlang 27.x，指定的 Erlang 27.3.4.11 在此范围内。
- PostgreSQL JDBC URL 建议在生产环境启用合适的 `sslmode`。

## MyBatis-Plus 与 XML SQL

Mapper 接口继承 `BaseMapper` 后可以使用 MyBatis-Plus 的通用增删改查，同时可以声明自定义方法并在 XML 中编写 SQL：

```text
backend/src/main/java/com/example/fullstacklab/mapper/LearningNoteMapper.java
backend/src/main/resources/mapper/LearningNoteMapper.xml
```

示例接口：

- `POST /api/notes`：使用 MyBatis-Plus `BaseMapper.insert`
- `GET /api/notes?limit=20`：执行 `LearningNoteMapper.xml` 中的 `selectRecent` SQL

Redisson 可以在 Spring Bean 中直接注入：

```java
private final RedissonClient redissonClient;
```

## 检查地址

- 项目连通性：`GET http://localhost:8080/api/health`
- Spring Boot 健康检查：`GET http://localhost:8080/actuator/health`
