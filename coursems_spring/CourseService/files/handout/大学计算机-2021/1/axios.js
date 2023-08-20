import axios from 'axios'
import router from './router'
import store from './store'

axios.defaults.baseURL="http://localhost:8888"

axios.interceptors.request.use(config=>{
    return config
})

axios.interceptors.response.use(response=>{
        let res = response.data;
        if(res.code === 200||response.status===200){
            return response
        } else {
            // Element.Message.error(res.msg,{duration:2*1000});
            console.log(res.msg)
            return response
        }
    },
    error=>{
        console.log(error)
        if(error.response.data){
            error.message = error.response.data.msg
        }
        if(error.response.status===401){
            store.commit("REMOVE_INFO")
            router.push("/login")
        }
        Element.Message.error(error.message,{duration:2*1000});
        return Promise.reject(error)
    }
)
