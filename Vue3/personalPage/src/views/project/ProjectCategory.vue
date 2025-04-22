<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
//model of categorys
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
import {projectCategoryListService,projectCategoryAddService,projectCategoryUpdateService,projectCategoryDeleteService } from '@/api/project.js'
const projectCategoryList = async() => {
    let res = await projectCategoryListService()
    categorys.value = res.data.filter(item => item.content === '项目');

}


projectCategoryList();

//控制添加分类弹窗
const dialogVisible = ref(false)

//添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
//添加分类表单校验
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}
import { ElMessage,ElMessageBox } from 'element-plus'
//访问后台，添加文章分类
const addCategory = async ()=>{
    categoryModel.value.content = '项目'
    let result = await projectCategoryAddService(categoryModel.value);
    ElMessage.success(result.message? result.message:'添加成功')
    //隐藏弹窗
    dialogVisible.value = false
    //再次访问后台接口，查询所有分类
    projectCategoryList()
}

const updateCategory = async ()=>{
    categoryModel.value.content = '项目'
    let result = await projectCategoryUpdateService(categoryModel.value);
    ElMessage.success(result.message? result.message:'修改成功')
    //隐藏弹窗
    dialogVisible.value = false
    //再次访问后台接口，查询所有分类
    projectCategoryList()
}

//删除分类
const deleteCategory = async (row) => {
    ElMessageBox.confirm('此操作将永久删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async() => {
        let result = await projectCategoryDeleteService(row.id);
        ElMessage.success(result.message? result.message:'删除成功')
        //再次访问后台接口，查询所有分类
        projectCategoryList()
    }).catch(() => {
        ElMessage.info('已取消删除')
    });
}

//获取所有分类
const title = ref('编辑分类')

const showDialog = (row) => {
    dialogVisible.value = true;
    title.value = '编辑分类'
    //将当前行的数据赋值给categoryModel
    categoryModel.value.categoryName = row.categoryName;
    categoryModel.value.categoryAlias = row.categoryAlias;
    //将当前行的id赋值给categoryModel
    categoryModel.value.id = row.id;
}

const clearData = () => {
    categoryModel.value.categoryName = '';
    categoryModel.value.categoryAlias = '';
}

</script>


<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>项目分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true;title='添加分类';clearData()">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <el-dialog v-model="dialogVisible" :title="title" width="30%">
           <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
            <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
            </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
            </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="title=='添加分类'?addCategory():updateCategory()"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>

    </el-card>
</template>


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