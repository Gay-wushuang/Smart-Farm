你们这个项目我建议**先接 4 个 MCP，不要一开始接太多**。

结合你上传的技术栈：后端是 **Spring Boot 3.x + MySQL + MyBatis + Swagger**，小程序是原生，代码管理用 **Git / Gitee**，并且你们要求后端先出 Apifox 接口文档、禁止直接推 `dev` 和 `main`。

## 直接建议：先接这 4 个

### 1. Filesystem MCP：必接

作用：让 Codex 直接读写你们项目文件。

适合你们现在这种结构：

```text
backend/
miniapp/
docs/
```

Codex 可以帮你：

```text
生成 Spring Boot Controller
生成 Service / Mapper / Entity
生成 MyBatis XML
生成小程序 api 封装
检查目录结构是否符合约定
```

---

### 2. MySQL MCP：必接

作用：让 Codex 能看数据库表结构、辅助写 SQL、检查字段设计。

你们项目有这些业务：

```text
土地认领
土地详情
订单支付
我的地块
监控画面
远程浇水
远程施肥
人工种植预约
续租
运营后台管理
```

所以 MySQL MCP 很有用。

Codex 可以帮你：

```text
根据表结构生成 Entity
根据业务生成 Mapper
检查 SQL 是否能跑
帮你设计订单表、土地表、预约表、设备表
```

注意：**先只给只读账号**，不要一开始给 Codex 写库权限。

---

### 3. Git / Gitee MCP：建议接

你们代码管理是 Git / Gitee，且有明确分支策略：`main` 是线上稳定版本，`dev` 是开发主分支，功能分支从 `dev` 开，禁止直接推 `dev` 和 `main`。

Codex 可以帮你：

```text
读 issue
读 PR
检查本次改动
根据需求创建功能分支
生成 commit message
检查是否误改 main / dev
```

如果暂时找不到稳定的 Gitee MCP，就先用：

```text
Filesystem MCP + Git 命令
```

也能满足 80% 需求。

---

### 4. Swagger / OpenAPI MCP：建议接

你们要求“后端先出接口文档 Apifox”。

所以要让 Codex 能围绕接口文档工作。

可以让 Codex 读取：

```text
openapi.yaml
swagger.json
Apifox 导出的 OpenAPI 文件
```

然后它可以帮你：

```text
根据接口文档生成 Controller
根据 Controller 反推接口文档
检查前后端字段是否一致
给小程序生成 api 请求方法
```

这个对你们团队协作非常关键。

---

## 暂时不要急着接的 MCP

你们现在先别急着接遥感、GIS、QGIS、地图类 MCP。

因为从你上传的流程图来看，当前业务更像是 **智慧农场 / 土地认领 / 远程管理 / 后台运营**，还没到真正的“遥感土地监控”阶段。流程里主要是：用户打开小程序、授权登录、浏览土地、查看详情、支付租赁、进入我的地块、查看监控、远程浇水/施肥、人工预约、续租；运营端则是土地管理、订单处理、派单、设备管理和数据统计。

所以现阶段重点不是 GIS，而是：

```text
业务接口
数据库
小程序
后台管理
订单流程
设备控制接口
```

等你们后面真的要做“卫星遥感监控、地块变化检测、地图图层”，再接：

```text
QGIS MCP
PostGIS MCP
GDAL / Rasterio MCP
地图服务 MCP
```

---

# 你们现在应该这样接

## 第一步：项目根目录建 `.codex/config.toml`

在项目根目录：

```bash
mkdir .codex
```

新建：

```text
.codex/config.toml
```

Codex 官方文档说明，MCP 配置可以放在用户级 `~/.codex/config.toml`，也可以放在项目级 `.codex/config.toml`；CLI 和 IDE 扩展共享这个配置。([OpenAI 开发者][1])

---

## 第二步：先接 Filesystem MCP

示例配置：

```toml
[mcp_servers.filesystem]
command = "npx"
args = [
  "-y",
  "@modelcontextprotocol/server-filesystem",
  "D:/你的项目路径/智慧农场"
]
```

