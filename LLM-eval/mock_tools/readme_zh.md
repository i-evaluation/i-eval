# MockTool - 模拟I-Eval 测试工具集模拟服务

## 1. 项目简介

MockTool 是一个用于模拟I-Eval 测试工具集的 HTTP 服务的 Spring Boot 应用，提供各种设备的模拟接口，支持设备状态管理、数据处理和业务流程模拟。

## 2. HTTP 服务接口

### 2.1 空调设备接口

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/api/air-conditioner/update` | POST | 更新空调设备状态 | 更新空调的各种参数（温度、模式、风速等） |
| 2 | `/api/air-conditioner/status` | POST | 查询空调设备状态 | 获取空调当前运行状态和参数 |
| 3 | `/api/air-conditioner/set` | POST | 设置用户反馈 | 接收用户反馈信息 |
| 4 | `/api/air-conditioner/performance` | POST | 获取空调性能数据 | 获取空调性能指标和统计数据 |

### 2.2 帖子服务接口

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/posts/{userId}/create` | POST | 创建帖子 | 创建新的帖子内容 |
| 2 | `/posts/posts/add` | POST | 添加帖子 | 添加帖子（模拟20秒处理时间） |
| 3 | `/posts/posts/modify` | POST | 修改帖子 | 修改已存在的帖子内容 |

### 2.3 工业标准接口

#### 2.3.1 电气箱接口

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/api/ebox/power_off` | POST | 断电操作 | 执行断电流程的第一步 |
| 2 | `/api/ebox/verify` | POST | 验证操作 | 验证无电压状态 |
| 3 | `/api/ebox/handle` | POST | 处理操作 | 执行处理步骤 |
| 4 | `/api/ebox/recheck` | POST | 复查操作 | 执行复查步骤 |
| 5 | `/api/ebox/restore` | POST | 恢复操作 | 恢复供电，完成流程 |

#### 2.3.2 方法模式接口

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/api/method/update_production_order` | POST | 更新生产订单 | 更新生产订单状态 |
| 2 | `/api/method/update_device` | POST | 更新设备信息 | 更新设备状态信息 |
| 3 | `/api/method/create_quality_check` | POST | 创建质量检查 | 创建质量检查记录（模拟5秒处理时间） |

### 2.4 CNC 加工管理接口

#### 2.4.1 加工计划接口 (`/cnc/process/plans`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/cnc/process/plans` | POST | 上传加工计划 | 创建新的加工计划 |
| 2 | `/cnc/process/plans/{planId}` | GET | 查询单个加工计划 | 根据计划ID查询计划详情 |
| 3 | `/cnc/process/plans` | GET | 查询所有加工计划 | 获取所有加工计划列表 |
| 4 | `/cnc/process/plans/{planId}/tasks` | GET | 查询计划下所有任务 | 获取指定计划下的所有任务 |

#### 2.4.2 工件管理接口 (`/cnc/process/workpieces`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/cnc/process/workpieces` | POST | 上传工件信息 | 创建新的工件记录 |
| 2 | `/cnc/process/workpieces/{workpieceId}` | GET | 查询工件信息 | 根据工件ID查询详情 |
| 3 | `/cnc/process/workpieces` | GET | 查询所有工件 | 获取所有工件列表 |
| 4 | `/cnc/process/workpieces/{workpieceId}/batch` | POST | 创建批次 | 为工件创建批次 |
| 5 | `/cnc/process/workpieces/batch/{batchId}` | GET | 查询批次信息 | 查询批次详情 |
| 6 | `/cnc/process/workpieces/{workpieceId}/transport` | POST | 上传运输信息 | 记录工件运输信息 |
| 7 | `/cnc/process/workpieces/{workpieceId}/transport` | GET | 查询运输信息 | 查询工件运输记录 |
| 8 | `/cnc/process/workpieces/{workpieceId}/storage` | POST | 上传存储信息 | 记录工件存储信息 |
| 9 | `/cnc/process/workpieces/{workpieceId}/storage` | GET | 查询存储信息 | 查询工件存储记录 |

