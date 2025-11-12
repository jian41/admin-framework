package io.admin.modules.api.service;

import io.admin.framework.data.query.JpaQuery;
import io.admin.modules.api.entity.ApiAccount;
import io.admin.modules.api.entity.ApiAccountResource;
import io.admin.framework.data.service.BaseService;
import io.admin.modules.api.entity.ApiResource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiAccountResourceService extends BaseService<ApiAccountResource> {

    public List<ApiResource> findByAccount(ApiAccount acc) {
        JpaQuery<ApiAccountResource> q = new JpaQuery<>();
        q.eq(ApiAccountResource.Fields.account, acc);

        List<ApiAccountResource> list = baseDao.findAll(q);


        List<ApiResource> resourceList = list.stream().map(ApiAccountResource::getResource)
                .sorted(Comparator.comparing(ApiResource::getAction))
                .collect(Collectors.toList());

        return resourceList;
    }

    public ApiAccountResource findByAccountAndAction(ApiAccount account, String action) {
        JpaQuery<ApiAccountResource> q = new JpaQuery<>();
        q.eq(ApiAccountResource.Fields.account, account);
        q.eq(ApiAccountResource.Fields.resource + "." + ApiResource.Fields.action, action);

        return baseDao.findOne(q);
    }


    public void deleteByResource(ApiResource r) {
        JpaQuery<ApiAccountResource> q = new JpaQuery<>();
        q.eq(ApiAccountResource.Fields.resource, r);

        ApiAccountResource old = baseDao.findOne(q);

        if(old != null){
            baseDao.delete(old);
        }

    }
}

