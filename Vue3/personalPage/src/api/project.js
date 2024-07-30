import request from '@/utils/request.js'
import { useTokenStore } from '@/pinia/token.js'
export const projectCategoryListService = () => {
    const tokenStore = useTokenStore();
    return request({
        url:'/category/list',
        method:'get',
        /*
        headers:{
            'Authorization':tokenStore.getToken()
        }
        */
    })
}
export const projectCategoryUpdateService = (categoryModel) => {
    return request.put('/category/update', categoryModel)
}

export const projectCategoryAddService = (categoryModel) => {
    return request.post('/category/addProject', categoryModel)
}

export const projectCategoryDeleteService = (id) => {
    return request.delete(`/category/delete?id=${id}`)
}

export const projectListService = (param) => {
    return request.get(`/project/list`, { params: param })
}

export const projectAddService = (projectModel) => {
    return request.post('/project/add', projectModel)
}

export const projectUpdateService = (projectModel) => {
    return request.put('/project/update', projectModel)
}

export const projectDeleteService = (id) => {
    return request.delete(`/project/delete?id=${id}`)
}