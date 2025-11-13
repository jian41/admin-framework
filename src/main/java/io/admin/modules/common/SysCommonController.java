
package io.admin.modules.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import io.admin.common.utils.field.ValueType;
import io.admin.modules.common.dto.response.LoginDataResponse;
import io.admin.modules.common.dto.response.LoginInfoResponse;
import io.admin.modules.system.ConfigConsts;
import io.admin.modules.system.dto.mapper.MenuMapper;
import io.admin.modules.system.dto.response.MenuResponse;
import io.admin.modules.system.entity.SysRole;
import io.admin.modules.system.entity.SysUser;
import io.admin.modules.system.service.*;
import io.admin.framework.config.SysProp;
import io.admin.framework.config.data.sysmenu.MenuDefinition;
import io.admin.common.dto.AjaxResult;
import io.admin.common.utils.tree.TreeTool;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("admin")
public class SysCommonController {

    @Resource
    SysRoleService roleService;


    @Resource
    SysConfigService sysConfigService;

    @Resource
    SysMenuBadgeService sysMenuBadgeService;

    @Resource
    SysFileService sysFileService;

    @Resource
    SysProp sysProp;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private SysUserMessageService sysUserMessageService;

    /**
     * 站点信息， 非登录情况下使用
     */
    @GetMapping("public/site-info")
    public AjaxResult siteInfo() {
        Dict data = new Dict();
        data.put("captcha",sysConfigService.getMixed("sys.captcha", Boolean.class));
        data.put("code",sysConfigService.getMixed("sys.captchaType",String.class));
        data.put("copyright",sysConfigService.getMixed("sys.copyright",String.class));
        data.put("loginBoxBottomTip",sysConfigService.getMixed("sys.loginBoxBottomTip",String.class));
        data.put("logo",sysConfigService.getMixed("sys.logo",String.class));
        data.put("title",sysConfigService.getMixed("sys.title",String.class));
        data.put("waterMark",sysConfigService.getMixed("sys.waterMark",Boolean.class));

        // 将公钥发给前端，用于前端加密
        String publicKey = sysConfigService.getMixed(ConfigConsts.RSA_PUBLIC_KEY,String.class);
        Assert.notNull(publicKey, "服务未初始化密钥信息，无法登录");
        data.put("rsaPublicKey", publicKey);

        // 登录背景图
        String bg = sysConfigService.getMixed("sys.loginBackground",String.class);
        if(bg != null && sysFileService.isFileExist(bg)){
            data.put("loginBackground",bg);
        }

        return AjaxResult.ok().data(data);
    }

    /**
     * 检查是否登录
     * 检查是否需要修改密码
     */
    @GetMapping("public/checkLogin")
    public AjaxResult checkLogin(HttpServletRequest request) {
        LoginDataResponse r = new LoginDataResponse();

        HttpSession session = request.getSession(false);
        if (session == null) {
            log.debug("checkLogin session is null");
            return AjaxResult.err("未登录");
        }

        SysUser user = LoginTool.getLoginUser();
        boolean login = user != null;
        if (!login){
            return AjaxResult.err("未登录");
        }
        r.setLogin(true);
        r.setNeedUpdatePwd(false); // TODO
        r.setDictTree(sysDictService.tree());


        List<String> permissions = LoginTool.getPermissions();
        List<String> roles = LoginTool.getRoles();
        List<SysRole> roleList = roleService.findAllByCode(roles);
        String roleNames = roleList.stream().map(SysRole::getName).collect(Collectors.joining(","));

        LoginInfoResponse userResponse = new LoginInfoResponse();
        userResponse.setId( user.getId());
        userResponse.setName(user.getName());
        userResponse.setOrgName(sysOrgService.getNameById(user.getUnitId()));
        userResponse.setDeptName(sysOrgService.getNameById(user.getDeptId()));
        userResponse.setPermissions(permissions);
        userResponse.setAccount(user.getAccount());
        userResponse.setRoleNames(roleNames);
        userResponse.setMessageCount(sysUserMessageService.countUnReadByUser(user.getId()));
        r.setLoginInfo(userResponse);

        return AjaxResult.ok().data(r);
    }




    /**
     * 前端左侧菜单调用， 以展示顶部及左侧菜单
     */
    @GetMapping("menuInfo")
    public AjaxResult menuInfo() {
        String account = LoginTool.getLoginAccount();

        SysUser user = sysUserService.findByAccount(account);
        Set<SysRole> roles = user.getRoles();

        List<MenuDefinition> menuList = roleService.ownMenu(roles);

        List<MenuResponse> menuResponseList = menuMapper.menuToResponseList(menuList);
        List<MenuResponse> tree = TreeTool.buildTree(menuResponseList, MenuResponse::getId, MenuResponse::getPid, MenuResponse::setChildren);

        // 顶层菜单如果没有子节点，则移除
        tree.removeIf(t -> CollUtil.isEmpty(t.getChildren()));

        // 设置每个菜单的最顶层父节点id，方便刷新时切换顶层节点
        TreeTool.walk(tree, MenuResponse::getChildren, (menu, parentMenu) -> {
            String rootid = parentMenu == null ? menu.getId() : parentMenu.getRootid();
            menu.setRootid(rootid);
        });

        Dict data = new Dict();

        List<Dict> topMenus = tree.stream().map(r -> Dict.of("key", r.getKey(), "label", r.getLabel())).toList();
        data.put("topMenus", topMenus);
        data.put("menus", tree);
        data.put("badgeList", sysMenuBadgeService.findAll());

        return AjaxResult.ok().data(data);
    }


    @Resource
    private SysDictService sysDictService;


}
