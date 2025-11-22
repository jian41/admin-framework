import {
    FieldDateTimePickerString,
    FieldDictRadio,
    FieldImageBase64,
    FieldInput,
    FieldPassword,
    FieldRadioBoolean,
    FieldUploadImage
} from "../field";
import {ViewImage,  ViewText} from "../view";
import {ViewPassword,ViewBoolean} from "../../view-components";
import {FieldRemoteSelect} from "../../field-components";


export const fieldRegistry = {
    'text':FieldInput,

    'dict':FieldDictRadio,
    'dictRadio':FieldDictRadio,
    'password':FieldPassword,
    'datetime':FieldDateTimePickerString,
    'boolean':FieldRadioBoolean,
    imageBase64:FieldImageBase64,
    image:FieldUploadImage,

    select: FieldRemoteSelect
}

export const viewRegistry = {
    text: ViewText,
    password: ViewPassword,
    boolean: ViewBoolean,
    imageBase64: ViewImage,
    image:ViewImage
}




