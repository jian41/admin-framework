import React from 'react';


export class ViewBoolean extends React.Component {


    render() {
        let {value} = this.props;
        if(value == null){
            return null;
        }

        return  value ? '是': '否'
    }
}
