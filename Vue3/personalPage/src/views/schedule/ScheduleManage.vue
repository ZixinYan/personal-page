<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>日程安排</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer=true;title='添加日程'">添加日程</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="截止日期：">
                <el-date-picker 
                    v-model="rawDeadline"
                    type="date" 
                    placeholder="请选择截止日期" 
                    @change="formatDeadline"
                    style="width: 200px;">
                </el-date-picker>
            </el-form-item>

            <el-form-item label="优先级：">
                <el-rate v-model="prioritys" :max="5" />
            </el-form-item>

            <el-form-item label="状态：">
                <el-select placeholder="请选择" v-model="finishs" style="width: 200px;">
                    <el-option label="已完成" value="已完成"></el-option>
                    <el-option label="进行中" value="进行中"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="scheduleList">搜索</el-button>
                <el-button @click="resetFilters">重置</el-button>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="findToday()">仅显示今日创建日程</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="schedules" style="width: 100%">
            <el-table-column label="截止日期" width="150">
                <template #default="{ row }">
                    <span>{{ row.deadline }}</span>
                </template>
            </el-table-column>
            <el-table-column label="日程内容" width="300" prop="content"></el-table-column>
            <el-table-column label="番茄钟使用">
                <template #default="{ row }">
                    <span v-for="n in row.actualTime" :key="n" style="margin-right: 4px;">x</span>
                </template>
            </el-table-column>
            <el-table-column label="预测番茄钟" prop="predictTime" width="100"></el-table-column>
            <el-table-column label="优先级" width="200">
                <template #default="{ row }">
                    <el-rate v-model="row.priority" :max="5" disabled />
                </template>
            </el-table-column>

            <el-table-column label="建立时间" prop="createTime"></el-table-column>
            <el-table-column label="状态" prop="finish"></el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                    <el-tooltip class="item" effect="dark" content="开始一个番茄时间" placement="top">
                        <el-button :icon="Check" circle plain type="primary" @click="StartCountDown(row)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="编辑日程" placement="top">
                        <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="删除日程" placement="top">
                        <el-button :icon="Delete" circle plain type="danger" @click="deleteschedule(row)"></el-button>
                    </el-tooltip>
                    
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[2, 3, 5 ,10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
        <!-- 抽屉 -->
        <el-drawer v-model="visibleDrawer" :title="title" direction="rtl" size="50%">
            <!-- 添加文章表单 -->
            <el-form :model="scheduleModel" label-width="100px">
                <el-form-item label="日程内容">
                    <el-input v-model="scheduleModel.content" placeholder="请输入日程"></el-input>
                </el-form-item>  
                <el-form-item label="日程优先级">
                    <el-rate v-model="scheduleModel.priority" :max="5" />
                </el-form-item>
                <el-form-item label="截止日期">
                    <el-date-picker v-model="scheduleModel.rawDeadline" type="date" placeholder="请选择日期" @change="formatScheduleDeadline"></el-date-picker>
                </el-form-item>
                <el-form-item label="预测番茄钟">
                    <el-select v-model="scheduleModel.predictTime" placeholder="请选择预测番茄钟个数">
                        <el-option v-for="n in 7" :key="n" :label="n" :value="n"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="title=='添加日程' ? scheduleAdd('已完成') : updateschedule('已完成')">已完成</el-button>
                    <el-button type="info" @click="title=='添加日程' ? scheduleAdd('进行中') : updateschedule('进行中')">进行中</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Check, Edit, Delete, Right } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useTokenStore } from '@/pinia/token.js';
import { scheduleListService, scheduleAddService, scheduleDeleteService, scheduleUpdateService } from '@/api/schedule.js'

const schedules = ref([])

const rawDeadline = ref('')
const deadline = ref('')
const finishs = ref('')
const prioritys = ref(0)

const pageNum = ref(1)
const total = ref(0)
const pageSize = ref(3)

const onSizeChange = (size) => {
    pageSize.value = size
    scheduleList()
}

const onCurrentChange = (num) => {
    pageNum.value = num
    scheduleList()
}

const today = computed(() => {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
});

