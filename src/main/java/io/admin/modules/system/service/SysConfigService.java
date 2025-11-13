
package io.admin.modules.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.admin.common.utils.RequestTool;
import io.admin.common.utils.tree.TreeTool;
import io.admin.framework.config.SysProp;
import io.admin.framework.config.data.ConfigDataDao;
import io.admin.framework.config.data.sysconfig.ConfigDefinition;
import io.admin.framework.config.data.sysconfig.ConfigGroupDefinition;
import io.admin.framework.data.query.JpaQuery;
import io.admin.modules.system.dao.SysConfigDao;
import io.admin.modules.system.dto.response.SysConfigResponse;
import io.admin.modules.system.entity.SysConfig;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class SysConfigService {

    @Resource
    private SysConfigDao sysConfigDao;

    @Resource
    private ConfigDataDao dataProp;

    @Resource
    private Environment env;

    @Resource
    private SysProp sysProp;


    /***
     * 最终的配置， 先取数据库，数据库不存在则取env
     * @return
     */
    public <T> T getMixed(String key, Class<T> type) {
        String value = env.getProperty(key);
        String sysKeyPrefix = "sys.";
        if (StrUtil.isEmpty(value) && key.startsWith(sysKeyPrefix)) {
            String sysKey = key.substring(sysKeyPrefix.length()); // 去掉前缀
            Object fieldValue = BeanUtil.getFieldValue(sysProp, sysKey);
            if (fieldValue != null) {
                value = Convert.convert(String.class,fieldValue);
            }
        }

        SysConfig sysConfig = sysConfigDao.findByCode(key);
        if (sysConfig != null) {
            String dbValue = sysConfig.getValue();
            if (dbValue != null) {
                value = dbValue;
            }
        }
        if (StrUtil.isEmpty(value)) {
            return null;
        }

        return Convert.convert(type, value);
    }


    public String getBaseUrl() {
        String url = this.get("sys.baseUrl");
        if (StrUtil.isEmpty(url)) {
            url = RequestTool.getBaseUrl(RequestTool.currentRequest());
        }
        return url;
    }


    public boolean getBoolean(String key) {
        Object value = this.get(key);
        return Convert.toBool(value);
    }

    public int getInt(String key) {
        Object value = this.get(key);
        return Convert.toInt(value);
    }


    public String get(String key) {
        SysConfig sysConfig = sysConfigDao.findByCode(key);
        if (sysConfig == null) {
            return null;
        }
        return sysConfig.getValue();
    }


    /**
     * 获取默认密码
     */
    public String getDefaultPassWord() {
        return get("sys.default.password");
    }


    public Map<String, Object> siteInfo() {
        Map<String, Object> map = this.findByPrefix("sys.siteInfo");
        return map;
    }


    /**
     * 通过前缀查询键值对
     *
     * @param prefix
     * @return
     */
    public Map<String, Object> findByPrefix(String prefix) {
        if (!prefix.endsWith(".")) {
            prefix = prefix + ".";
        }
        JpaQuery<SysConfig> q = new JpaQuery<>();
        q.like("id", prefix + "%");
        List<SysConfig> list = sysConfigDao.findAll(q);

        Map<String, Object> map = new HashMap<>();
        for (SysConfig sysConfig : list) {
            String k = sysConfig.getId();
            k = k.replace(prefix, "");
            Object v = sysConfig.getValue();
            map.put(k, v);
        }

        return map;
    }


    @Transactional
    public void save(SysConfig cfg) {
        SysConfig db = sysConfigDao.findOne(cfg.getId());
        db.setValue(cfg.getValue());

    }

    public List<SysConfigResponse> findAllByRequest(String searchText) {
        List<SysConfigResponse> responseList = new ArrayList<>();

        List<SysConfig> configList = sysConfigDao.findAll();
        for (ConfigGroupDefinition config : dataProp.getConfigs()) {
            SysConfigResponse response = new SysConfigResponse();
            response.setId(config.getGroupName());
            response.setName(config.getGroupName());

            for (ConfigDefinition child : config.getChildren()) {
                if (StrUtil.isNotEmpty(searchText) && !StrUtil.containsAll(searchText, child.getName(), child.getDescription(), child.getCode())) {
                    continue;
                }
                SysConfigResponse r = new SysConfigResponse();
                r.setName(child.getName());
                r.setDescription(child.getDescription());
                r.setCode(child.getCode());
                response.getChildren().add(r);

                for (SysConfig c : configList) {
                    if (c.getCode().equals(child.getCode())) {
                        r.setId(c.getId());
                        r.setValue(c.getValue());
                        r.setUpdateTime(c.getUpdateTime());
                        break;
                    }
                }
            }
            responseList.add(response);
        }


        TreeTool.cleanEmptyChildren(responseList, SysConfigResponse::getChildren, SysConfigResponse::setChildren);

        return responseList;
    }


}
