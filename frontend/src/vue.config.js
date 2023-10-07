// vue.config.js
const webpack=require("webpack");
module.exports = {
    pluginOptions: new webpack.ProvidePlugin({
        jQuery: "jquery",
        $: "jquery"
    }),
    lintOnSave: false,
}

// module.exports = {
//     devServer: {
//         // Paths
//         host: '127.0.0.1',
//         port: 8081,
//         https: false,
//         open: true,
//         proxy: {
//           '/api': { // 匹配所有以 '/api'开头的请求路径
//             target: 'http://localhost:8080', // 代理目标的基础路径
//             ws: true,
//             changeOrigin: true, // 支持跨域
//             pathRewrite: {// 重写路径: 去掉路径中开头的'/api'
//               '^/api': ''
//             }
//           }
//         },
//     }
// }

