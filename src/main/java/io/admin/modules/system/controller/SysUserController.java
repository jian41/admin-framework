
package io.admin.modules.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.PasswdStrength;
import cn.hutool.core.util.StrUtil;
import io.admin.framework.data.query.JpaQuery;
import io.admin.common.dto.table.Table;
import io.admin.common.dto.AjaxResult;
import io.admin.common.dto.antd.Option;
import io.admin.framework.log.Log;
import io.admin.modules.common.LoginTool;
import io.admin.modules.system.dto.request.GrantUserPermRequest;
import io.admin.modules.system.dto.response.UserResponse;
import io.admin.modules.system.entity.OrgType;
import io.admin.modules.system.entity.SysOrg;
import io.admin.modules.system.entity.SysUser;
import io.admin.modules.system.service.SysConfigService;
import io.admin.modules.system.service.SysOrgService;
import io.admin.modules.system.service.SysUserService;
import io.admin.framework.config.security.HasPermission;
import io.admin.framework.config.argument.RequestBodyKeys;
import io.admin.framework.perm.SecurityUtils;
import io.admin.framework.perm.Subject;
import io.admin.framework.data.domain.BaseEntity;
import io.admin.framework.pojo.param.DropdownParam;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("admin/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysConfigService configService;

    @Resource
    private SysOrgService sysOrgService;


    @HasPermission("sysUser:view")
    @RequestMapping("page")
    public AjaxResult page(String orgId, String roleId, String searchText, @PageableDefault(sort = "updateTime", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<UserResponse> page = sysUserService.findAll(orgId, roleId, searchText, pageable);

        return AjaxResult.ok().data(page);
    }


    @Log("用户-保存")
    @HasPermission("sysUser:save")
    @PostMapping("save")
    public AjaxResult save(@RequestBody SysUser input, RequestBodyKeys updateFields) throws Exception {
        boolean isNew = input.isNew();
        String inputOrgId = input.getDeptId();
        SysOrg org = sysOrgService.findOneByRequest(inputOrgId);
        if (org.getType() == OrgType.UNIT) {
            input.setUnitId(inputOrgId);
            input.setDeptId(null);
        } else {
            SysOrg unit = sysOrgService.findParentUnit(org);
            Assert.notNull(unit, "部门%s没有所属单位".formatted(org.getName()));
            input.setUnitId(unit.getId());
        }


        updateFields.add("unitId");
        sysUserService.saveOrUpdateByRequest(input, updateFields);

        if (isNew) {
            return AjaxResult.ok().msg("添加成功,密码：" + configService.getDefaultPassWord());
        }

        return AjaxResult.ok();
    }


    @Log("用户-删除")
    @HasPermission("sysUser:delete")
    @GetMapping("delete")
    public AjaxResult delete(String id) {
        sysUserService.delete(id);
        return AjaxResult.ok();
    }


    /**
     * 检查密码强度
     *
     * @param password
     */
    @GetMapping("pwdStrength")
    public AjaxResult pwdStrength(String password) {
        if (StrUtil.isEmpty(password)) {
            return AjaxResult.err().msg("请输入密码");
        }

        PasswdStrength.PASSWD_LEVEL level = PasswdStrength.getLevel(password);

        if (level == PasswdStrength.PASSWD_LEVEL.EASY) {
            return AjaxResult.err().msg("密码强度太低");
        }

        return AjaxResult.ok().data(level);
    }


    @Log("用户-重置密码")
    @HasPermission("sysUser:resetPwd")
    @PostMapping("resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        sysUserService.resetPwd(user.getId());
        String defaultPassWord = configService.getDefaultPassWord();
        return AjaxResult.ok().msg("重置成功,新密码为：" + defaultPassWord).data("新密码：" + defaultPassWord);
    }


    @RequestMapping("options")
    public AjaxResult options(DropdownParam param) {
        String searchText = param.getSearchText();
        JpaQuery<SysUser> query = new JpaQuery<>();

        if (searchText != null) {
            query.like("name", "%" + searchText.trim() + "%");
        }

        // 权限过滤
        Collection<String> orgIds = LoginTool.getOrgPermissions();
        if (CollUtil.isNotEmpty(orgIds)) {
            query.addSubOr(q -> {
                q.in(SysUser.Fields.unitId, orgIds);
                q.in(SysUser.Fields.deptId, orgIds);
            });

        }

        Page<SysUser> page = sysUserService.findAllByRequest(query, PageRequest.of(0, 200));


        Map<String, SysOrg> dict = sysOrgService.dict();
        List<Option> options = Option.convertList(page.getContent(), BaseEntity::getId, t -> {
            if (t.getDeptId() != null) {
                SysOrg sysOrg = dict.get(t.getDeptId());
                if(sysOrg != null){
                    return t.getName() + " (" + sysOrg.getName() + ")";

                }
            }

            return t.getName();
        });


        return AjaxResult.ok().data(options);
    }


    /**
     * 拥有数据
     */
    @GetMapping("getPermInfo")
    public AjaxResult getPermInfo(String id) {
        GrantUserPermRequest permInfo = sysUserService.getPermInfo(id);
        return AjaxResult.ok().data(permInfo);
    }


    /**
     * 授权数据
     */
    @Log("用户-授权数据")
    @HasPermission("sysUser:grantPerm")
    @PostMapping("grantPerm")
    public AjaxResult grantPerm(@Valid @RequestBody GrantUserPermRequest param) {
        sysUserService.grantPerm(param.getId(), param.getRoleIds(), param.getDataPermType(), param.getOrgIds());
        return AjaxResult.ok();
    }

    /**
     * 用户树
     * 机构刷下面增加用户节点
     *
     * @return
     */
    @GetMapping("tree")
    public AjaxResult tree() {
        Subject subject = SecurityUtils.getSubject();
        List<SysOrg> orgList = sysOrgService.findByLoginUser(true, false);
        if (orgList.isEmpty()) {
            return AjaxResult.ok().data(Collections.emptyList());
        }

        Collection<String> orgPermissions = subject.getOrgPermissions();
        List<SysUser> userList = sysUserService.findByUnit(orgPermissions);

        List<TreeOption> tree = orgList.stream().map(o -> new TreeOption(o.getName(), o.getId(), o.getPid())).collect(Collectors.toList());

        List<TreeOption> userTree = userList.stream().map(u -> new TreeOption(u.getName(), u.getId(), StrUtil.emptyToDefault(u.getDeptId(), u.getUnitId()))).collect(Collectors.toList());

        tree.addAll(userTree);

        tree = TreeOption.convertTree(tree);

        return AjaxResult.ok().data(tree);
    }


    /**
     * 下拉表格
     *
     * @param param
     * @param pageable
     * @return
     */
    @RequestMapping("tableSelect")
    public AjaxResult tableSelect(DropdownParam param, Pageable pageable) {
        JpaQuery<SysUser> q = new JpaQuery<>();
        q.searchText(param.getSearchText(), SysUser.Fields.name, SysUser.Fields.account);

        List<String> selected = param.getSelected();
        if (CollUtil.isNotEmpty(selected)) {
            q.in("id", selected);
        }

        Page<SysUser> page = sysUserService.findAllByRequest(q, pageable);


        Table<SysUser> tb = new Table<>(page);
        tb.addColumn("标识", "id");
        tb.addColumn("账号", SysUser.Fields.account).setSorter(true);
        tb.addColumn("名称", SysUser.Fields.name).setSorter(true);


        return AjaxResult.ok().data(tb);
    }


}
