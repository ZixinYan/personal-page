import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useTokenStore = defineStore('token', () => {
    //定义一个变量,记录token
    const token = ref('')    
    //定义一个方法,设置token
    const setToken = (newToken) => {
        token.value = newToken
    }
    //定义一个方法,获取token
    const getToken = () => {
        return token.value
    }
    //定义一个方法,删除token
    const delToken = () => {
        token.value = ''
    }
    //presist:true表示该store会被持久化
    return {token,setToken,getToken,delToken,presist:true}
    


}
)
export default useTokenStore