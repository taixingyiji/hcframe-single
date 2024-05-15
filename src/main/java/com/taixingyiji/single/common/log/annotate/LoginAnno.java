package com.taixingyiji.single.common.log.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 方法注解
@Target(ElementType.METHOD)
// 运行时可见
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAnno {

    // 记录日志的操作类型
    String operateType();
    // 记录日志主键
    String primaryKey() default "id";
    // 记录模块
    String moduleName() default "-";

    boolean isBefore() default false;
}
