package io.admin.modules.system.controller;


import io.admin.modules.common.LoginUtils;
import io.admin.modules.system.service.SysUserMessageService;
import io.admin.common.dto.AjaxResult;
import io.admin.modules.system.entity.SysUserMessage;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("admin/user/msg")
public class UserMsgController {


    @Resource
    SysUserMessageService sysUserMsgService;


    @RequestMapping("page")
    public AjaxResult page(Boolean read, @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) throws SQLException {
        String userId = LoginUtils.getUserId();
        Page<SysUserMessage> page = sysUserMsgService.findByUser(userId, read, pageable);
        return AjaxResult.ok().data(page);
    }

    @PostMapping("read")
    public AjaxResult read(@RequestBody SysUserMessage msg) {
        sysUserMsgService.read(msg.getId());

        return AjaxResult.ok();
    }



}
