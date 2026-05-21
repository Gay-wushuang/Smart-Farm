---
name: springboot-multimodule-design
description: Spring Boot 多模块分层架构设计规范——适用于中大型 Java 后端项目的模块划分、分层设计、DTO/VO 隔离、统一响应、全局异常、AOP 自动填充等最佳实践
---

# Spring Boot 多模块分层架构设计规范

## 概述

本技能基于标准 Java 后端项目的分层架构设计方法论，适用于 Spring Boot + MyBatis 技术栈的中大型项目。核心思想是 **职责分离、单向依赖、统一规范**。

**适用场景：** 外卖系统、电商后台、企业管理平台等需要清晰分层的 Web 后端项目。

---

## 一、多模块划分原则

### 1.1 三模块标准结构

```
project-name (父工程，仅做依赖版本管理)
│
├── xxx-common    ← 基础层：工具、常量、异常、配置属性
├── xxx-pojo      ← 数据层：实体类、DTO、VO
└── xxx-server    ← 应用层：控制器、服务、Mapper、配置
```

### 1.2 依赖关系规则

```
xxx-server ──依赖──► xxx-common
xxx-server ──依赖──► xxx-pojo
xxx-common 和 xxx-pojo 互不依赖
```

**核心约束：**
- 依赖方向严格单向，禁止循环依赖
- common 和 pojo 是纯 library，不依赖 Spring Web
- server 是唯一可部署模块，使用 `spring-boot-maven-plugin` 打包

### 1.3 父工程 POM 设计

```xml
<!-- 父工程 packaging 必须为 pom -->
<packaging>pom</packaging>

<modules>
    <module>xxx-common</module>
    <module>xxx-pojo</module>
    <module>xxx-server</module>
</modules>

<!-- 统一管理依赖版本 -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.0</version>
        </dependency>
        <!-- 其他版本声明 -->
    </dependencies>
</dependencyManagement>
```

---

## 二、各模块内部结构

### 2.1 common 模块 — 基础设施层

```
xxx-common/src/main/java/com/xxx/
├── constant/          # 常量类
│   ├── StatusConstant.java        # 状态码（启用/禁用）
│   ├── MessageConstant.java       # 提示消息
│   ├── JwtClaimsConstant.java     # JWT 字段名
│   └── PasswordConstant.java      # 密码相关常量
├── context/           # 线程上下文
│   └── BaseContext.java           # ThreadLocal 传递用户 ID 等
├── enumeration/       # 枚举类
│   └── OperationType.java         # INSERT / UPDATE 操作类型
├── exception/         # 业务异常体系
│   ├── BaseException.java         # 异常基类
│   ├── LoginFailedException.java
│   ├── OrderBusinessException.java
│   └── ...                        # 按业务域命名
├── json/              # 序列化配置
│   └── JacksonObjectMapper.java
├── properties/        # 配置属性类
│   ├── JwtProperties.java        # @ConfigurationProperties
│   ├── AliOssProperties.java
│   └── WeChatProperties.java
├── result/            # 统一响应封装
│   ├── Result.java                # 通用响应
│   └── PageResult.java           # 分页响应
└── utils/             # 工具类
    ├── JwtUtil.java
    ├── AliOssUtil.java
    ├── HttpClientUtil.java
    └── WeChatPayUtil.java
```

**设计要点：**
- 常量类按职责拆分，不要一个大而全的 Constants 类
- 异常类按业务域命名，便于全局异常处理器分类处理
- `@ConfigurationProperties` 类实现类型安全的配置绑定
- 工具类保持无状态，方法签名清晰

### 2.2 pojo 模块 — 数据模型层

```
xxx-pojo/src/main/java/com/xxx/
├── entity/            # 数据库实体（与表一一对应）
│   ├── Employee.java
│   ├── Dish.java
│   ├── Orders.java
│   └── ...
├── dto/               # 数据传输对象（请求参数）
│   ├── EmployeeDTO.java
│   ├── EmployeeLoginDTO.java
│   ├── DishPageQueryDTO.java     # 分页查询专用
│   └── ...
└── vo/                # 视图对象（响应数据）
    ├── EmployeeLoginVO.java
    ├── OrderReportVO.java
    └── ...
```

### 2.3 DTO / VO / Entity 三类分离规范

| 类型 | 命名后缀 | 数据流向 | 用途 |
|------|----------|----------|------|
| **Entity** | 无后缀 | 数据库 ↔ Java | 表映射，字段与表列一一对应 |
| **DTO** | `DTO` | 前端 → 后端 | 请求入参，可包含校验注解 |
| **VO** | `VO` | 后端 → 前端 | 响应出参，裁剪/聚合后的数据 |
| **QueryDTO** | `PageQueryDTO` | 前端 → 后端 | 分页/条件查询专用 |

