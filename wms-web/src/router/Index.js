import Router from 'vue-router'

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
    }
]

const router = new Router({
    mode: 'history',
    routes
})

export default router;