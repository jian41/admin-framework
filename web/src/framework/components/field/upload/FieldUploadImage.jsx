import React from 'react';
import {FieldUploadFile} from "./FieldUploadFile";


/**
 * crop: 带裁切
 */

export class FieldUploadImage extends React.Component {

  render() {
    return <FieldUploadFile {...this.props} accept={'image/*'} />
  }
}


