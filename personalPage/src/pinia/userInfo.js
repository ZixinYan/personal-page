import {defineStore} from 'pinia'
import {ref} from 'vue'

const useUserInfoStore  = defineStore('userInfo',() => {
    const user = ref({})

    const setUser = (userInfo) => {
        user.value = userInfo
    }
    
    const getUser = () => {
        return user.value
    }

    const clearUser = () => {
        user.value = {}
    }

    return {
        user,
        setUser,
        getUser,
        clearUser
    }
},{persist: true})

export default useUserInfoStore
