import Router from 'vue-router'
import VueRouter from "vue-router";

export const staticRoutes = [
    {
        path: '/',
        name: 'login',
        component:()=>import('@/components/HomeLogin.vue'),
    },
    {
        path: '/Index',
        name: 'index',
        component:()=>import('@/components/HomeIndex.vue'),
        children: [
            {
                path: '/Home',
                name: 'home',
                meta:{
                    title:'首页'
                },
                component:()=>import('@/components/HomeHome.vue'),
            },
            {
                path: '/Admin',
                name: 'admin',
                meta:{
                    title:'项目组长管理'
                },
                component:()=>import('@/components/admin/AdminManage.vue'),
            },
            {
                path: '/User',
                name: 'user',
                meta:{
                    title:'员工管理'
                },
                component:()=>import('@/components/user/UserManage.vue'),
            },
        ]
    }
]

function createRouter() {
    return new Router({
        mode: 'history',
        routes: staticRoutes
    })
}

const router = createRouter()

export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher
}

const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(to) {
    return VueRouterPush.call(this, to).catch(err => err);
}

export default router;
