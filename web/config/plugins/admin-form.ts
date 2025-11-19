import {IApi} from 'umi';
import * as fs from "fs";
import * as path from "path";

// 自动注册src/forms下的表单
export default (api: IApi) => {
    api.logger.info('plugin starting... ')
    api.logger.info('info', JSON.stringify(api.env))

    api.describe({
        key: 'form-register-by-dir',
    });

    api.addEntryImports(() => ({
        source: path.join( api.paths.absSrcPath , 'framework/system/formRegistry'),
        specifier: 'formRegistry'
    }))

    const dir = path.join(api.paths.absSrcPath , 'forms')
    api.logger.info('scan dir is',dir)
    fs.readdirSync(dir).forEach(file => {
        if (!file.endsWith(".jsx")) {
            return;
        }
        let source = path.join(dir,file) ;
        let name = file.replace(".jsx", "");
        console.log('form info: ' , name,source)

        // import form
        api.addEntryImports(() => ({
            source: source,
            specifier: name
        }))

        // register form
        api.addEntryCodeAhead(() => `formRegistry.register("${name}",${name} );`)
    });

};