package io.admin.modules.flowable.core.dto.request;

import io.admin.modules.flowable.core.dto.TaskHandleResult;
import lombok.Data;

@Data
public class HandleTaskRequest {

    TaskHandleResult result;
    String taskId;
    String comment;
}