const formatDate = (date) => {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

const formatDeadline = (date) => {
    if (!date) return;
    const localDate = new Date(date);
    deadline.value = formatDate(localDate);
}

const scheduleList = async () => {
    let param = {
        pageNum: pageNum.value||1,
        pageSize: pageSize.value||3,
        deadline: deadline.value, 
        finish: finishs.value,
        priority: prioritys.value
    }
    console.log(param)
    let result = await scheduleListService(param)
    console.log(result)
    if (result.data && result.data.items.length > 0) {
        total.value = result.data.total
        schedules.value = result.data.items
    } else {
        schedules.value = []
    }
}

const findToday = async () => {
    try {
        let result = await scheduleListService({
            pageNum: pageNum.value || 1,
            pageSize: pageSize.value || 3,
            deadline: deadline.value, 
            finish: finishs.value,
            priority: prioritys.value
        });
        if (result.data && result.data.items.length > 0) {
            schedules.value = result.data.items.filter(item => formatDate(item.createTime) === today.value);
            console.log(schedules.value);
        } else {
            schedules.value = [];
            console.log(result.data);
        }
    } catch (error) {
        console.error('Error fetching today\'s schedule:', error);
        ElMessage.error('获取今日日程失败');
    }
}
const visibleDrawer = ref(false)
const scheduleModel = ref({
    id:'',
    content: '',
    finish: '',
    priority: 0,
    predictTime: '',
    actualTime: '',
    rawDeadline: ''
})

const clearForm = () => {
    scheduleModel.value = {
        id:'',
        content: '',
        finish: '',
        priority: 0,
        predictTime: '',
        actualTime: '',
        rawDeadline: ''
    }
}

const formatScheduleDeadline = (date) => {
    if (!date) return;
    const localDate = new Date(date);
    scheduleModel.value.deadline = formatDate(localDate);
}


const scheduleAdd = async (clickState) => {
    if (!validateForm()) {
        clearForm()
        return;
    }
    scheduleModel.value.finish = clickState
    scheduleModel.value.predictTime = scheduleModel.value.predictTime ? scheduleModel.value.predictTime : 1
    scheduleModel.value.actualTime = 0
    let res = await scheduleAddService(scheduleModel.value)
    console.log(res)
    ElMessage.success('添加成功')
    visibleDrawer.value = false
    clearForm()
    scheduleList()
}

const tokenStore = useTokenStore()

const title = ref('');

const showDialog = (row) => {
    visibleDrawer.value = true;
    title.value = '编辑日程'
    scheduleModel.value = { ...row }
    scheduleModel.value.rawDeadline = new Date(row.deadline)
}

const updateschedule = async (clickState) => {
    if (!validateForm()) return;
    scheduleModel.value.finish = clickState
    await scheduleUpdateService(scheduleModel.value)
    ElMessage.success('修改成功')
    visibleDrawer.value = false
    clearForm()
    scheduleList()
}

const deleteschedule = async (row) => {
    ElMessageBox.confirm('此操作将永久删除日程记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async() => {
        console.log(row.id);
        let result = await scheduleDeleteService(row.id);
        ElMessage.success(result.message ? result.message : '删除成功')
        scheduleList()
    }).catch(() => {
        ElMessage.info('已取消删除')
    });
}

const resetFilters = () => {
    rawDeadline.value = ''
    deadline.value = ''
    finishs.value = ''
    prioritys.value = 0
    scheduleList()
}

const validateForm = () => {
    if (!scheduleModel.value.content) {
        ElMessage.error('日程内容不能为空');
        return false;
    }
    if (!scheduleModel.value.deadline) {
        ElMessage.error('截止日期不能为空');
        return false;
    }
    if (scheduleModel.value.priority === 0) {
        ElMessage.error('优先级不能为0');
        return false;
    }
    return true;
}

import { useRouter } from 'vue-router'
const router = useRouter();
import CryptoJS from 'crypto-js';

const StartCountDown = (row) => {
    const encryptedRow = CryptoJS.AES.encrypt(JSON.stringify(row), 'helloworld').toString();
    router.push({ path: '/countdown', query: { row: encryptedRow } });
}

scheduleList()
</script>


<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;
    user-select: none; 

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        user-select: none; 
    }
}
</style>
