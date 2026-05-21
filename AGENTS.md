# 项目协作规则

本项目是智慧农场土地认领系统。

## 技术栈
- 后端：Spring Boot 3.x
- 数据库：MySQL
- ORM：MyBatis
- 接口文档：Swagger / Apifox
- 小程序：原生微信小程序
- 代码管理：Git

## 目录结构
- backend/ 后端项目
- miniapp/ 小程序项目
- docs/ 文档

## 分支策略
- main：线上稳定版本
- dev：开发主分支
- feat/*：功能分支
- fix/*：修复分支

## 禁止事项
- 禁止直接推送 main
- 禁止直接推送 dev
- 禁止未测试就提交
- 禁止修改无关模块

## 开发要求
- 后端先出接口文档
- 写完一个模块立刻测试
- 每次改动说明影响范围
- Mapper XML、Entity、Controller 字段必须一致
