<script setup>
import { ref } from 'vue'
const passwords = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
})
const rules = {
    old_pwd: [
        { required: true, message: '请输入原密码', trigger: 'blur' },
        {
            pattern: /^\S{6,20}$/,
            message: '密码必须是6-20位的非空字符串',
            trigger: 'blur'
        }
    ],
    new_pwd: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        {
            pattern: /^\S{6,20}$/,
            message: '密码必须是6-20位的非空字符串',
            trigger: 'blur'
        }
    ],
    re_pwd: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        {
            pattern: /^\S{6,20}$/,
            message: '密码必须是6-20位的非空字符串',
            trigger: 'blur'
        }
    ]
}
import { resetpasswordService } from '@/api/user.js'
import { ElMessage } from 'element-plus'

import { useRouter } from 'vue-router'
const router = useRouter()

const resetpassword = async() => {
    if(passwords.value.newpassword !== passwords.value.repassword){
        ElMessage.error('两次输入的密码不一致')
        return
    }
    let result = await resetpasswordService(passwords.value)
    ElMessage.success(result.message? result.message:'修改成功')
    router.push('/')
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>重置密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="passwords" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="原密码" prop="old_pwd" class="header">
                        <el-input v-model="passwords.old_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd" class="header">
                        <el-input v-model="passwords.new_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="二次输入" prop="re_pwd" class="header">
                        <el-input v-model="passwords.re_pwd"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="resetpassword()">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.header {
    display: flex;
    justify-content: space-between;
    user-select: none; 
}
</style>