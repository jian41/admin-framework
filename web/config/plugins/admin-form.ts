import {IApi} from 'umi';
import * as fs from "fs";

// 自动注册src/forms下的表单
export default (api: IApi) => {
    console.log('form-register-by-dir running... ')
    api.describe({
        key: 'form-register-by-dir',
    });
    api.addEntryImports(() => ({
        source: api.cwd + '/src/framework/system/formRegistry',
        specifier: 'formRegistry'
    }))

    const dir = api.cwd + "/src/forms"
    fs.readdirSync(dir).forEach(file => {
        if (file.endsWith(".jsx")) {
            let source = api.cwd + '/src/forms/' + file;
            console.log('form source path is ' + source)
            let name = file.replace(".jsx", "");
            api.addEntryImports(() => ({
                source: source,
                specifier: name
            }))
            api.addEntryCodeAhead(() => `formRegistry.register("${name}",${name} );`)
        }
    });

};