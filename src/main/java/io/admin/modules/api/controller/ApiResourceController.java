package io.admin.modules.api.controller;

import cn.hutool.core.collection.CollUtil;
import io.admin.framework.config.argument.RequestBodyKeys;
import io.admin.framework.config.security.HasPermission;
import io.admin.modules.api.service.ApiResourceService;
import io.admin.common.dto.table.Table;
import io.admin.framework.data.query.JpaQuery;
import io.admin.common.dto.AjaxResult;
import io.admin.modules.api.entity.ApiResource;
import io.admin.common.dto.DropdownRequest;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/apiResource")
public class ApiResourceController  {
    @Resource
    private ApiResourceService service;

    @HasPermission("apiResource:view")
    @RequestMapping("page")
    public AjaxResult page(ApiResource request,@PageableDefault(direction = Sort.Direction.DESC, sort = "updateTime") Pageable pageable) throws Exception {
        JpaQuery<ApiResource> q = new JpaQuery<>();
        // 视情况修改
        q.likeExample(request);
        Page<ApiResource> page = service.pageByRequest(q, pageable);
        return AjaxResult.ok().data(page);
    }

    @HasPermission("apiResource:save")
    @PostMapping("save")
    public AjaxResult save(@RequestBody ApiResource input, RequestBodyKeys updateFields) throws Exception {
        service.saveOrUpdateByRequest(input, updateFields);
        return AjaxResult.ok().msg("保存成功");
    }

    @HasPermission("apiResource:delete")
    @RequestMapping("delete")
    public AjaxResult delete(String id) {
        service.deleteByRequest(id);
        return AjaxResult.ok().msg("删除成功");
    }

    @RequestMapping("tableSelect")
    public AjaxResult tableSelect(DropdownRequest dropdownRequest, @PageableDefault(sort = "id") Pageable pageable) {
        JpaQuery<ApiResource> q = new JpaQuery<>();
        q.searchText(dropdownRequest.getSearchText(), ApiResource.Fields.name, ApiResource.Fields.action, ApiResource.Fields.desc);

        List<String> selected = dropdownRequest.getSelected();
        if(CollUtil.isNotEmpty(selected)){
          q.in("id", selected);
        }

        Page<ApiResource> page = service.pageByRequest(q,pageable);

        Table<ApiResource> tb = new Table<>(page);
        tb.addColumn("标识", "id");
        tb.addColumn("名称", ApiResource.Fields.name).setSorter(true);
        tb.addColumn("动作", ApiResource.Fields.action).setSorter(true);
        tb.addColumn("描述", ApiResource.Fields.desc);

        return AjaxResult.ok().data(tb);
    }
}
