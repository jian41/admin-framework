package io.admin.modules.flowable.core.definition;



import io.admin.common.utils.field.FieldDescription;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessDefinitionDescription {

    String key();
    String name();


    /**
     * 支持得表单， 不同人物可能选择不同得表单
     *
     * @return
     */
    FormKeyDescription[] formKeys() default {};

    FieldDescription[] conditionVars() default {};
}
