import React from 'react';
import {FieldUploadFile} from "../FieldUploadFile";


export class FieldUploadCropImage extends React.Component {
  render() {
      return <FieldUploadFile {...this.props} accept={"image/*"} cropImage={true} />
  }
}


