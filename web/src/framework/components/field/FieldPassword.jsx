import React from "react";
import {Input} from "antd";

export  class FieldPassword extends React.Component {


    render() {
        return <Input.Password {...this.props}/>
    }

}
