package io.admin.modules.flowable.core.assignment;

import java.util.Collection;
import java.util.List;

/**
 * 分配类型
 */
public interface AssignmentTypeProvider {


    String getCode();

    String getLabel();

    int getOrder();


    // 是否多选
    boolean isMultiple();


    String getLabelById(String id);


    /**
     * bpm xml 文件的属性
     *
     * @return
     */
    XmlAttribute getXmlAttribute();


    /**
     * 列表
     *
     * @return
     */
    Collection<Identity> findAll();


    /**
     * 获得用户所属分组 （如角色，分组等）， 用于运行时查询，
     * 例如 角色分 管理员， 如果你时管理员，就能看到待办
     */
    default List<String> findGroupsByUser(String userId) {
        return null;
    }




    /**
     * 设置审批者的标准属性，
     */
    enum XmlAttribute {
        assignee,
        candidateGroups,
        candidateUsers,
    }
}
