<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref,onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const isFindPassword = ref(true)
const FindPasswordData = ref({
    email: '',
    password: '',
    passwordConfirm: '',
    code:''
})



const rules={
    email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 5 到 16 个字符', trigger: 'blur' }
    ],
    passwordConfirm: [
        {
            required: true,
            message: '请再次输入密码',
            trigger: 'blur'
        },
        {
            validator: (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'))
                } else if (value !== FindPasswordData.value.password) {
                    callback(new Error('两次输入密码不一致!'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ],
    code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
    ]
}

import { findPasswordService, getPermissionService, sendCodeService } from '@/api/user.js'
import {useRouter} from 'vue-router'
const router = useRouter();
const SendCode = async()=>{
    const permissionResponse = await getPermissionService(FindPasswordData.value);
        if (permissionResponse && permissionResponse.data) {
            console.log(permissionResponse.data);
            // 将权限码赋值给 FindPasswordData.value 的 code 字段
            FindPasswordData.value.code = permissionResponse.data;
        }
    await sendCodeService(FindPasswordData.value);
    FindPasswordData.value.code = '';
    ElMessage.success('验证码发送成功');
}
const FindPassword = async()=>{
    let result = await findPasswordService(FindPasswordData.value);
    ElMessage.success('重置密码成功');
    router.push('/');
}
import {useTokenStore} from '@/pinia/token.js'
const tokenStore = useTokenStore();

const clearFindPasswordData= ()=>{
    FindPasswordData.value.email = '';
    FindPasswordData.value.password = '';
    FindPasswordData.value.passwordConfirm = '';
    FindPasswordData.value.code = '';
}


const findPassword = ()=>{
    router.push('/findPassword');
}

const back= ()=>{
    router.push('/');
}


const pictures = ref('');

onMounted(async () => {
  const images = import.meta.glob('@/assets/login/*.png');
  const keys = Object.keys(images);
  const randomKey = keys[Math.floor(Math.random() * keys.length)];
  pictures.value = await images[randomKey]().then(mod => mod.default);
});
</script>

<template>
    <el-row class="find-page">
        <el-col :span="12" class="bg" :style="{ backgroundImage: 'url(' + pictures + ')' }"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 //model：绑定数据模型 rules规定前端校验模型-->
            <el-form ref="form" size="large" autocomplete="off" v-if="isFindPassword" :model="FindPasswordData" :rules="rules">
                <el-form-item>
                    <h1 class="title">重 置 密 码</h1>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input :prefix-icon="User" placeholder="请输入邮箱" v-model="FindPasswordData.email"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="FindPasswordData.password"></el-input>
                </el-form-item>
                <el-form-item prop="passwordConfirm">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请再次输入密码" v-model="FindPasswordData.passwordConfirm"></el-input>
                </el-form-item>
                <!-- 获取验证码按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="SendCode()">获取验证码</el-button>
                </el-form-item>
                <el-form-item prop="code">
                    <el-input :prefix-icon="Lock" type="code" placeholder="请输入验证码" v-model="FindPasswordData.code"></el-input>
                </el-form-item>
                <!-- 重置密码按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="FindPassword()">
                        重置密码
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="back()">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
.find-page {
    height: 100vh;
    background-color: rgb(177, 217, 232) ;
        display: flex; // 添加 flex 布局
        backdrop-filter: blur(10px); // 添加磨砂效果

    .bg {
        width: 60%;
        height: 100%;
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
            margin: 10px 0 10px 10px;
            font-size: 30px;
            font-style: italic;
        }

        .button {
            width: 100%;
            margin: auto;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>