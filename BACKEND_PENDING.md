# 后端接口待确认清单

当前小程序仍可通过 mock 数据运行。以下项目缺乏实际后端地址或接口确认，已在代码中用 `[BACKEND_PENDING]` 标记。

## 基础地址

- `api/config.js`
  - `BASE_URL = 'TODO_REPLACE_WITH_REAL_BACKEND_URL'`
  - 需要替换为真实后端域名或网关地址。
  - 在替换前，请保持 mock 模式用于本地 UI 联调。

## 接口路径

- `api/land.js`
  - `GET /lands/{landId}/camera`
  - 用途：获取地块实时监控视频流。
  - 待确认：真实接口路径、返回字段、视频流格式和鉴权方式。

- `api/order.js`
  - `POST /lands/{landId}/renew`
  - 用途：创建续租订单或续租申请。
  - 待确认：是否直接创建订单、是否需要接入支付创建接口、请求参数和返回字段。

## 当前 mock 覆盖

- 登录：`POST /auth/wx-login`
- 绑定手机号：`POST /auth/bind-phone`
- 用户信息：`GET /user/profile`
- 土地列表：`GET /lands`
- 土地详情：`GET /lands/{landId}`
- 土地认领：`POST /lands/{landId}/claim`
- 我的土地：`GET /user/lands`
- 消息通知：`GET /notifications`
- 标记已读：`PUT /notifications/{notificationId}/read`
- 全部已读：`PUT /notifications/read-all`
- 创建服务订单：`POST /orders`
- 订单详情：`GET /orders/{orderId}`
- 我的订单：`GET /user/orders`
- 创建支付：`POST /pay/create`
- 支付状态：`GET /pay/status/{paymentId}`
