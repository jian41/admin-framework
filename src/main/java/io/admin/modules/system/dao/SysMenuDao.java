
package io.admin.modules.system.dao;


import io.admin.framework.config.data.ConfigDataDao;
import io.admin.framework.config.data.sysmenu.MenuDefinition;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;


@Slf4j
@Repository
public class SysMenuDao {

    @jakarta.annotation.Resource
    private ConfigDataDao configDataDao;


    /**
     * 扁平的列表
     *
     * @return
     */
    @SneakyThrows
    public List<MenuDefinition> findAll() {
        return configDataDao.getMenus();
    }





    public List<MenuDefinition> findAllById(List<String> ids) {
        List<MenuDefinition> list = this.findAll();
        return list.stream().filter(t->ids.contains(t.getId())).toList();
    }
}
