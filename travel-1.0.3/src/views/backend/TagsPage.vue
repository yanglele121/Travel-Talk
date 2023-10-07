<template>
    <div class="content-box">
        <div class="content">
            <div class="SitePagecontent">
                <div class="container">
                    <div class="handle-box">
                        <el-button type="primary" icon="el-icon-plus" @click="centerDialogVisible = true">添加标签</el-button>
                    </div>
                    <el-table height="500px"  size="medium" :data="tableData"
                        :header-cell-style="{ color: '#848484', fontSize: '13px', backgroundColor: '#qua' }">
                        <el-table-column label="排序">
                            <template #default="scope">
                                <el-tag class="myfirst" v-if="scope.$index===0">{{ scope.$index+1 }}</el-tag>
                                <el-tag class="second" v-else-if="scope.$index===1">{{ scope.$index+1 }}</el-tag>
                                <el-tag class="third" v-else-if="scope.$index===2">{{ scope.$index+1 }}</el-tag>
                                <el-tag class="other" v-else>{{ scope.$index+1 }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="ID" prop="tagArticleNid" align="center"></el-table-column>
                        <el-table-column label="标签名" prop="articleTag" align="center"></el-table-column>
                        
                        <el-table-column label="关联文章数量"  align="center" prop="articleCount">
                            
                        </el-table-column>
                        <el-table-column label="操作" width="220" align="center">
                            <template v-slot="scope">
                                <el-button type="text" icon="el-icon-edit" @click="editRow(scope.row)">编辑</el-button>
                                <el-button type="text" class="red" icon="el-icon-delete" @click="deleteRow(scope.row)">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>

                </div>
                <!-- 添加 -->
                <el-dialog title="添加标签" :visible.sync='centerDialogVisible'>
                    <el-form ref="addtag" label-width="80px" :model="tagForm" :rules="tagRule">
                        <el-form-item label="标签" prop="articleTag">
                            <el-input v-model="tagForm.articleTag"></el-input>
                        </el-form-item>
                    </el-form>
                    <template #footer>
                        <span class="dialog-footer">
                            <el-button @click="centerDialogVisible = false">取 消</el-button>
                            <el-button type="primary" @click="pageaddsite('addtag')">确 定</el-button>
                        </span>
                    </template>
                </el-dialog>

                <!-- 编辑弹出框 -->

                <el-dialog class="editContent" title="编辑" :visible.sync='editVisible'>

                    <el-form ref="edittag" label-width="80px" :model="editForm" :rules="tagRule">

                        <el-form-item label="标签名" prop="articleTag">
                            <el-input v-model="editForm.articleTag"></el-input>
                        </el-form-item>
                    </el-form>
                    <template #footer>
                        <span class="dialog-footer">
                            <el-button @click="editVisible = false">取 消</el-button>
                            <el-button type="primary" @click="saveEdit('edittag')">确 定</el-button>
                        </span>
                    </template>

                </el-dialog>

            </div>
        </div>


    </div>
</template>
  
<script>
import { ArticleTags,newtag,updateTag,deleteTag } from "../../api/tag";
import { Message } from "element-ui";
import  request  from "@/request"
export default {

    data() {
        return {
            baseURL:"",
            tableData: [],
            centerDialogVisible: false,
            editVisible: false,

            tagForm: {
                articleTag:"",
            },
            tagRule: {
                articleTag: [{
                    required: true,
                    message: "标签名为必填项",
                    trigger: "blur",
                }],
            },
            editForm: {
                tagArticleNid:"",
                articleTag:"",
            },
        }
    },
    methods:
    {
        async getData() {
            // this.clearForm()
            await ArticleTags()
                .then((res) => {
                    // console.log(res);
                    this.tableData = res.data
                })
                .catch((err) => {
                    Message({
                        message: err,
                        type: "error"
                    })
                });
        },
        editRow(row) {
            this.editVisible = true;
            this.editForm.tagArticleNid=row.tagArticleNid
            this.editForm.articleTag=row.articleTag
            // console.log(this.editForm);
        },
        saveEdit(formName)//保存修改内容
        {
            //向后端发送请求 再从新得到数据
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    updateTag(this.editForm)
                        .then((res) => {
                            // console.log(res);
                            this.getData();
                            this.editVisible = false;
                            Message({
                                message: res.data,
                                type: "success"
                            })

                        })
                        .catch((err) => {
                            this.getData();
                            Message({
                                message: err,
                                type: "error"
                            })
                        })

                }
            });
        },
        pageaddsite(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    newtag(this.tagForm).
                    then((res)=>{
                        if (res.success) {
                        this.getData();
                        this.centerDialogVisible = false;
                        this.clearForm()
                        Message({
                            type: "success",
                            message: res.data
                        })
                    }
                    }).catch(error=>{
                        Message({
                            type: 'error',
                            message: error
                        })
                        this.clearForm()
                    })
                }
            });

        },
        async deleteRow(row){
            try{
                const res =await deleteTag(row)
                this.getData()
                Message({
                    type:"success",
                    message:res.data
                })
            }catch(err){
                Message({
                    type:"error",
                    message:err
                })   
            }

        },
        clearForm() {
                this.tagForm.articleTag=""
        }




    },
    created() {
        this.baseURL = request.defaults.baseURL;
        this.getData();
    }

}

</script>
  
<style src="../../assets/css/sitePage/sitePage.css">



</style>
  