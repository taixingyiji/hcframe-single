package com.taixingyiji.single.common.log;


import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lhc
 * @date 2020.01.17
 */
@Order(3)
@Aspect
@Component
public class OprateLog {

    private static final String LOG_ID = "LOG_ID";
    private static final String OS_SYS_TITLE = "GB_LOGS";
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(OS_SYS_TITLE).tablePk(LOG_ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;

    public OprateLog(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                     TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Around("@annotation(com.taixingyiji.base.module.log.annotation.LogAnno)")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        HashMap<String, Object> user = (HashMap<String, Object>) SecurityUtils.getSubject().getPrincipal();

        // 1.方法执行前的处理，相当于前置通知
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        // 获取方法
        Method method = methodSignature.getMethod();
        // 获取方法上面的注解
        LogAnno logAnno = method.getAnnotation(LogAnno.class);
        ResultVO result = ResultVO.getSuccess();
        if (!logAnno.isBefore()) {
            result = (ResultVO) pjp.proceed();
        }

        // 获取操作记录的日志表
        Map<String, Object> log = new HashMap<String, Object>();
        log.put("CREATE_TIME", new Date());
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip)) {
            ip = request.getRemoteAddr();
        }
        log.put("IP", ip);
        log.put("CURL", request.getRequestURI());
        log.put("ACTTYPE", request.getMethod());
        log.put("CLOG", "参数：" + getRequestParamsByProceedingJoinPoint(pjp));
        log.put("OPERATETYPE", logAnno.operateType());
        log.put("MODULENAME", logAnno.moduleName());
        if (user == null) {
            return ResultVO.getNoLogin();
        }
        log.put("USERID", user.get("CREDIT_CODE"));
        tableService.saveWithDate(TABLE_INFO, log);

        return result;
    }

    /**
     * 获取入参
     *
     * @param proceedingJoinPoint
     * @return
     */
    private Map<String, Object> getRequestParamsByProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = proceedingJoinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        Map<String, Object> requestParams = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            if ((value instanceof HttpServletRequest) || (value instanceof HttpServletResponse)) {
                continue;
            }
            //如果是文件对象
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                // 获取文件名
                value = file.getOriginalFilename();
            }
            requestParams.put(paramNames[i], value);
        }
        return requestParams;
    }

}
