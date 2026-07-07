import Vue from 'vue'
import Vuex from 'vuex'
import router,{resetRouter} from '../router/Index.js'
import createPersistedstate from 'vuex-persistedstate'

Vue.use(Vuex)

function addNewRoute(menuList){
    if(!Array.isArray(menuList) || menuList.length === 0){
        resetRouter()
        return
    }

    resetRouter()
    menuList.forEach(menu => {
        router.addRoute('index', {
            path: '/'+menu.menuClick,
            name: menu.menuName,
            meta:{
                title:menu.menuName
            },
            component:()=>import('../components/'+menu.menuComponent),
        })
    })

    if(router.currentRoute.path && router.currentRoute.path !== '/'){
        router.replace(router.currentRoute.fullPath).catch(() => {})
    }
}

export default new Vuex.Store({
    state: {
        menu:[]
    },
    mutations: {
        setMenu(state, menuList) {
            state.menu = Array.isArray(menuList) ? menuList : []
            addNewRoute(state.menu)
        },
        restoreRoutes(state){
            addNewRoute(state.menu)
        },
        clearMenu(state){
            state.menu = []
            resetRouter()
        }
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    },
    plugins:[createPersistedstate({
        storage: window.sessionStorage,
        paths: ['menu']
    })]
})
