package io.admin.modules.system.controller;

import io.admin.common.dto.AjaxResult;
import io.admin.framework.config.security.LoginUser;
import io.admin.modules.common.LoginUtils;
import io.admin.modules.system.dto.request.UpdatePwdRequest;
import io.admin.modules.system.dto.response.UserCenterInfo;
import io.admin.modules.system.dto.response.UserResponse;
import io.admin.modules.system.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userCenter")
public class UserCenterController {

    @Resource
    private SysUserService sysUserService;


    @RequestMapping("info")
    public AjaxResult info() {
        LoginUser sysUser = LoginUtils.getUser();

        UserResponse user = sysUserService.findOneDto(sysUser.getId());

        UserCenterInfo info = new UserCenterInfo();
        info.setName(sysUser.getName());
        info.setDept(user.getDeptLabel());
        info.setEmail(user.getEmail());
        info.setAccount(user.getAccount());
        info.setPhone(user.getPhone());
        info.setRoles(user.getRoleNames());
        info.setUnit(user.getUnitLabel());
        info.setCreateTime(user.getCreateTime());

        return AjaxResult.ok().data(info);
    }


    @PostMapping("updatePwd")
    public AjaxResult updatePwd(@RequestBody UpdatePwdRequest request) {
        String newPassword = request.getNewPassword();
        sysUserService.updatePwd(LoginUtils.getUserId(), newPassword);
        return AjaxResult.ok();
    }
}
