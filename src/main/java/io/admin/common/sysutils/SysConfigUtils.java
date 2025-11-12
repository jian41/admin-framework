package io.admin.common.sysutils;

import io.admin.common.utils.SpringTool;
import io.admin.modules.system.dao.SysConfigDao;
import io.admin.modules.system.entity.SysConfig;

public class SysConfigUtils {

    public static String get(String id){
        SysConfig cfg = dao().findOne(id);
        if(cfg == null){
            return null;
        }
        return cfg.getValue();
    }

    public static void set(String id){
        SysConfig cfg = dao().findOne(id);
        if(cfg == null){

        }
    }

    private static  SysConfigDao DAO;
    private static SysConfigDao dao(){
        if(DAO == null){
            DAO = SpringTool.getBean(SysConfigDao.class);
        }
        return DAO;
    }


}
