
package io.admin.modules.system.service;

import io.admin.common.dto.AjaxResult;
import io.admin.common.utils.IpAddressTool;
import io.admin.common.utils.RequestTool;
import io.admin.framework.config.security.LoginUser;
import io.admin.framework.data.service.BaseService;
import io.admin.framework.log.Log;
import io.admin.modules.common.LoginUtils;
import io.admin.modules.system.dao.SysOpLogDao;
import io.admin.modules.system.entity.SysLog;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Date;

@Slf4j
@Service
public class SysLogService extends BaseService<SysLog> {


    @Resource
    private SysOpLogDao dao;

    public void saveOperationLog(JoinPoint joinPoint, long duration, String params, AjaxResult result) {
        Date now = new Date();

        HttpServletRequest request = RequestTool.currentRequest();
        String ip = IpAddressTool.getIp(request);

        LoginUser loginUser = LoginUtils.getUser();
        if (loginUser == null) {
            return;
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Log methodAnn = method.getAnnotation(Log.class);

        SysLog sysLog = new SysLog();
        sysLog.setOperation(methodAnn.value());
        sysLog.setIp(ip);
        sysLog.setOperationTime(now);
        sysLog.setDuration((int) duration);
        sysLog.setUserId(loginUser.getId());
        sysLog.setUsername(loginUser.getName());
        sysLog.setParams(params);
        sysLog.setSuccess(result.isSuccess());
        if(!result.isSuccess()){
            sysLog.setError(result.getMessage());
        }
        dao.save(sysLog);
    }




}
