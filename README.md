# 智慧农场土地认领系统

## 项目简介
基于 Spring Boot 3.x + 微信小程序的智慧农场土地认领系统。

## 技术栈
- 后端：Spring Boot 3.2.0 + MyBatis + MySQL + Swagger
- 小程序：原生微信小程序
- 代码管理：Git

## 项目结构
```
wisdom-v2.0/
├── backend/                    # 后端项目
│   ├── src/main/java/com/wisdom/farm/
│   │   ├── controller/        # 控制器层
│   │   ├── service/           # 服务层
│   │   ├── mapper/            # 数据访问层
│   │   ├── entity/            # 实体类
│   │   ├── common/            # 公共类
│   │   └── config/            # 配置类
│   └── src/main/resources/
│       ├── mapper/            # MyBatis XML
│       └── application.yml    # 配置文件
├── miniapp/                   # 小程序项目
│   ├── pages/                 # 页面
│   ├── api/                   # 接口封装
│   ├── utils/                 # 工具函数
│   ├── app.js                 # 小程序入口
│   └── app.json               # 小程序配置
└── docs/                      # 文档
    ├── openapi.yaml           # 接口文档
    └── database.sql           # 数据库脚本
```

## 业务流程
1. 用户打开小程序，微信授权登录
2. 浏览可认领土地，查看详情
3. 选择土地，在线支付租赁
4. 进入"我的地块"，查看实时监控
5. 远程操作（一键浇水/远程施肥）
6. 预约人工种植服务
7. 租赁到期可续租

## 快速开始
### 后端启动
1. 创建数据库，执行 `docs/database.sql`
2. 修改 `backend/src/main/resources/application.yml` 中的数据库配置
3. 启动 `WisdomFarmApplication`
4. 访问 Swagger: http://localhost:8080/swagger-ui.html

### 小程序启动
1. 使用微信开发者工具打开 `miniapp` 目录
2. 修改 `utils/request.js` 中的 BASE_URL
3. 编译运行

## 分支策略
- main: 线上稳定版本
- dev: 开发主分支
- feat/*: 功能分支
- fix/*: 修复分支

## 开发规范
- 后端先出接口文档（Apifox）
- 写完一个模块立刻测试，推git
- 禁止直接推 main 和 dev
