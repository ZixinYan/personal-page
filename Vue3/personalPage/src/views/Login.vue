<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

// 控制注册与登录表单的显示，默认显示注册
const isRegister = ref(false)
const registerData = ref({
    username: '',
    password: '',
    repassword: ''
})
const loginData = ref({
    username: '',
    password: ''
})

const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 10, message: '长度在 5 到 16 个字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 5 到 16 个字符', trigger: 'blur' }
    ],
    repassword: [
        {
            required: true,
            message: '请再次输入密码',
            trigger: 'blur'
        },
        {
            validator: (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'))
                } else if (value !== registerData.value.password) {
                    callback(new Error('两次输入密码不一致!'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ]
}

import { userRegisterService } from '@/api/user.js'
import { userLoginService } from '@/api/user.js'
import { useTokenStore } from '@/pinia/token.js'

const router = useRouter();
const tokenStore = useTokenStore();

const register = async () => {
    let result = await userRegisterService(registerData.value);
    ElMessage.success('注册成功');
}

const login = async () => {
    let result = await userLoginService(loginData.value);
    ElMessage.success('登录成功');
    tokenStore.setToken(result.data);
    router.push('/page');
}

const clearRegisterData = () => {
    registerData.value.username = '';
    registerData.value.password = '';
    registerData.value.repassword = '';
}

const findPassword = () => {
    router.push('/findPassword');
}

// 轮播图片
const pictures = ref('');

onMounted(async () => {
  const images = import.meta.glob('@/assets/login/*.png');
  const keys = Object.keys(images);
  const randomKey = keys[Math.floor(Math.random() * keys.length)];
  pictures.value = await images[randomKey]().then(mod => mod.default);
});

</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg" :style="{ backgroundImage: 'url(' + pictures + ')' }"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1 class="title">注   册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="repassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码" v-model="registerData.repassword"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false; clearRegisterData()">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="loginData" :rules="rules">
                <el-form-item>
                    <h1 class="title">登   录</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="loginData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="loginData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false" @click="findPassword()">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true; clearRegisterData()">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
.login-page {
    width: 100vw;
    height: 100vh;
    background-color: rgb(177, 217, 232) ;
        display: flex; // 添加 flex 布局
        backdrop-filter: blur(10px); // 添加磨砂效果

    .bg {
        width: 80vw;
        height: 100vh;
        background-size: cover;
        background-position: center;
        border-radius: 5px 40px 40px 5px;
        position: relative;
        overflow: hidden;
        box-shadow: 0 10px 8px rgba(5, 5, 10, 7); // 添加阴影
    }

    .form {
        width: 20vw;
        display: flex;
        flex-direction: column;
        justify-content: center;
        margin: auto; // 居中对齐
        user-select: none;
        padding:  50px; // 可选：添加一些内边距
        background: rgba(255, 255, 255, 0.6); // 更改背景色
        border-radius: 30px; // 添加圆角
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); // 添加阴影

        .title {
            margin: 10px 0 0 10px;
            font-size: 30px;
            font-style: italic;
        }

        .button {
            width: 80%;
            margin: 5px auto;
            
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>

