import React from "react";
import {PageUtil} from "@/framework";
import InstanceStatusInfo from "../../../components/InstanceStatusInfo";

export default class extends React.Component {


    render() {
        const {businessKey, id} = PageUtil.currentParams()
        return <InstanceStatusInfo businessKey={businessKey} id={id}/>
    }

}
