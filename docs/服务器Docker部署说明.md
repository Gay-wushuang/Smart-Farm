# 服务器 Docker 部署说明

本文用于在腾讯云测试服务器部署智慧农场 v2 后端和 MySQL。

## 服务器信息

- 服务器：`82.157.124.170`
- SSH 端口：`22`
- SSH 用户：`root`
- 建议部署目录：`/opt/wisdom-v2.0`
- 后端访问端口：`8081`
- Compose 文件：`docker-compose.yml`

## 一、连接服务器

Termius 中填写：

```text
Address: 82.157.124.170
Port: 22
Username: root
```

或在本机 PowerShell 执行：

```powershell
ssh root@82.157.124.170
```

如果连接失败，先检查腾讯云安全组是否放行 `22` 端口。

## 二、安装 Docker

Ubuntu / Debian 可执行：

```bash
apt update
apt install -y docker.io docker-compose-plugin git
systemctl enable --now docker
docker -v
docker compose version
```

## 三、部署代码

```bash
mkdir -p /opt
cd /opt
git clone <你的仓库地址> wisdom-v2.0
cd wisdom-v2.0
```

如果服务器上已有代码：

```bash
cd /opt/wisdom-v2.0
git pull
```

## 四、配置环境变量

```bash
cp .env.example .env
nano .env
```

Docker Compose 部署时，数据库地址保持：

```env
MYSQL_HOST=mysql
MYSQL_PORT=3306
MYSQL_DATABASE=wisdom_farm
MYSQL_USER=root
MYSQL_PASSWORD=请改成强密码
JWT_SECRET=请改成至少32位随机字符串
```

`.env` 包含数据库密码、JWT 密钥、微信配置，禁止提交到 Git。

## 五、启动服务

```bash
docker compose up -d --build
```

查看状态：

```bash
docker compose ps
docker compose logs -f wisdom-v2-backend
```

首次启动时，MySQL 会自动执行：

```text
docs/database.sql
```

注意：只有 MySQL 数据目录首次创建时会自动初始化 SQL。已有数据卷时，不会重复执行初始化脚本。

## 六、访问验证

浏览器访问：

```text
http://82.157.124.170:8081/
http://82.157.124.170:8081/swagger-ui.html
```

服务器命令验证：

```bash
curl http://127.0.0.1:8081/
curl http://127.0.0.1:8081/swagger-ui.html -I
```

腾讯云安全组需要放行 `8081` 端口。

## 七、常用维护命令

```bash
cd /opt/wisdom-v2.0
docker compose ps
docker compose logs -f
docker compose restart
docker compose up -d --build
docker compose down
```

仅停止容器但保留数据库数据：

```bash
docker compose down
```

清空 MySQL 数据并重新执行 `docs/database.sql`：

```bash
docker compose down -v
docker compose up -d --build
```

执行 `down -v` 会删除数据库数据，只能在确认不需要旧数据时使用。

## 八、小程序联调

开发阶段可把小程序接口地址临时设置为：

```text
http://82.157.124.170:8081
```

微信开发者工具中需要勾选“不校验合法域名”。正式上线建议使用域名和 HTTPS。
