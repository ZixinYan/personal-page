import request from '@/utils/request.js'
import { useTokenStore } from '@/pinia/token.js'
export const scheduleListService = (param) => {
    return request.get(`/schedule/list`, { params: param })
} 

export const scheduleAddService = (data) => {
    return request({
        url:'/schedule/add',
        method:'post',
        data
    })
}

export const scheduleUpdateService = (data) => {
    return request({
        url:'/schedule/update',
        method:'put',
        data
    })
}

export const scheduleDeleteService = (id) => {
    return request.delete(`/schedule/delete?id=${id}`)
}
