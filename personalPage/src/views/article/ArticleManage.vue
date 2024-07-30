<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'

//文章分类数据模型
const categorys = ref([
    {
        id: 0,
        categoryName: "",
        categoryAlias: "",
        createTime: "",
        updateTime: ""
    }
])

//用户搜索时选中的分类id
const categoryId=ref('')

//用户搜索时选中的发布状态
const state=ref('')

//文章列表数据模型
const articles = ref([
    {
        id: 0,
        title: "",
        content: "",
        coverImg: "",
        state: "",
        categoryId: 0,
        createTime: "",
        updateTime: ""
    }
])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}
import {articleCategoryListService,articleListService,articleAddService} from '@/api/article.js'
//访问后台，查询文章列表
const articleCategoryList = async () => {
    let res = await articleCategoryListService()
    categorys.value = res.data.filter(item => item.content === '日记');
}
//页面加载时，调用查询文章列表的函数q
const articleList = async () => {
    let param = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value?categoryId.value:null,
        state: state.value?state.value:null
    }
    let result = await articleListService(param)
    total.value = result.data.total
    articles.value = result.data.items
    
    for(let i = 0; i < articles.value.length; i++) {
        let article = articles.value[i]
        for (let j = 0; j < categorys.value.length; j++) {
            if(article.categoryId === categorys.value[j].id) {
                article.categoryId = categorys.value[j].categoryName
            }
        }
    }
}


articleCategoryList()
articleList()

import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import {Plus} from '@element-plus/icons-vue'
//控制抽屉是否显示
const visibleDrawer = ref(false)
const visibleBtn = ref(true)
//添加表单数据模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content:'',
    state:''
})
const clearForm = () => {
    articleModel.value.title = ''
    articleModel.value.categoryId = ''
    articleModel.value.coverImg = ''
    articleModel.value.content = ''
}

import { ElMessage,ElMessageBox } from 'element-plus'
const articleAdd = async (clickState) => {
    articleModel.value.state = clickState
    let res = await articleAddService(articleModel.value)
    console.log(res)
    ElMessage.success('添加成功')
    visibleDrawer.value = false
    clearForm()
    articleList()
}

import { useTokenStore } from '@/pinia/token.js';
const tokenStore = useTokenStore()

const uploadSuccess = (res) => {
    articleModel.value.coverImg = res.data;
    console.log(res.data)
}

const title = ref('');
const check = ref('');

const showDialog = (row,titles) => {
    visibleDrawer.value = true;
    title.value = titles
    //将当前行的数据赋值给articleModel
    articleModel.value.title = row.title;
    articleModel.value.categoryId = row.categoryId;
    articleModel.value.coverImg = row.coverImg;
    articleModel.value.content = row.content;
    articleModel.value.state = row.state;
    articleModel.value.id = row.id;

    if(titles === '查看日记') {
        visibleBtn.value = false
    } else{
        visibleBtn.value = true
    }
}

import { articleDeleteService, articleUpdateService } from '@/api/article.js'
const updateArticle = async (clickState)=>{
    articleModel.value.state = clickState
    for (let i = 0; i < categorys.value.length; i++) {
        if (articleModel.value.categoryId === categorys.value[i].categoryName) {
            articleModel.value.categoryId = categorys.value[i].id
        }
    }
    let result = await articleUpdateService(articleModel.value);
    ElMessage.success('修改成功')
    //隐藏弹窗
    visibleDrawer.value = false
    //再次访问后台接口，查询所有分类
    clearForm()
    articleList()
}

//删除分类
const deleteArticle= async (row) => {
    ElMessageBox.confirm('此操作将永久删除该日记, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async() => {
        let result = await articleDeleteService(row.id);
        ElMessage.success(result.message? result.message:'删除成功')
        //再次访问后台接口，查询所有文章
        articleList()
    }).catch(() => {
        ElMessage.info('已取消删除')
    });
}

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>日记管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer=true;title='添加日记'">添加日记</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="日记分类：">
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
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId='';state=''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="日记标题" width="400" prop="title">
                <template #default="{ row }">
                    <el-link :underline="false" type="primary" @click="showDialog(row,'查看日记') ">{{ row.title }}</el-link>
                </template>
            </el-table-column>
            <el-table-column label="分类" prop="categoryId"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row,'编辑日记')"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)" ></el-button>
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
        <el-drawer v-model="visibleDrawer" :title="title" direction="rtl" size="60%">
            <!-- 添加文章表单 -->
            <el-form :model="articleModel" label-width="100px" >
                <el-form-item label="日记标题"  >
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>  
                <el-form-item label="日记分类">
                    <el-select placeholder="请选择" v-model="articleModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="日记封面">
                    <!-- 上传图片
                        auto-upload: 是否自动上传
                        action: 上传的地址
                        name: 上传的文件字段名
                        headers: 设置上传的请求头部(tokens)
                        :on-success: 上传成功的回调 
                    -->


                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false"
                        action="/api/upload" 
                        name="file" 
                        :headers="{'Authorization':tokenStore.getToken()}" 
                        :on-success="uploadSuccess"
                        :disabled="!visibleBtn">
                        <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon"></el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="日记内容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html" :readOnly="!visibleBtn"></quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button v-if="visibleBtn" type="primary" @click="title=='添加日记'?articleAdd('已发布'):updateArticle('已发布')">发布</el-button>
                    <el-button v-if="visibleBtn" type="info" @click="title=='添加日记'?articleAdd('草稿'):updateArticle('草稿')">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
.avatar-uploader {
    :deep() {
        .avatar {
            width: 200px;
            height: 300px;
            display: block;
        }
/*
        .el-upload {
            
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
            
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }
*/

        .el-icon.avatar-uploader-icon {
            width: 300px;
            height: 178px;
            background: url('@/assets/cover.jpg') no-repeat center;
            background-size: cover; /* 或者使用 contain，根据需要 */
            border: 2px solid #8c939d; /* 添加边框 */
            box-sizing: border-box; /* 确保边框不会影响容器的尺寸 */
            outline: none;
        }
    }
}
.editor {
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>