import {IApi} from 'umi';
import * as fs from "fs";
import * as path from "path";

// 自动注册src/forms下的表单
const pkgName = '@jiangood/springboot-admin-starter';
export default (api: IApi) => {

    api.describe({
        key: 'addDependenceRoutes',
    });

    const pageDir = api.paths.absNodeModulesPath + "/" + pkgName + "/src/pages"
    api.logger.info('scan dir is', pageDir);
    const exist = fs.existsSync(pageDir)
    if(!exist){
        api.logger.info('dir not exist, return ')
        return
    }

    const frameworkRoutes = []
    parseDir(pageDir, frameworkRoutes)
    api.modifyRoutes((routes) => {
        // routes 代表用户项目的路由，umi已经自动解析过pages目录
        // 接下来加入加入框架的路由，确保用户项目路由优先级高
        for (let route of frameworkRoutes) {
            if (routes[route.id] == null) { //  如果用户项目没有这个路由，则加入。否则以用户项目为准
                routes[route.id] = route
            }
        }
        return routes;
    })
};

function parseDir(pageDir, fileRoutes) {
    const list = fs.readdirSync(pageDir)

    for (let fileName of list) {
        const fullPath = path.join(pageDir, fileName)
        const stats = fs.statSync(fullPath)
        if (stats.isFile()) {
            if (fileName.endsWith(".jsx")) {
                addRoute(fullPath, fileRoutes)
            }
        } else if (stats.isDirectory()) {
            parseDir(fullPath, fileRoutes)
        }

    }
}

function addRoute(file, fileRoutes) {
    let routePath = file.substring(file.indexOf('pages') + 6, file.length - 4)
    routePath = routePath.replaceAll('\\', '/')

    let parentId = "@@/global-layout";

    // 文件$开头的会替换为路径变量 如$id 变为 :id
    routePath = routePath.replaceAll("\$", ":")

    fileRoutes.push({
        absPath: file,
        id: routePath,
        path: routePath,
        file,
        parentId
    })
    if (routePath.endsWith("/index")) {
        routePath = routePath.substring(0, routePath.length - 6)
        fileRoutes.push({
            absPath: file,
            id: routePath,
            path: routePath,
            file,
            parentId: parentId
        })
    }
}
