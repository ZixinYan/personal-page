<script setup>
import {
    Files,
    Collection,
    HomeFilled,
    Management,
    Promotion,
    UserFilled,
    User,
    StarFilled,
    Crop,
    Tools,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'
import background from '@/assets/background.jpg'
import {userInfoService} from '@/api/user.js'
import useUserInfoStore from '@/pinia/userInfo.js'
import useTokenStore from '@/pinia/token.js'
const userInfoStore = useUserInfoStore()
const getUserInfo= async ()=>{
    let res = await userInfoService()
    userInfoStore.setUser(res.data)
}

getUserInfo()
import { ElMessageBox, ElMessage } from 'element-plus'
const tokenStore = useTokenStore()
const handelCommand = (command) => {
    if (command === 'logout') {
        ElMessageBox.confirm('确定退出登录吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            tokenStore.delToken()
            location.href = '/'
        }).catch(() => {
            ElMessage.info('已取消退出')
        })
    } else {
        location.href = '/user/' + command
    }
}
</script>

<template>
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <el-menu active-text-color="#ffd04b" background-color="#232323"  text-color="#fff"
                router>
                <el-menu-item index="/main">
                    <el-icon><HomeFilled /></el-icon>
                    <span>主 页 面</span>
                </el-menu-item>

                <el-menu-item index="/schedule/manage">
                    <el-icon><StarFilled /></el-icon>
                    <span>日程管理</span>
                </el-menu-item>

                <el-sub-menu index="1">
                    <template #title>
                        <el-icon>
                            <Management/>
                        </el-icon>
                        <span>日  记</span>
                    </template>                
                    <el-menu-item index="/article/category">
                        <el-icon><Collection /></el-icon>
                    <span>日记分类</span>
                </el-menu-item>
                <el-menu-item index="/article/manage" >
                    <el-icon><Files /></el-icon>
                    <span>日记管理</span>
                </el-menu-item>
            </el-sub-menu>

            <el-sub-menu>
            <template #title>
                        <el-icon>
                            <Tools />
                        </el-icon>
                        <span>项 目</span>
                    </template>                
                <el-menu-item index="/project/category">
                    <el-icon>
                        <Collection />
                    </el-icon>
                    <span>项目分类</span>
                </el-menu-item>
                <el-menu-item index="/project/manage" >
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>项目管理</span>
                </el-menu-item>
            </el-sub-menu>
                <el-sub-menu index="2">
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index="/user/info">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item index="/user/avatar">
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <el-menu-item index="/user/resetpassword">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>


            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div>Welcome：<strong>{{ userInfoStore.user.nickname }}</strong></div>
                <el-dropdown placement="bottom-end" @command="handelCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.user.userPic ? userInfoStore.user.userPic : avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User" class="header no-select">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop" class="header no-select">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetpassword" :icon="EditPen" class="header no-select">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton" class="header no-select">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
        
            <!-- 中间区域 -->
            <el-main class="main-content">
                <router-view></router-view>
            </el-main>
            <el-footer>个人中心 ©2024 Created by zx</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;
    user-select: none; 

    .el-aside {
        background-color: #232323;
        box-shadow: 0 10px 8px rgba(5, 5, 6, 7); // 添加阴影
        &__logo {
            height: 120px;
            background: url('@/assets/logo.png') no-repeat center / 120px auto;
            background-size: contain;
            user-select: none; 
        }

        .el-menu {
            border-right: none; 
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;
            user-select: none;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}

.main-content {
  flex: 1;
  background-image: url('@/assets/background.jpg'); /* 替换为你的背景图片路径 */
  background-size: cover;
  background-position: center;
  color: white; /* 根据需要调整文本颜色以确保在背景图片上可见 */
}

.no-select {
  user-select: none !important; /* 确保样式具有最高优先级 */
  -webkit-user-select: none !important;
  -moz-user-select: none !important;
  -ms-user-select: none !important;
}

</style>
