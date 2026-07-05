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
            }
        ]
    }
]

const router = new Router({
    mode: 'history',
    routes
})
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(to) {
    return VueRouterPush.call(this, to).catch(err => err);
}

export default router;