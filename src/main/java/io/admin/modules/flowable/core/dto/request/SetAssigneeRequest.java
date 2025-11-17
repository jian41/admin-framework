package io.admin.modules.flowable.core.dto.request;

import lombok.Data;

@Data
public class SetAssigneeRequest {
    String taskId;
    String assignee;
}
