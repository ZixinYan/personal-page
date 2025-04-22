<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'

//文章分类数据模型
const categorys = ref([
    {
        id : '',
        categoryName: '',
        categoryAlias: '',
        createTime: '',
        updateTime: '',
        content:''
    }
])
//用户搜索时选中的分类id
const categoryId=ref('')

//用户搜索时选中的发布状态
const state=ref('')

//文章列表数据模型
const project = ref([
    {
        id: 0,
        projectName: '',
        categoryId: 0,
        projectUrl: '',
        briefNote: '',
        createTime: '',
        updateTime: '',
        status: '',
    }
])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    projectList()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    projectList()
}
import {projectCategoryListService,projectListService,projectAddService} from '@/api/project.js'
//访问后台，查询文章列表
const projectCategoryList = async () => {
    let res = await projectCategoryListService()
    console.log(res)
    categorys.value = res.data.filter(item => item.content === '项目');
}
//页面加载时，调用查询文章列表的函数q
const projectList = async () => {
    let param = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value?categoryId.value:null,
        status: state.value?state.value:null
    }
    let result = await projectListService(param)
    console.log(result)
    total.value = result.data.total
    project.value = result.data.items
    
    for(let i = 0; i < project.value.length; i++) {
        let projectObj = project.value[i]
        for (let j = 0; j < categorys.value.length; j++) {
            if(projectObj.categoryId === categorys.value[j].id) {
                projectObj.categoryId = categorys.value[j].categoryName
            }
        }
    }
}


projectCategoryList()
projectList()

import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const projectModel = ref({
        id: "",
        projectName: "",
        categoryId: "",
        projectUrl: "",
        briefNote: "",
        createTime: "",
        updateTime: "",
        status: "",
})
const clearForm = () => {
    projectModel.value = {
        id: "",
        projectName: "",
        categoryId: "",
        projectUrl: "",
        briefNote: "",
        createTime: "",
        updateTime: "",
        status: "",
    }
}

import { ElMessage,ElMessageBox } from 'element-plus'
const projectAdd = async (clickState) => {
    projectModel.value.status = clickState
    let res = await projectAddService(projectModel.value)
    console.log(res)
    ElMessage.success('添加成功')
    visibleDrawer.value = false
    clickState = ''
    clearForm()
    projectList()
}

import { useTokenStore } from '@/pinia/token.js';
const tokenStore = useTokenStore()

const title = ref('');

const showDialog = (row) => {
    visibleDrawer.value = true;
    title.value = '编辑项目'
    //将当前行的数据赋值给projectModel
    projectModel.value = row
}

import { projectDeleteService, projectUpdateService } from '@/api/project.js'
const updateproject = async (clickState)=>{
    projectModel.value.status = clickState
    for(let i = 0; i < categorys.value.length; i++) {
        if(projectModel.value.categoryId === categorys.value[i].categoryName) {
            projectModel.value.categoryId = categorys.value[i].id
        }
    }
    let result = await projectUpdateService(projectModel.value);
    ElMessage.success('修改成功')
    //隐藏弹窗
    visibleDrawer.value = false
    //再次访问后台接口，查询所有分类
    clearForm()
    projectList()
}

//删除分类
const deleteproject= async (row) => {
    ElMessageBox.confirm('此操作将永久删除项目记录, 是否继续?', '才不是温馨提示你呢！', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async() => {
        let result = await projectDeleteService(row.id);
        ElMessage.success(result.message? result.message:'删除成功')
        //再次访问后台接口，查询所有文章
        projectList()
    }).catch(() => {
        ElMessage.info('已取消删除')
    });
}

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>项目管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer=true;title='添加项目'">添加项目</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="项目分类：">
                <el-select placeholder="请选择" v-model="categoryId" style="width: 200px;">
                    <el-option 
                        v-for="c in categorys" 
                        :key="c.id" 
                        :label="c.categoryName"
                        :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state" style="width: 200px;">
                    <el-option label="已完成" value="已完成"></el-option>
                    <el-option label="开发中" value="开发中"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="projectList">搜索</el-button>
                <el-button @click="categoryId='';state=''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="project" style="width: 100%">
            <el-table-column label="项目标题" width="400" prop="projectName">
                <template v-slot="scope">
                  <a :href="scope.row.projectUrl" target="_blank">{{ scope.row.projectName }}</a>
                </template>
            </el-table-column>
            <el-table-column label="分类" prop="categoryId"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="status"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteproject(row)" ></el-button>
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
            <el-form :model="projectModel" label-width="100px" >
                <el-form-item label="项目名称" >
                    <el-input v-model="projectModel.projectName" placeholder="请输入标题"></el-input>
                </el-form-item>  
                <el-form-item label="项目分类">
                    <el-select placeholder="请选择" v-model="projectModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="项目链接">
                    <el-input v-model="projectModel.projectUrl" placeholder="请输入链接"></el-input>
                </el-form-item>
                <el-form-item label="项目简介">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="projectModel.briefNote" contentType="html"></quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="title=='添加项目'?projectAdd('已完成'):updateproject('已完成')">已完成</el-button>
                    <el-button type="info" @click="title=='添加项目'?projectAdd('开发中'):updateproject('开发中')">开发中</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>
<style lang="scss" scoped>

a{
    color: #409EFF;
    text-decoration: none;
    font-style: italic;
}

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
.editor {
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>