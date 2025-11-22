import React from 'react';
import { SelectProps } from 'antd/es/select';

export interface FieldRemoteSelectMultipleProps extends Omit<SelectProps, 'options' | 'children'| 'mode'> {

    /**
     * 请求地址
     */
    url: string ;
}

export class FieldRemoteSelectMultiple extends React.Component<FieldRemoteSelectMultipleProps, any> {}

