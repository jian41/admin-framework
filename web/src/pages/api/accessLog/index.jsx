import React from 'react'
import {HttpUtil, Page, ProTable} from "@/framework";


export default class extends React.Component {



  columns = [

    {
      title: '接口名称',
      dataIndex: 'name',




    },

    {
      title: '接口',
      dataIndex: 'action',




    },

      {
          title: 'requestId',
          dataIndex: 'requestId',

      },

    {
      title: '请求数据',
      dataIndex: 'requestData',




    },

    {
      title: '响应数据',
      dataIndex: 'responseData',




    },

    {
      title: 'ip',
      dataIndex: 'ip',




    },

    {
      title: 'ipLocation',
      dataIndex: 'ipLocation',




    },

    {
      title: '执行时间',
      dataIndex: 'executionTime',




    },

    {
      title: '接口账户',
      dataIndex: 'accountName',




    },


  ]



  render() {
    return <Page>
      <ProTable
          request={(params) => HttpUtil.pageData('admin/apiAccessLog/page', params)}
          columns={this.columns}
      />


    </Page>


  }
}


