import React from 'react';
import { SelectProps } from 'antd/es/select';

export interface FieldRemoteSelectProps extends Omit<SelectProps, 'options' | 'children'|'mode'> {

    /**
     * 请求地址
     */
    url: string ;
}

export class FieldRemoteSelect extends React.Component<FieldRemoteSelectProps, any> {}

