package io.admin.modules.flowable.core.assignment.impl;


import org.springframework.stereotype.Component;

@Component
public class AssigneeUserProvider extends BaseUserProvider  {



    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public boolean isMultiple() {
        return false;
    }


    @Override
    public String getCode() {
        return "AssigneeUser";
    }

    @Override
    public String getLabel() {
        return "办理人";
    }




    @Override
    public XmlAttribute getXmlAttribute() {
        return XmlAttribute.assignee;
    }


}
