package io.admin.modules.flowable.core;


import lombok.Data;

@Data
public class FlowableLoginUser {
    String id;
    String name;

    String unitId;
    String unitName;
    String deptId;
    String deptName;

    String deptLeaderId;


    boolean superAdmin;
}
