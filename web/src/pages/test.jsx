import React from "react";
import {Card} from "antd";
import {formRegistry} from "@/framework";

export default class extends React.Component {


    render() {
        let allKeys = formRegistry.getAllKeys();
        console.log('所有表单：', allKeys)

        return <Card title='测试页面'>




        </Card>
    }
}