把路径换成你们真实项目路径，例如：

```toml
[mcp_servers.filesystem]
command = "npx"
args = [
  "-y",
  "@modelcontextprotocol/server-filesystem",
  "D:/project/smart-farm"
]
```

不要写成整个 D 盘：

```toml
# 不建议
"D:/"
```

只开放项目目录就行。

---

## 第三步：接 MySQL MCP

先建一个只读数据库账号。

MySQL 里执行类似：

```sql
CREATE USER 'codex_readonly'@'%' IDENTIFIED BY '你的密码';

GRANT SELECT ON smart_farm.* TO 'codex_readonly'@'%';

FLUSH PRIVILEGES;
```

然后在 `.codex/config.toml` 加：

```toml
[mcp_servers.mysql]
command = "npx"
args = [
  "-y",
  "@modelcontextprotocol/server-mysql"
]
env = {
  MYSQL_HOST = "127.0.0.1",
  MYSQL_PORT = "3306",
  MYSQL_USER = "codex_readonly",
  MYSQL_PASSWORD = "你的密码",
  MYSQL_DATABASE = "smart_farm"
}
```

如果你们用的 MySQL MCP 包名不是这个，就按实际包名改。不同社区 MCP 的环境变量名字可能不一样。

---

## 第四步：接 Git / Gitee

如果你们只是让 Codex 本地操作 Git，Filesystem MCP 已经够用了。

但是建议在项目根目录加一个 `AGENTS.md`，告诉 Codex 你们的分支规则。

新建：

```text
AGENTS.md
```

内容可以直接写：

```md
# 项目协作规则

本项目是智慧农场系统。

技术栈：
- 后端：Spring Boot 3.x
- 数据库：MySQL
- ORM：MyBatis
- 接口文档：Swagger / Apifox
- 小程序：原生微信小程序
- 代码管理：Git / Gitee

目录结构：
- backend/ 后端项目
- miniapp/ 小程序项目
- docs/ 文档

分支策略：
- main：线上稳定版本
- dev：开发主分支
- feat/*：功能分支
- fix/*：修复分支

禁止：
- 禁止直接推送 main
- 禁止直接推送 dev
- 禁止未测试就提交
- 禁止修改无关模块

开发要求：
- 后端先出接口文档
- 写完一个模块立刻测试
- 每次改动说明影响范围
- Mapper XML、Entity、Controller 字段必须一致
```

这样 Codex 每次进入项目时会更容易遵守你们规则。

---

## 第五步：接 OpenAPI / Swagger 文件

建议你们建：

```text
docs/openapi.yaml
```

或者：

```text
docs/swagger.json
```

然后让 Codex 通过 Filesystem MCP 读取它。

你可以对 Codex 说：

```text
根据 docs/openapi.yaml，生成 backend 里的用户登录、土地列表、土地详情接口。
要求符合 Spring Boot 3.x + MyBatis 项目结构。
```

也可以说：

```text
检查 miniapp/api/land.js 和 backend/controller/LandController.java 的字段是否一致。
```

这一步不用单独装很多东西，**先用文件方式接入就够了**。

---

# 我建议你们的 `.codex/config.toml` 初版

可以先这样写：

```toml
# .codex/config.toml

[mcp_servers.filesystem]
command = "npx"
args = [
  "-y",
  "@modelcontextprotocol/server-filesystem",
  "D:/project/smart-farm"
]

[mcp_servers.mysql]
command = "npx"
args = [
  "-y",
  "@modelcontextprotocol/server-mysql"
]
env = {
  MYSQL_HOST = "127.0.0.1",
  MYSQL_PORT = "3306",
  MYSQL_USER = "codex_readonly",
  MYSQL_PASSWORD = "你的密码",
  MYSQL_DATABASE = "smart_farm"
}
```

然后在 Codex 里输入：

```text
/mcp
```

