package com.wisdom.farm.handler;

import com.wisdom.farm.common.Result;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public Result<Object> handleIllegalArgument(RuntimeException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public Result<Object> handleSecurity(SecurityException e) {
        return Result.error(403, e.getMessage());
    }

    @ExceptionHandler({MyBatisSystemException.class, SQLException.class, DataAccessException.class})
    public Result<Object> handleDatabaseException(Exception e) {
        return Result.error("数据库访问失败，请检查数据库是否已启动、库名和账号密码是否正确");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Object> handleNoResourceFound(NoResourceFoundException e) {
        Result<Object> result = Result.error("资源不存在");
        result.setCode(404);
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        return Result.error("服务器内部错误");
    }
}
