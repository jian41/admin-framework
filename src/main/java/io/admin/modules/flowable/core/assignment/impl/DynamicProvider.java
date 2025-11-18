package io.admin.modules.flowable.core.assignment.impl;

import io.admin.modules.flowable.core.assignment.AssignmentTypeProvider;
import io.admin.modules.flowable.core.assignment.Identity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.admin.modules.flowable.core.impl.FlowableManagerImpl.VAR_DEPT_LEADER;

@Component
public class DynamicProvider implements AssignmentTypeProvider {

    @Override
    public String getCode() {
        return "dynamic";
    }

    @Override
    public String getLabel() {
        return "动态办理人";
    }

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public boolean isMultiple() {
        return false;
    }



    @Override
    public XmlAttribute getXmlAttribute() {
        return XmlAttribute.assignee;
    }

    @Override
    public Collection<Identity> findAll() {
        List<Identity> list = new ArrayList<>();

        list.add(new Identity("${INITIATOR}",null, "发起人", true, true));
        list.add(new Identity("${"+ VAR_DEPT_LEADER +"}", null, "部门领导", true, true));


        return list;
    }

    @Override
    public String getLabelById(String id) {
        return "部门领导";
    }
}
