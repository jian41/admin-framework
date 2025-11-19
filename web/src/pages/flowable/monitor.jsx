import React from "react";
import {Button, Form, Modal, Tabs} from "antd";
import AllDefinition from "../../components/monitor/AllDefinition";
import AllInstance from "../../components/monitor/AllInstance";
import {FieldUserSelect, HttpUtil, Page, ProTable} from "@/framework";

export default class extends React.Component {

    state = {
        assigneeFormOpen:false,
        assigneeFormValues:{}
    }

    assigneeFormRef = React.createRef()
    taskTableRef = React.createRef()
    render() {
        const items = [
            {label: '运行中的任务', key: 'AllTask', children: this.renderTask()},
            {label: '运行中的流程', key: 'AllInstance', children: <AllInstance/>},

            {label: '所有定义', key: 'AllDefinition', children: <AllDefinition/>},
        ];
        return <Page padding> <Tabs items={items} destroyOnHidden /></Page>

    }

    renderTask = () => {
        return<> <ProTable
            actionRef={this.taskTableRef}
            columns={[
                {
                    dataIndex: 'id',
                    title: '任务标识',
                },
                {
                    dataIndex: 'name',
                    title: '名称',
                },

                {
                    dataIndex: 'processDefinitionId',
                    title: '定义'
                },
                {
                    dataIndex: 'processInstanceId',
                    title: '实例'
                },

                {
                    dataIndex: 'assignee',
                    title: '处理人'
                },
                {
                    dataIndex: 'assigneeLabel',
                    title: '处理人显示名'
                },
                {
                    dataIndex: 'startTime',
                    title: '开始时间'
                },
                {
                    dataIndex: 'tenantId',
                    title: '租户'
                },

                {
                    dataIndex:'id',
                    render:(id)=>{
                        return <Button size='small' onClick={()=>this.onClickSetAssignee(id)}>指定处理人</Button>
                    }
                }
            ]}
            request={(params) => HttpUtil.pageData('admin/flowable/monitor/task', params)}
        >
            <Form.Item label='受理人' name='assignee'>
                <FieldUserSelect />
            </Form.Item>
        </ProTable>
        <Modal title='指定处理人'
               open={this.state.assigneeFormOpen}
               onOk={()=>this.assigneeFormRef.current.submit()}
               onCancel={()=>this.setState({assigneeFormOpen:false})}
               destroyOnHidden
        >
                <Form ref={this.assigneeFormRef} onFinish={this.submitSetAssignee} initialValues={this.state.assigneeFormValues}>
                    <Form.Item name='taskId' noStyle>
                    </Form.Item>
                    <Form.Item name='assignee' label='用户'>
                        <FieldUserSelect />
                    </Form.Item>
                </Form>
        </Modal>
        </>
    };

    onClickSetAssignee = id => {
        this.setState({assigneeFormOpen:true,assigneeFormValues:{taskId:id}})
    };

    submitSetAssignee = values => {
        HttpUtil.post('admin/flowable/monitor/setAssignee',values).then(()=>{
            this.setState({assigneeFormOpen:false})
            this.taskTableRef.current.reload()
        })
    };
}
