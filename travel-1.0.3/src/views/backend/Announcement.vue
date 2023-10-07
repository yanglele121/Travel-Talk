<template>
    <div class="content-box">
        <div class="content">
            <div class="SitePagecontent">
                <div class="container">
                    <div class="handle-box">
                        <el-button type="primary" icon="el-icon-plus" @click="centerDialogVisible = true">添加资讯</el-button>
                    </div>
                    <el-table height="500px" border size="small" :data="tableData"
                        :header-cell-style="{ color: '#848484', fontSize: '13px', backgroundColor: '#qua' }"
                        @selection-change="handleSelectionChange">
                        <el-table-column type="selection" width="40" align="center"></el-table-column>
                        <el-table-column label="ID" prop="announcementNid" width="50" align="center"></el-table-column>
                        <el-table-column label="资讯头图" width="110" align="center">
                            <template #default="scope">
                                <el-image class="table-td-thumb" :src="scope.row.coverUrl" :z-index="10"
                                    :preview-src-list="[scope.row.coverUrl]" preview-teleported>
                                </el-image>
                            </template>
                        </el-table-column>
                        <el-table-column label="标题" prop="announcementTitle" width="240" align="center"></el-table-column>
                        <el-table-column label="资讯来源" prop="announcementContent" align="center">
                            <template v-slot="scope">
                                <p style="height: 80px; overflow: scroll; line-height: 30px;">
                                    <a :href="scope.row.announcementContent" target="_blank">
                                        {{ scope.row.announcementContent }}
                                    </a>
                                </p>
                            </template>
                        </el-table-column>
                        <el-table-column label="创建时间" width="150" align="center" prop="createDate">
                            <template #default="scope">
                            {{ scope.row.createDate|formatDate }}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="110" align="center">
                            <template v-slot="scope">
                                <el-button type="text" icon="el-icon-edit" @click="editRow(scope.row)">编辑</el-button>
                            </template>
                        </el-table-column>
                    </el-table>

                    <el-pagination class="pagination" background :current-page="queryInfo.pagenum"
                        :page-size="queryInfo.pagesize" :total="total" @current-change="handleCurrentChange">
                    </el-pagination>

                </div>
                <!-- 添加 -->
                <el-dialog title="添加资讯" :visible.sync='centerDialogVisible'>
                    <el-form ref="addAnnouncement" label-width="80px" :model="AnnouncementForm" :rules="AnnouncementRules">
                        <el-form-item label="标题" prop="announcementTitle">
                            <el-input v-model="AnnouncementForm.announcementTitle"></el-input>
                        </el-form-item>
                        <el-form-item label="资讯来源" prop="announcementContent">
                            <el-input type="textarea" v-model="AnnouncementForm.announcementContent"></el-input>
                        </el-form-item>
                        <el-form-item label="上传头图" required>
                            <el-upload class="upload-demo" :action="baseURL+'/uploadAnnouncementCover'"
                                :file-list="fileList" :on-success="handleSuccess" list-type="picture">
                                <el-button size="small" type="primary">点击上传</el-button>
                            </el-upload>
                        </el-form-item>
                    </el-form>
                    <template #footer>
                        <span class="dialog-footer">
                            <el-button @click="centerDialogVisible = false">取 消</el-button>
                            <el-button type="primary" @click="pageaddsite('addAnnouncement')">确 定</el-button>
                        </span>
                    </template>
                </el-dialog>

                <!-- 编辑弹出框 -->

                <el-dialog class="editContent" title="编辑" :visible.sync='editVisible'>

                    <el-form ref="editAnnouncement" label-width="80px" :model="editForm" :rules="AnnouncementRules">

                        <el-form-item label="标题" prop="announcementTitle">
                            <el-input v-model="editForm.announcementTitle"></el-input>
                        </el-form-item>

                        <el-form-item label="详细资讯" prop="announcementContent">
                            <el-input type="textarea" v-model="editForm.announcementContent"></el-input>
                        </el-form-item>

                        <el-form-item label="头图">
                            <img :src="editForm.coverUrl" style="width: 50%;" alt="">
                            <el-upload :action="baseURL+'/uploadAnnouncementCover'" 
                                :show-file-list="false" 
                                :on-success="handleImgSuccess"
                                ref="upload"
                                :before-upload="beforeImgUpload()">
                                <el-button>更新图片</el-button>
                            </el-upload>
                        </el-form-item>
                    </el-form>

                    <template #footer>
                        <span class="dialog-footer">
                            <el-button @click="editVisible = false">取 消</el-button>
                            <el-button type="primary" @click="saveEdit('editAnnouncement')">确 定</el-button>
                        </span>
                    </template>

                </el-dialog>

            </div>
        </div>


    </div>