**为什么不能用 Entity 直接做接口入参/出参：**
1. 泄露数据库结构（表名、字段名、内部 ID）
2. 包含多余字段（如 createBy、isDeleted 等内部审计字段）
3. 无法灵活聚合（一个 VO 可能需要关联多张表的数据）
4. 无法针对不同接口做差异化校验

### 2.4 server 模块 — 业务应用层

```
xxx-server/src/main/java/com/xxx/
├── XxxApplication.java            # 启动类
├── annotation/        # 自定义注解
│   └── AutoFill.java              # 自动填充标记
├── aspect/            # AOP 切面
│   └── AutoFillAspect.java        # 自动填充审计字段
├── config/            # Spring 配置类
│   ├── WebMvcConfiguration.java   # 拦截器、CORS
│   ├── RedisConfiguration.java
│   └── OssConfiguration.java
├── controller/        # 控制器（按角色分包）
│   ├── admin/                     # 管理端接口
│   │   ├── EmployeeController.java
│   │   ├── DishController.java
│   │   └── ...
│   └── user/                      # 用户端接口（可选）
├── handler/           # 全局异常处理
│   └── GlobalExceptionHandler.java
├── interceptor/       # 拦截器
│   └── JwtTokenAdminInterceptor.java
├── mapper/            # MyBatis Mapper 接口
│   ├── EmployeeMapper.java
│   └── ...
└── service/           # 业务逻辑
    ├── EmployeeService.java       # 接口
    └── impl/
        └── EmployeeServiceImpl.java  # 实现
```

---

## 三、核心设计模式

### 3.1 统一响应封装

```java
/**
 * 统一 API 响应结构
 * 所有接口必须返回 Result<T>，保证前端解析一致
 */
@Data
public class Result<T> {
    private Integer code;   // 1=成功, 0=失败
    private String msg;     // 提示信息
    private T data;         // 响应数据

    public static <T> Result<T> success() { ... }
    public static <T> Result<T> success(T data) { ... }
    public static <T> Result<T> error(String msg) { ... }
}
```

**Controller 使用示例：**

```java
@PostMapping
public Result<String> addEmployee(@RequestBody EmployeeDTO dto) {
    employeeService.save(dto);
    return Result.success();
}

@GetMapping("/{id}")
public Result<EmployeeVO> getById(@PathVariable Long id) {
    EmployeeVO vo = employeeService.getById(id);
    return Result.success(vo);
}
```

### 3.2 统一异常处理

```java
/**
 * 全局异常处理器
 * 捕获所有 Controller 层抛出的异常，统一返回格式
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 业务异常 → 返回具体消息
    @ExceptionHandler(BaseException.class)
    public Result<String> handleBusinessException(BaseException ex) {
        log.error("业务异常: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    // 参数校验异常 → 返回字段错误信息
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return Result.error(msg);
    }

    // 未知异常 → 返回通用错误，记录日志
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error("系统繁忙，请稍后再试");
    }
}
```

**异常体系设计：**

```
BaseException (业务异常基类)
├── LoginFailedException          # 登录失败
├── AccountLockedException        # 账号被锁定
├── PasswordErrorException        # 密码错误
├── OrderBusinessException        # 订单业务异常
├── DeletionNotAllowedException   # 不允许删除
└── ...
```

### 3.3 AOP 自动填充审计字段

```java
/**
 * 自定义注解：标记需要自动填充的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();  // INSERT 或 UPDATE
}
```

```java
/**
 * AOP 切面：拦截 Mapper 层方法，自动填充公共字段
 */
@Aspect
@Component
public class AutoFillAspect {

    @Before("@annotation(autoFill)")
    public void autoFill(JoinPoint joinPoint, AutoFill autoFill) {
        Object entity = joinPoint.getArgs()[0];
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        if (autoFill.value() == OperationType.INSERT) {
            // 反射设置 createTime, updateTime, createUser, updateUser
        } else if (autoFill.value() == OperationType.UPDATE) {
            // 反射设置 updateTime, updateUser
        }
    }
}
```

**Mapper 接口使用：**

```java
@AutoFill(OperationType.INSERT)
void insert(Employee employee);

@AutoFill(OperationType.UPDATE)
void update(Employee employee);
```

### 3.4 JWT 认证拦截器

