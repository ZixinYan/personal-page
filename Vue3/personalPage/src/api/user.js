import request from '@/utils/request.js'

export const userRegisterService = (registerData)=>{
    const params = new URLSearchParams()
    for(let key in registerData){
        params.append(key,registerData[key])
    }
    return request.post('/user/register', params)
}

export const userLoginService = (loginData)=>{
    const params = new URLSearchParams()
    for(let key in loginData){
        params.append(key,loginData[key])
    }
    return request.post('/user/login', params)
}

export const userInfoService = ()=>{
    return request.get('/user/userinfo')
}

export const updateUserInfoService = (userInfo)=>{
    return request.put('/user/update', userInfo)
}

export const updateUserAvaterService = (avatarUrl)=>{
    const params = new URLSearchParams();
    params.append('avatarUrl',avatarUrl)
    return request.patch('/user/updateAvatar',params)
}

export const resetpasswordService = (passwords)=>{
    return request.patch('/user/updatePwd',passwords)
}

export const findPasswordService = (FindPassword)=>{
    return request.post('/findPassword',FindPassword)
}

export const getPermissionService = (FindPassword)=>{
    return request.post('/code/getCode',FindPassword)
}

export const sendCodeService = (FindPassword)=>{
    return request.post('/code/sendEmailCode',FindPassword)
}