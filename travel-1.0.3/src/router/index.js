import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/frontend/Home.vue'

Vue.use(VueRouter)

const routes = [{
  path: '/',
  name: 'Home',
  component: Home,
  meta:{
    title:"行旅谈",
    keepAlive: true
  }
},
{
  path: '/search',
  name: 'search',
  component: () => import('../views/frontend/Search.vue')
},
{
  path: '/about',
  name: 'About',
  component: () => import('../views/frontend/About.vue')
},
{
  path: '/login',
  name: 'Login',
  component: () => import('../views/frontend/Login.vue')
},
{
  path: '/personal/:id',
  name: 'Personal',
  component: () => import('../views/person/PersonalCenter.vue'),
  redirect:'/personal/footprint/:id',
  children: [
    {
      path: '/personal/footprint/:id',
      name:'footprint',
      component: r => require.ensure([], () => r(require('@/views/person/Footprint')), 'footprint')
    },
    {
      path: '/personal/mymessage/:id',
      name:'mymessage',
      component: r => require.ensure([], () => r(require('@/views/person/MyMessage')), 'mymessage')
    },
    {
      path:'/personal/myarticle/:id',
      name:'myarticle',
      component: r => require.ensure([], () => r(require('@/views/person/MyArticle')), 'myarticle')
    },
    {
      path:'/personal/mycollect/:id',
      name:'mycollect',
      component: r => require.ensure([], () => r(require('@/views/person/MyCollect')), 'mycollect')
    },
    {
      path:'/personal/myfan/:id',
      name:'myfan',
      component: r => require.ensure([], () => r(require('@/views/person/MyFanAndFollow')), 'myfan')
    },
    {
      path:'/personal/myfollow/:id',
      name:'myfollow',
      component: r => require.ensure([], () => r(require('@/views/person/MyFanAndFollow')), 'myfollow')
    }
  ]
},
{
  path: '/site',
  name: 'Site',
  component: () => import('../views/frontend/Site.vue'),
  props: route => ({ siteId: route.query.siteId }),
},
{
  path:'/article/:id',
  name:'Article',
  component: ()=> import('../views/frontend/Articles.vue')
},

{
  path:'/search',
  name:'Search',
  component: ()=> import('../views/frontend/Search.vue')
},

{
  path: '/flow',
  name: 'Flow',
  component: () => import('../views/frontend/Flow.vue')
},
{
  path: '/admin/login',
  name: 'AdminLogin',
  component: () => import('../views/backend/AdminLogin.vue')
},
{
  path: '/admin/dashboard',
  name: 'InfoPage',
  component: () => import('../views/backend/InfoPage.vue'),
  meta: {
    requireAuth: true,
  }
},
{
  path: '/admin/announcements',
  name: 'AnnouncementPage',
  component: () => import('../views/backend/Announcement.vue'),
  meta: {
    requireAuth: true,
  }
},
{
  path: '/admin/consumers',
  name: 'ConsumerPage',
  component: () => import('../views/backend/ConsumerPage.vue'),
  meta: {
    requireAuth: true,
  }
},
{
  path: '/admin/sites',
  name: 'SitePage',
  component: () => import('../views/backend/SitePage.vue'),
  meta: {
    requireAuth: true,
  }
},
{
  path: '/admin/tags',
  name: 'TagsPage',
  component: () => import('../views/backend/TagsPage.vue'),
  meta: {
    requireAuth: true,
  }
},
{
  path: '/admin/articles',
  name: 'ArticlePage',
  component: () => import('../views/backend/ArticlePage.vue'),
  meta: {
    requireAuth: true,
  }
},
{
  path: '/admin/comments',
  name: 'CommentPage',
  component: () => import('../views/backend/CommentPage.vue'),
  meta: {
    requireAuth: true,
  }
},
{
  path:'/createCenter',
  name:'CreateCenter',
  component:()=> import("../views/frontend/CreateCenter.vue")
},
{
  path: '/im',
  name: 'Im',
  component: () => import('../views/frontend/Im.vue')
},


//404页面
{
  path: '/404',
  name: 'NotFound',
  component: ()=>import ("../views/error/404.vue")
}
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 在Vue Router中注册beforeEach导航守卫
router.beforeEach((to, from, next) => {
  // 根据to.meta.title的定义更新文档标题
  document.title = to.meta.title || '行旅谈';
  next();
});
router.beforeEach((to, from, next) => {
  if (to.matched.some((r) => r.meta.requireAuth)) {
    let token = localStorage.getItem('adminToken');
    if (token) {   //判断是否已经登录
      next();
    } else {
      next({
        path: '/admin/login',
        query: {redirect: to.fullPath}   //登录成功后重定向到当前页面
      });
    }
  } else {
    next();
  }
});
export default router
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}