```java
/**
 * 管理端 JWT 认证拦截器
 * 拦截 /admin/** 路径，校验 Token 有效性
 */
@Component
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // 1. 获取 Token
        String token = request.getHeader("token");

        // 2. 校验 Token
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            BaseContext.setCurrentId(empId);  // 存入 ThreadLocal
            return true;
        } catch (Exception ex) {
            response.setStatus(401);
            return false;
        }
    }
}
```

```java
// 注册拦截器
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtTokenAdminInterceptor)
        .addPathPatterns("/admin/**")
        .excludePathPatterns("/admin/employee/login");
}
```

### 3.5 多环境配置分离

```
resources/
├── application.yml              # 主配置（引用变量占位符）
├── application-dev.yml          # 开发环境
├── application-test.yml         # 测试环境
├── application-prod.yml         # 生产环境
└── mapper/                      # MyBatis XML
    ├── EmployeeMapper.xml
    └── ...
```

**主配置引用变量：**

```yaml
spring:
  datasource:
    druid:
      driver-class-name: ${sky.datasource.driver}
      url: jdbc:mysql://${sky.datasource.host}:${sky.datasource.port}/${sky.datasource.database}
      username: ${sky.datasource.username}
      password: ${sky.datasource.password}
```

**dev 环境填入实际值：**

```yaml
sky:
  datasource:
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: 123456
```

---

## 四、设计检查清单

在搭建新项目或审查架构时，确认以下各项：

### 模块划分
- [ ] 父工程 packaging 为 pom，仅管理版本
- [ ] 依赖方向单向：server → common, server → pojo
- [ ] common 和 pojo 互不依赖
- [ ] server 是唯一可部署模块

### 数据模型
- [ ] Entity 与数据库表一一对应
- [ ] 接口入参使用 DTO，不直接用 Entity
- [ ] 接口出参使用 VO，裁剪敏感/无关字段
- [ ] 分页查询使用独立的 PageQueryDTO

### 统一规范
- [ ] 所有接口返回 Result<T>
- [ ] 业务异常继承 BaseException
- [ ] 全局异常处理器覆盖所有异常类型
- [ ] 审计字段（createTime 等）通过 AOP 自动填充

### 安全认证
- [ ] JWT Token 通过拦截器统一校验
- [ ] 当前用户 ID 存入 ThreadLocal，不在方法间传参
- [ ] 敏感配置（密钥、密码）通过 Profile 分离，不硬编码

### 可维护性
- [ ] 常量类按职责拆分
- [ ] Controller 按角色/功能分包
- [ ] Service 接口与实现分离
- [ ] MyBatis XML 与 Mapper 接口对应

---

## 五、常见反模式与纠正

### 反模式一：单体大 Controller

```
# 坏：一个 Controller 几百行，所有接口混在一起
# 好：按业务域拆分
controller/
├── admin/
│   ├── EmployeeController.java   # 员工管理
│   ├── DishController.java       # 菜品管理
│   ├── OrderController.java      # 订单管理
│   └── ReportController.java     # 数据统计
```

### 反模式二：Entity 直接做接口参数

```java
# 坏：直接暴露数据库实体
@PostMapping
public Result addEmployee(@RequestBody Employee employee) { ... }

# 好：用 DTO 隔离
@PostMapping
public Result addEmployee(@RequestBody EmployeeDTO dto) { ... }
```

### 反模式三：Service 层不写接口

```java
# 坏：直接写实现类
@Service
public class EmployeeService { ... }

# 好：接口与实现分离（便于替换、代理、测试）
public interface EmployeeService { ... }

@Service
public class EmployeeServiceImpl implements EmployeeService { ... }
```

### 反模式四：硬编码魔法值

```java
# 坏：到处写 1 和 0
if (status == 1) { ... }

# 好：使用常量类
if (status == StatusConstant.ENABLE) { ... }
```

### 反模式五：配置写死在代码里

```java
# 坏：数据库连接硬编码
String url = "jdbc:mysql://localhost:3306/sky_take_out";

# 好：通过 @ConfigurationProperties 绑定 yml
@Autowired
private JwtProperties jwtProperties;
```

---

## 六、技术栈参考

| 功能模块 | 推荐技术 |
|----------|----------|
| Web 框架 | Spring Boot 2.7.x / 3.x |
| ORM | MyBatis / MyBatis-Plus |
| 数据库 | MySQL 8.0 |
| 连接池 | Druid / HikariCP |
| 缓存 | Spring Data Redis |
| 认证 | JWT (JJWT) |
| 文件存储 | 阿里云 OSS / MinIO |
| API 文档 | Knife4j (Swagger) |
| 分页 | PageHelper |
| AOP | Spring AOP / AspectJ |
| 日志 | SLF4J + Logback |
| 工具库 | Lombok, Hutool, MapStruct |
