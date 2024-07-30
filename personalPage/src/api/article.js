 import request from '@/utils/request.js'
import { useTokenStore } from '@/pinia/token.js'
export const articleCategoryListService = () => {
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
export const articleCategoryUpdateService = (categoryModel) => {
    return request.put('/category/update', categoryModel)
}

export const articleCategoryAddService = (categoryModel) => {
    return request.post('/category/addArticle', categoryModel)
}

export const articleCategoryDeleteService = (id) => {
    return request.delete(`/category/delete?id=${id}`)
}

export const articleListService = (param) => {
    return request.get(`/article/list`, { params: param })
}

export const articleAddService = (articleModel) => {
    return request.post('/article/add', articleModel)
}

export const articleUpdateService = (articleModel) => {
    return request.put('/article/update', articleModel)
}

export const articleDeleteService = (id) => {
    return request.delete(`/article/delete?id=${id}`)
}