import Router from 'vue-router'
import VueRouter from "vue-router";

const routes = [
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
                    title:'管理员管理'
                },
                component:()=>import('@/components/admin/AdminManage.vue'),
            },
            {
                path: '/User',
                name: 'user',
                meta:{
                    title:'用户管理'
                },
                component:()=>import('@/components/user/UserManage.vue'),
            },
        ]
    }
]

const router = new Router({
    mode: 'history',
    routes
})

export function resetRouter() {
    router.matcher = new VueRouter({
        mode: 'history',
        routes: [],
    }).matcher
}

const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(to) {
    return VueRouterPush.call(this, to).catch(err => err);
}

export default router;