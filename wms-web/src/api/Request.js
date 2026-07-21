import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8090',
    timeout: 5000
})

let redirectingToLogin = false

function handleUnauthorized(message) {
    if (redirectingToLogin) {
        return
    }
    redirectingToLogin = true
    sessionStorage.clear()
    alert(message || '登录已过期，请重新登录')
    window.location.href = '/'
}

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'

    const token = sessionStorage.getItem('Token')
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
}, error => {
    return Promise.reject(error)
})

request.interceptors.response.use(
    response => {
        let res = response.data
        if (response.config.responseType === 'blob') {
            return res
        }
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res
    },
    error => {
        if (error.response && error.response.status === 401) {
            const message = error.response.data && error.response.data.msg
            handleUnauthorized(message)
        }
        return Promise.reject(error)
    }
)

export default request
