import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../views/Manage.vue'),
    redirect: '/home',
    children: [
      {path:'home',name:'首页',component:()=>import('../views/Home.vue')},
      {path:'user',name:'用户管理',component:()=>import('../views/User.vue')},
      {path:'PersonInfo',name:'个人信息',component:()=>import('../views/PersonInfo.vue')},
      {path:'file',name:'文件管理',component:()=>import('../views/File.vue')},
   ]
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  localStorage.setItem('currentPathName',to.name ) // 保存当前路由的name
  store.commit('setPath') // 触发store路由变化
    next()
})

export default router
