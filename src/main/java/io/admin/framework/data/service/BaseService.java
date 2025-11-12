package io.admin.framework.data.service;


import io.admin.framework.data.domain.PersistEntity;
import io.admin.framework.data.query.JpaQuery;
import io.admin.framework.data.repository.BaseDao;
import io.admin.common.dto.Option;
import jakarta.persistence.Transient;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseService<T extends PersistEntity> {


    @Autowired
    protected BaseDao<T> baseDao;

    /**
     * 更新时，指定字段更新
     * 防止了全字段更新，以免有些字段非前端输入的情况
     */
    @Transactional
    public T saveOrUpdateByRequest(T input, List<String> updateKeys) throws Exception {
        String id = input.getId();
        if (id == null) {
            return baseDao.persist(input);
        }

        baseDao.updateField(input, updateKeys);
        return baseDao.findById(id);
    }


    @Transactional
    public void deleteByRequest(String id) {
        baseDao.deleteById(id);
    }

    public Page<T> findAllByRequest(JpaQuery<T> q, Pageable pageable) {
        return baseDao.findAll(q, pageable);
    }

    public T findOneByRequest(String id) {
        return baseDao.findOne(id);
    }


}
