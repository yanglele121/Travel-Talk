import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueWaterfallEasy from 'vue-waterfall-easy'

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

import {formatTime,formatDate} from "./utils/time";
import { formatStarCounts } from './utils/data'
import { formatcity } from './utils/city'
import VueDirectiveImagePreviewer from 'vue-directive-image-previewer'
import 'vue-directive-image-previewer/dist/assets/style.css'


Vue.use(Element)
Vue.use(VueQuillEditor);
Vue.use(VueWaterfallEasy)
// Vue.use(Region)
Vue.use(VueDirectiveImagePreviewer)
Vue.config.productionTip = false

// 格式话时间
Vue.filter('formatTime', formatTime)
Vue.filter('formatDate',formatDate)
Vue.filter('formatStarCounts',formatStarCounts)
Vue.filter('formatcity',formatcity)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')