检查 MCP 是否生效。Codex 官方文档也说明，在 TUI 里可以用 `/mcp` 查看当前启用的 MCP server。([OpenAI 开发者][1])

---

# 你们第一批可以让 Codex 做什么

接完以后，不要一上来让它“做完整项目”。

先按模块来：

## 任务 1：让 Codex 生成数据库表

输入：

```text
根据智慧农场业务流程，帮我设计 MySQL 表结构。
需要包括：
1. 用户表
2. 土地表
3. 土地认领订单表
4. 我的地块表
5. 监控设备表
6. 浇水/施肥操作记录表
7. 人工种植预约表
8. 续租记录表
9. 后台管理员表

要求：
- 适合 Spring Boot 3.x + MyBatis
- 字段用下划线命名
- 每张表要有 id、create_time、update_time、deleted
- 给出建表 SQL
```

---

## 任务 2：让 Codex 生成接口文档

输入：

```text
根据数据库表和业务流程，生成 docs/openapi.yaml。
先生成这些接口：
- 微信授权登录
- 土地列表
- 土地详情
- 创建认领订单
- 支付回调
- 我的地块列表
- 查看监控画面
- 一键浇水
- 远程施肥
- 人工种植预约
- 续租订单
- 后台土地管理
- 后台订单处理
- 后台设备状态管理

要求兼容 Apifox 导入。
```

---

## 任务 3：让 Codex 生成后端代码

输入：

```text
根据 docs/openapi.yaml，生成 Spring Boot 后端代码。
目录必须符合：
backend/src/main/java/com/xxx/controller
backend/src/main/java/com/xxx/service
backend/src/main/java/com/xxx/mapper
backend/src/main/java/com/xxx/entity
backend/src/main/resources/mapper

先生成土地模块：
- 土地列表
- 土地详情
- 后台新增土地
- 后台编辑土地
- 后台修改土地状态

要求：
- Controller 只处理参数和返回
- Service 写业务逻辑
- Mapper 使用 MyBatis
- XML 写 SQL
- 返回统一 Result<T>
```

---

## 任务 4：让 Codex 生成小程序接口封装

输入：

```text
根据 docs/openapi.yaml，生成 miniapp/api 目录下的请求封装。
先生成：
- api/user.js
- api/land.js
- api/order.js
- api/device.js
- api/appointment.js

要求：
- 原生微信小程序
- 统一封装 request
- token 从 wx.getStorageSync('token') 获取
- 接口错误统一处理
```

---

# 你们后面再接的 MCP

等第一版业务跑通以后，再加这些：

## 设备控制 MCP

如果你们真的有浇水、施肥硬件，可以自建一个：

```text
device-control-mcp
```

提供工具：

```text
get_device_status
start_watering
stop_watering
start_fertilizing
get_device_logs
```

但这个一定要加权限确认，不能让 Codex 随便开设备。

## 地图 / GIS MCP

如果后续要做土地地图：

```text
PostGIS MCP
QGIS MCP
Map Server MCP
```

这时候数据库也要从 MySQL 部分升级到：

```text
MySQL + 空间字段
或
PostgreSQL + PostGIS
```

如果只是普通智慧农场认领系统，MySQL 够用；如果要做地块边界、空间查询、地图叠加，PostGIS 更合适。

---

# 最终结论

你们现在最应该接：

```text
1. Filesystem MCP
2. MySQL MCP
3. Git / Gitee 能力
4. OpenAPI / Swagger 文件能力
```

暂时不要优先接：

```text
QGIS
遥感
卫星影像
PostGIS
```

除非你们已经要做“地图边界、地块坐标、遥感变化检测”。

你们当前第一步应该是：

```text
建 .codex/config.toml
接 Filesystem MCP
接 MySQL 只读 MCP
写 AGENTS.md 约束团队规范
建 docs/openapi.yaml
让 Codex 先生成数据库表和接口文档
```

这样最稳。

[1]: https://developers.openai.com/codex/mcp?utm_source=chatgpt.com "Model Context Protocol – Codex"

