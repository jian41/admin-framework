package io.admin.modules.api.controller;

import cn.hutool.core.collection.CollUtil;
import io.admin.modules.api.service.ApiResourceService;
import io.admin.common.dto.table.Table;
import io.admin.framework.persistence.BaseController;
import io.admin.framework.data.query.JpaQuery;
import io.admin.common.dto.AjaxResult;
import io.admin.modules.api.entity.ApiResource;
import io.admin.common.dto.DropdownRequest;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/apiResource")
public class ApiResourceController extends BaseController<ApiResource> {
    @Resource
    private ApiResourceService service;



    @RequestMapping("tableSelect")
    public AjaxResult tableSelect(DropdownRequest dropdownRequest, @PageableDefault(sort = "id") Pageable pageable) {
        JpaQuery<ApiResource> q = new JpaQuery<>();
        q.searchText(dropdownRequest.getSearchText(), ApiResource.Fields.name, ApiResource.Fields.action, ApiResource.Fields.desc);

        List<String> selected = dropdownRequest.getSelected();
        if(CollUtil.isNotEmpty(selected)){
          q.in("id", selected);
        }

        Page<ApiResource> page = service.findAllByRequest(q,pageable);

        Table<ApiResource> tb = new Table<>(page);
        tb.addColumn("标识", "id");
        tb.addColumn("名称", ApiResource.Fields.name).setSorter(true);
        tb.addColumn("动作", ApiResource.Fields.action).setSorter(true);
        tb.addColumn("描述", ApiResource.Fields.desc);

        return AjaxResult.ok().data(tb);
    }
}
