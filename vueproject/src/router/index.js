import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";


Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
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


  //未找到路由的情况
  if(!to.matched.length){
    const storeMenus=localStorage.getItem("menus");
    if(storeMenus){
      next('/404')
    }else{
      next('/login')
    }
  }
  //放行
  next()
})

export default router

//刷新路由会导致页面重置
export const setRoutes=() =>{
  const storeMenus=localStorage.getItem('menus') // 获取菜单
  if(storeMenus) {

    //获取当前的路由对象名称数组
    const currentRouteNames=router.getRoutes().map(v=>v.name)
    if(!currentRouteNames.includes('Manage')) {
      //拼装动态路由
      const manageRoute = {
        path: '/',
        component: () => import('../views/Manage.vue'),
        name: 'Mange',
        redirect: '/home',
        children: [
          {path: 'personInfo', name: '个人信息', component: () => import('../views/PersonInfo.vue')},
          {path: 'password', name: '修改密码', component: () => import('../views/PassWord.vue')},
        ]
      }

      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {
        if (item.path) { //当且仅当path不为空才设置路由
          let itemMenu = {
            path: item.path.replace("/", ""),
            name: item.name,
            component: () => import('../views/' + item.pagePath + '.vue')
          }
          manageRoute.children.push(itemMenu)
        } else if (item.children.length) {
          item.children.forEach(item => {
            if (item.path) {
              let itemMenu = {
                path: item.path.replace("/", ""),
                name: item.name,
                component: () => import('../views/' + item.pagePath + '.vue')
              }
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
        //动态添加到现在的路由对象去
        router.addRoute(manageRoute);
      }
    }
}

//重置我就再set一遍
setRoutes()

//重置路由方法
export const resetRouter=() =>{
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