#### 2.4.3 刀具管理接口 (`/cnc/process/tools`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/cnc/process/tools` | POST | 上传刀具信息 | 创建新的刀具记录 |
| 2 | `/cnc/process/tools/{toolId}` | GET | 查询刀具信息 | 根据刀具ID查询详情 |
| 3 | `/cnc/process/tools/usage` | POST | 上传刀具使用记录 | 记录刀具使用情况 |
| 4 | `/cnc/process/tools/usage/{toolId}` | GET | 查询刀具使用记录 | 查询刀具使用历史 |
| 5 | `/cnc/process/tools/consumption` | POST | 上传刀具消耗记录 | 记录刀具消耗情况 |
| 6 | `/cnc/process/tools/consumption/{toolId}` | GET | 查询刀具消耗记录 | 查询刀具消耗历史 |
| 7 | `/cnc/process/tools/life/{toolId}` | GET | 查询刀具寿命 | 查询刀具剩余寿命 |
| 8 | `/cnc/process/tools/life/{toolId}/adjust` | POST | 调整刀具寿命 | 手动调整刀具寿命值 |

#### 2.4.4 模板和标准接口 (`/cnc/process`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/cnc/process/templates` | POST | 上传工艺模板 | 创建新的工艺模板 |
| 2 | `/cnc/process/templates/{templateId}` | GET | 查询模板信息 | 根据模板ID查询详情 |
| 3 | `/cnc/process/templates` | GET | 查询所有模板 | 获取所有模板列表 |
| 4 | `/cnc/process/standards` | POST | 上传工艺标准 | 创建新的工艺标准 |
| 5 | `/cnc/process/standards/{standardId}` | GET | 查询标准信息 | 根据标准ID查询详情 |
| 6 | `/cnc/process/standards` | GET | 查询所有标准 | 获取所有标准列表 |

#### 2.4.5 环境监控接口 (`/cnc/process/environment`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/cnc/process/environment/temperature` | POST | 上传温度数据 | 记录环境温度 |
| 2 | `/cnc/process/environment/temperature` | GET | 查询温度数据 | 查询温度历史记录 |
| 3 | `/cnc/process/environment/humidity` | POST | 上传湿度数据 | 记录环境湿度 |
| 4 | `/cnc/process/environment/humidity` | GET | 查询湿度数据 | 查询湿度历史记录 |
| 5 | `/cnc/process/environment/air-quality` | POST | 上传空气质量数据 | 记录空气质量指标 |
| 6 | `/cnc/process/environment/air-quality` | GET | 查询空气质量数据 | 查询空气质量历史 |
| 7 | `/cnc/process/environment/summary` | GET | 查询环境监控摘要 | 获取环境监控综合数据 |
| 8 | `/cnc/process/environment/alerts` | GET | 查询环境告警 | 获取环境异常告警信息 |

#### 2.4.6 其他 CNC 接口

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/cnc/process/logs` | POST | 上传加工日志 | 记录加工过程日志 |
| 2 | `/cnc/process/logs/{logId}` | GET | 查询加工日志 | 查询指定日志详情 |
| 3 | `/cnc/process/reports` | POST | 上传加工报告 | 创建加工报告 |
| 4 | `/cnc/process/reports/{reportId}` | GET | 查询加工报告 | 查询指定报告详情 |
| 5 | `/cnc/process/schedules` | POST | 创建生产计划 | 创建生产计划 |
| 6 | `/cnc/process/schedules/{scheduleId}` | GET | 查询生产计划 | 查询计划详情 |

### 2.5 旅游计划接口

#### 2.5.1 基础旅游计划接口 (`/travel-plans/basic`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/travel-plans/basic` | POST | 创建旅游计划 | 创建新的旅游计划 |
| 2 | `/travel-plans/basic/{planId}` | GET | 查询旅游计划 | 查询指定计划详情 |
| 3 | `/travel-plans/basic/{planId}` | PUT | 更新旅游计划 | 更新计划信息 |
| 4 | `/travel-plans/basic/{planId}` | DELETE | 删除旅游计划 | 删除指定计划 |
| 5 | `/travel-plans/basic` | GET | 查询所有计划 | 获取所有旅游计划列表 |

