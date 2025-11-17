package io.admin.modules.flowable.system;


import io.admin.framework.config.security.LoginUser;
import io.admin.modules.common.LoginUtils;
import io.admin.modules.flowable.flowable.FlowableLoginUser;
import io.admin.modules.flowable.flowable.FlowableLoginUserProvider;
import io.admin.modules.system.dto.response.UserResponse;
import io.admin.modules.system.entity.SysOrg;
import io.admin.modules.system.entity.SysUser;
import io.admin.modules.system.service.SysOrgService;
import io.admin.modules.system.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


@Component
public class LoginUserProviderImpl implements FlowableLoginUserProvider {

    @Resource
    SysOrgService sysOrgService;

    @Resource
    SysUserService sysUserService;


    @Override
    public FlowableLoginUser currentLoginUser() {
        LoginUser sysUser = LoginUtils.getUser();
        UserResponse user = sysUserService.findOneDto(sysUser.getId());

        FlowableLoginUser fu = new FlowableLoginUser();
        fu.setId(user.getId());
        fu.setName(user.getName());
        fu.setSuperAdmin(false);
        fu.setDeptId(user.getDeptId());
        fu.setDeptName(user.getDeptLabel());
        fu.setUnitName(user.getUnitId());
        fu.setUnitName(user.getUnitLabel());


        // 获取部门领导
        if(fu.getDeptId() != null){
            SysOrg org = sysOrgService.findOne(fu.getDeptId());

            if(org != null){
                SysUser deptLeader = org.getLeader();
                if(deptLeader != null){
                    fu.setDeptLeaderId(deptLeader.getId());
                }
            }
        }

        return fu;
    }

}
