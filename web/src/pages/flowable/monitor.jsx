import React from "react";
import {Tabs} from "antd";
import AllDefinition from "../../components/monitor/AllDefinition";
import AllInstance from "../../components/monitor/AllInstance";
import {HttpUtil, Page, ProTable} from "@/framework";

export default class extends React.Component {

    render() {
        const items = [
            {label: '运行中的流程', key: 'AllInstance', children: <AllInstance/>},
            {label: '运行中的任务', key: 'AllTask', children: this.renderTask()},
            {label: '所有定义', key: 'AllDefinition', children: <AllDefinition/>},
        ];
        return <Page padding> <Tabs items={items} destroyOnHidden/></Page>

    }

    renderTask = () => {
        console.log('渲染流程')
        return <ProTable
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
                    dataIndex: 'startTime',
                    title: '开始时间'
                },

                {
                    dataIndex: 'tenantId',
                    title: '租户标识'
                },
            ]}
            request={(params) => HttpUtil.pageData('admin/flowable/monitor/task', params)}
        >
        </ProTable>;
    };
}