</template>
  
<script>
import { Announcement, NewAnnouncement, UpdateAnnouncement } from "@/api/announcement.js"
import { Message } from "element-ui";
import { formatDate } from "../../utils/time";
import  request  from "@/request"
export default {

    data() {
        return {
            baseURL:"",
            multipleSelection: [],
            tableData: [],
            fileList: [],
            tempData: [],
            picUrl: "",
            centerDialogVisible: false,
            editVisible: false,
            delVisible: false,
            total: 0,

            AnnouncementForm: {
                announcementContent: "",
                announcementTitle: "",
                coverUrl: ""
            },
            AnnouncementRules: {
                announcementContent: [{
                    required: true,
                    message: "资讯内容为必填项",
                    trigger: "blur",
                }],
                announcementTitle: [{
                    required: true,
                    message: "资讯标题为必填项",
                    trigger: "blur",
                }]

            },
            editForm: {
                announcementNid:"",
                announcementContent: "",
                announcementTitle: "",
                coverUrl: ""
            },
            queryInfo: {
                // 当前页数
                pagenum: 1,
                // 显示一页多少条数据
                pagesize: 5
            },
        }
    },
    methods:
    {
        async getData() {
            // this.clearForm()
            await Announcement(this.queryInfo)
                .then((res) => {
                    // console.log(res);
                    this.tableData = res.data.list;
                    this.total = res.data.total;
                    this.currentPage = res.data.pageNum;
                    this.tempData = this.tableData;
                })
                .catch((err) => {
                    Message({
                        message: err,
                        type: "error"
                    })
                });
        },
        async addAnnouncement() {

        },
        handleSuccess(res) {
            this.fileList = []
            this.AnnouncementForm.coverUrl = res.data;
            this.fileList.push({
                'url': res.data
            })
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;

        },

        handleCurrentChange(newPage) {
            //把最新的页码（newPage）赋值给 动态的 pagenum 
            this.queryInfo.pagenum = newPage
            this.getData()
            //获取到最新显示的页码值  重新发送axios请求 这里是封装好的请求方法
        },


        handleImgSuccess(res) {
            this.editForm.coverUrl=res.data
        },
        beforeImgUpload() {

        },
        uploadUrl() {

        },

        editRow(row) {
            this.editVisible = true;

            this.editForm.announcementNid = row.announcementNid
            this.editForm.announcementTitle = row.announcementTitle;
            this.editForm.announcementContent = row.announcementContent;
            this.editForm.coverUrl = row.coverUrl
            // console.log(this.editForm);
        },
        saveEdit(formName)//保存修改内容
        {
            //向后端发送请求 再从新得到数据
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    UpdateAnnouncement(this.editForm)
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
                    NewAnnouncement(this.AnnouncementForm).
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
                    else {
                        Message({
                            type: 'error',
                            message: res.msg
                        })
                    }
                    })
                }
            });

        },
        clearForm() {
                this.AnnouncementForm.announcementContent = "",
                this.AnnouncementForm.announcementTitle = "",
                this.AnnouncementForm.coverUrl=""
                console.log(this.$refs.upload)
                this.$refs.upload.clearFiles();
        }




    },
    created() {
        this.baseURL = request.defaults.baseURL;
        this.getData();
    }

}

</script>
  
<style src="../../assets/css/sitePage/sitePage.css">
.site-img {
    width: 100%;
    height: 80px;
    border-radius: 5px;
    margin-bottom: 5px;
    overflow: hidden;
}
</style>
  