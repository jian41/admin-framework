package io.admin.modules.flowable.demo;

import io.admin.modules.flowable.core.FlowableEventType;
import io.admin.modules.flowable.core.definition.FormKeyDescription;
import io.admin.modules.flowable.core.definition.ProcessDefinition;
import io.admin.modules.flowable.core.definition.ProcessDefinitionDescription;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ProcessDefinitionDescription(key = "demo",name = "demo-派车流程", formKeys = @FormKeyDescription(value = "driverForm",label = "司机表单"))
public class DemoProcess implements ProcessDefinition {

    @Override
    public void onProcessEvent(FlowableEventType type, String initiator, String businessKey, Map<String, Object> variables) {

    }
}
