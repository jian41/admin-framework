import { IApi } from 'umi';
import * as fs from "fs";

// 自动注册src/forms下的表单
export default (api: IApi) => {
    console.log('form-register-by-dir running... ')
    api.describe({
        key: 'form-register-by-dir',
    });
    const dir = api.cwd + "/src/forms"
    fs.readdirSync(dir).forEach(file => {
        if (file.endsWith(".jsx")) {
           console.log(file)
            api.addEntryImports(() => ({
                source: '/modulePath/xxx.js',
                specifier: 'moduleName'
            }))
        }
    });

    api.addEntryCode(() => `console.log('I am after render!')`);
};