#### 2.5.2 旅游计划扩展接口 (`/travel-plans-extended`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/travel-plans-extended/{planId}/transport/route` | GET | 查询交通路线 | 获取交通路线信息 |
| 2 | `/travel-plans-extended/{planId}/transport/flights` | GET | 查询航班信息 | 获取航班选项 |
| 3 | `/travel-plans-extended/{planId}/transport/trains` | GET | 查询火车信息 | 获取火车选项 |
| 4 | `/travel-plans-extended/{planId}/transport/cost-estimate` | GET | 估算交通费用 | 计算交通费用 |

#### 2.5.3 旅游计划高级接口 (`/travel-plans-advanced`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/travel-plans-advanced/{planId}/attractions/recommendations` | GET | 获取景点推荐 | 获取推荐景点列表 |
| 2 | `/travel-plans-advanced/{planId}/attractions/{attractionId}` | GET | 查询景点详情 | 查询指定景点信息 |
| 3 | `/travel-plans-advanced/{planId}/activities/recommendations` | GET | 获取活动推荐 | 获取推荐活动列表 |

#### 2.5.4 旅游计划预订接口 (`/travel-plans/booking`)

| 序号 | 接口路径 | 请求方法 | 用途 | 说明 |
| --- | ---------- | --- | ---- | --- |
| 1 | `/travel-plans/booking/{planId}/hotels` | POST | 预订酒店 | 创建酒店预订 |
| 2 | `/travel-plans/booking/{planId}/hotels` | GET | 查询酒店预订 | 查询酒店预订信息 |
| 3 | `/travel-plans/booking/{planId}/flights` | POST | 预订航班 | 创建航班预订 |
| 4 | `/travel-plans/booking/{planId}/flights` | GET | 查询航班预订 | 查询航班预订信息 |

## 3. 打包方式

### 3.1 环境要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本

### 3.2 打包步骤

1. **使用 Maven 打包**

```bash
  mvn clean package -Dmaven.test.skip=true
```

2. **打包结果**

打包完成后，会在 `target` 目录下生成以下文件：
- `mocktool-0.0.1-SNAPSHOT.jar` - 可执行的 Spring Boot JAR 包
- `mocktool-0.0.1-SNAPSHOT.jar.original` - 原始 JAR 包（不包含依赖）

3. **运行 JAR 包**

```bash
# 直接运行
java -jar target/mocktool-0.0.1-SNAPSHOT.jar

# 指定端口运行
java -jar target/mocktool-0.0.1-SNAPSHOT.jar --server.port=8081

# 后台运行（Linux/Mac）
nohup java -jar target/mocktool-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

## 4. Docker 容器部署

### 4.1 使用 Docker Compose 部署（QC 环境）

项目使用 Docker Compose 进行容器化部署，采用挂载 JAR 包的方式，无需构建镜像。

#### 4.1.1 启动服务

```bash
# 1. 确保已打包 JAR 文件
mvn clean package -Dmaven.test.skip=true

# 2. 启动服务（后台运行）
docker-compose up -d

# 3. 查看服务状态
docker-compose ps

# 4. 查看日志
docker-compose logs -f
```

#### 4.1.2 停止服务

```bash
# 停止服务
docker-compose down

# 停止服务并删除数据卷
docker-compose down -v
```

#### 4.1.3 重启服务

```bash
# 重启服务
docker-compose restart
```

### 4.2 配置说明

- **部署方式**: Docker Compose 部署
- **日志配置**: 使用 `logback.xml` 进行日志配置，日志文件挂载到 `./logs` 目录
- **数据文件**: 项目使用 `src/main/resources` 目录下的数据文件
- **容器名称**: mocktool

## 5. 接口说明

### 5.1 请求格式

- 所有接口均使用 JSON 格式进行数据交互
- Content-Type: `application/json`

### 5.2 响应格式

标准响应格式：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 5.3 模拟特性

- **延迟模拟**: 部分接口会模拟实际设备的处理延迟（如 2-20 秒）
- **状态管理**: 接口会维护设备状态，支持状态查询和更新
- **随机结果**: 部分接口会随机返回 SUCCESS、FAIL、ERROR 等结果
- **数据持久化**: 使用内存缓存（Guava Cache）进行数据管理

