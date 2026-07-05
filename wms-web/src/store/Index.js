import Vue from 'vue'
import Vuex from 'vuex'
import router,{resetRouter} from '../router/Index.js'

Vue.use(Vuex)

function addNewRoute(menuList){
    console.log(JSON.parse(JSON.stringify(menuList)))
    let routes = router.options.routes;
    console.log(routes)
    routes.forEach(route => {
        if(route.path =="/Index"){
            menuList.forEach(menu => {
                let childRoute = {
                    path: '/'+menu.menuClick,
                    name: menu.menuName,
                    meta:{
                        title:menu.menuName
                    },
                    component:()=>import('../components/'+menu.menuComponent),
                }
                route.children.push(childRoute)
            })
        }
    })
    resetRouter()
    router.addRoutes(routes)
}

export default new Vuex.Store({
    state: {
        menu:[]
    },
    mutations: {
        setMenu(state, menuList) {
            state.menu = menuList
            addNewRoute(menuList)
        }
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    }
})
