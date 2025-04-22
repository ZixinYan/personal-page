import {createRouter, createWebHistory} from 'vue-router'
import LoginVue from '@/views/Login.vue'
import LayoutVue from '@/views/Layout.vue'

// 1. Define route components.
const routes = [
    {path: '/', component: LoginVue},
    {path:'/login',component:()=>import('@/views/LoginByPhone.vue')},
    {path:'/findpassword',component:()=>import('@/views/FindPassword.vue')},
    {path:'/countdown',component:()=>import('@/views/CountdownTimer.vue')},
    {path: '/page', component: LayoutVue,redirect:'/main',children:[
        {path:'/article/category',component:()=>import('@/views/article/ArticleCategory.vue')},
        {path:'/article/manage',component:()=>import('@/views/article/ArticleManage.vue')},
        {path:'/project/category',component:()=>import('@/views/project/ProjectCategory.vue')},
        {path:'/project/manage',component:()=>import('@/views/project/ProjectManage.vue')},
        {path:'/user/avatar',component:()=>import('@/views/user/UserAvatar.vue')},
        {path:'/user/info',component:()=>import('@/views/user/UserInfo.vue')},
        {path:'/user/resetpassword',component:()=>import('@/views/user/UserResetPassword.vue')},
        {path:'/schedule/manage',component:()=>import('@/views/schedule/ScheduleManage.vue')},
        {path:'/main',component:()=>import('@/views/main.vue')},
    ]
    },
]

// 2. Create the router instance and pass the `routes` option
const router = createRouter({
    history: createWebHistory(),
    routes:routes
})

// 3. Export the router instance
export default router



        /*
      import('@/assets/login/1.png'),
        import('@/assets/login/2.png'),
        import('@/assets/login/3.png'),
        import('@/assets/login/4.png'),
        import('@/assets/login/5.png'),
        import('@/assets/login/6.png'),
        import('@/assets/login/7.png'),
        */