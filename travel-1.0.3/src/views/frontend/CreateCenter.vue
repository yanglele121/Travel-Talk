<template>
    <div class="wrapper">
        <div class="fn-50">
            <img src="../../assets/img/创作中心.png">
        </div>
        <el-tabs type="border-card">
            <el-tab-pane label="写文章">
                <el-button type="primary" @click="dialog = true">完善文章信息</el-button>
                <el-drawer title="文章信息" :visible.sync="dialog" direction="rtl" custom-class="demo-drawer" ref="drawer">
                    <div class="demo-drawer__content">
                        <el-form :model="articleForm" :rules="ruleForm" ref="addFrom">
                            <el-form-item label="文章标题" :label-width="formLabelWidth" prop="articleTitle">
                                <el-input v-model="articleForm.articleTitle" autocomplete="on"></el-input>
                            </el-form-item>
                            <el-form-item label="文章摘要" :label-width="formLabelWidth" prop="articleAbstract">
                                <el-input type="textarea" placeholder="请输入文章简介" :rows="4" resize="none"
                                    v-model="articleForm.articleAbstract"></el-input>
                            </el-form-item>
                            <el-form-item label="关联景点" :label-width="formLabelWidth" prop="siteId">
                                <el-select v-model="articleForm.siteId" filterable placeholder="请选择">
                                    <el-option v-for="item in siteInfo" :key="item.siteNid" :label="item.siteName"
                                        :value="item.siteNid">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="相关标签" :label-width="formLabelWidth">
                                <el-select v-model="tagList" multiple filterable placeholder="请选择相关标签">
                                    <el-option v-for="tagitem in articleTags" :key="tagitem.tagArticleNid"
                                        :label="tagitem.articleTag" :value="tagitem.tagArticleNid">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="上传封面" :label-width="formLabelWidth" required>
                                <el-upload :action="baseURL+'/uploadCover'" list-type="picture-card"
                                    :auto-upload="true" :on-preview="handlePictureCardPreview" :on-remove="handleRemove"
                                    :on-success="handleSuccess" :limit="1" :file-list="fileList">
                                    <div slot="res" slot-scope="{res}">
                                        <i slot="default" class="el-icon-plus"></i>
                                        <img class="el-upload-list__item-thumbnail" :src="res.response.data" alt="">
                                        <span class="el-upload-list__item-actions">
                                            <span class="el-upload-list__item-preview"
                                                @click="handlePictureCardPreview(res)">
                                                <i class="el-icon-zoom-in"></i>
                                            </span>
                                            <span v-if="!disabled" class="el-upload-list__item-delete"
                                                @click="handleRemove(res)">
                                                <i class="el-icon-delete"></i>
                                            </span>
                                        </span>
                                    </div>
                                </el-upload>
                                <el-dialog :visible.sync="dialogVisible">
                                    <img width="100%" :src="dialogImageUrl" alt="">
                                </el-dialog>
                            </el-form-item>

                        </el-form>
                        <div class="demo-drawer__footer">
                            <el-button @click="cancelForm">取 消</el-button>
                            <el-button type="success" @click="add_article('addFrom')">发 布 文 章</el-button>
                        </div>
                    </div>
                </el-drawer>
                <!-- <div class="fn-50"> -->
                <!-- </div> -->
                <div id="vditor">
                </div>
            </el-tab-pane>
            <el-tab-pane label="发圈子">
                <el-button type="primary" @click="submitForm('addForm1')">记录好心情 ~</el-button>
                <el-form :model="ruleForm1" status-icon :rules="rules" ref="addForm1" label-width="100px"
                    class="demo-ruleForm">
                    <el-form-item label="写点什么吧" prop="picalbumContent">
                        <el-input type="textarea" v-model="ruleForm1.picalbumContent" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="上传图片" required>
                        <el-upload
                            class="upload-demo"
                            ref="upload"
                            :action="baseURL+'/uploadAlbumPic'"
                            :file-list="fileListPic"
                            :on-success="handleImgSuccess"
                            :limit="1" 
                            list-type="picture">
                            <el-button size="small" type="primary">点击上传</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                            </el-upload>
                    </el-form-item>
                </el-form>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>
import Vditor from "vditor"
import { Message } from "element-ui";
import { publishArticle } from "@/api/article"
import { AllSite } from "@/api/site";
import { ArticleTags } from "@/api/tag"
import { NewPicAlbum } from "@/api/picAlbum"
import request from "@/request"
import "vditor/dist/index.css"
export default {
    
    props: {
        value: {
            type: String,
            default: ''
        },
        id: {
            type: String,
            required: false,
            default() {
                return 'markdown-editor-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
            }
        },
        isHideTools: {
            type: Boolean,
            required: false,
            default: false
        },
        isPin: {
            type: Boolean,
            required: false,
            default: true
        },
        height: {
            type: String,
            required: false,
            default: '600px'
        },
        width: {
            type: String,
            required: false,
            default: "auto"
        },
        mode: {
            type: String,
            required: false,
            default: 'ir'
        },
    },
    data() {
        return {
            baseURL:"",
            contentEditor: "",
            dialog: false,
            fileList: [],
            articleForm: {
                articleTitle: "",
                coverUrl: "",
                articleAbstract: "",
                articleContent: "",
                siteId: "",

            },
            ruleForm: {
                articleTitle: [{ required: true, message: '请输入标题', trigger: 'blur' },
                { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }],
                siteId: [{ required: true, message: '请选择关联景点', trigger: 'blur' }],
                articleAbstract: [{ required: true, message: '请输入文章摘要', trigger: 'blur' }],

            },
            formLabelWidth: '80px',
            timer: null,
            dialogImageUrl: '',
            dialogVisible: false,
            disabled: false,
            siteInfo: [],
            articleTags: [],
            tagList: [],
            ruleForm1: {
                userId:"",
                picalbumContent: '',
                imgUrl:'',
            },
            rules: {
            picalbumContent: [
               {required: true, message:"请输入内容", trigger: 'blur'},
            ]
            },
            fileListPic: []
        }
    },
    //mounted
    mounted() {
        this.contentEditor = new Vditor("vditor", {
            height: this.height,
            width: this.width,
            toolbarConfig: {
                pin: this.isPin,
                hide: this.isHideTools,
            },
            cache: {
                enable: false
            },
            counter: {
                enable: true,
            },
            toolbar: [
                "emoji",
                "headings",
                "bold",
                "italic",
                "strike",
                "link",
                "|",
                "list",
                "ordered-list",
                "check",
                "outdent",
                "indent",
                "|",
                "quote",
                "line",
                "code",
                "inline-code",
                "insert-before",
                "insert-after",
                "|",
                { //自定义上传
                    hotkey: "",
                    name: "upload",
                    // tipPosition: "s",
                    tip: "上传图片",
                    className: "right",
                },
                "table",
                "|",
                "undo",
                "redo",
                "|",
                // "fullscreen",
                "edit-mode",
                {
                    name: "more",
                    toolbar: [
                        "both",
                        "code-theme",
                        "content-theme",
                        "export",
                        "outline",
                        "preview",
                        // "devtools",
                        // "info",
                        "help",
                    ],
                },
            ],
            after: () => {
                this.contentEditor.setValue(this.value)
            },
            mode: this.mode,
            preview: {
                mode: "both",
                actions: []
            },
            //这里写上传
            upload: {
                accept: 'image/jpg, image/jpeg, image/png',//规定上传的图片格式
                url: this.baseURL + "/uploadPicture",//请求的接口
                multiple: false,
                fieldName: 'file',
                max: 2 * 1024 * 1024,//上传图片的大小
                // extraData: { 'access_token': this.token }, //为 FormData 添加额外的参数
                linkToImgUrl: this.baseURL + "/uploadPicture",
                filename(name) {
                    return name.replace(/[^(a-zA-Z0-9\u4e00-\u9fa5\.)]/g, "")
                        .replace(/[\?\\/:|<>\*\[\]\(\)\$%\{\}@~]/g, "")
                        .replace("/\\s/g", "");
                },
                validate(files) {
                    const isLt2M = files[0].size / 1024 / 1024 < 2
                    if (!isLt2M) {
                        Message({
                            message: "上传图片大小不能超过 10MB!",
                            type: 'error',
                        })
                        //this.$message.error('')
                    }
                },
                //上传图片回显处理
                format(files, responseText) {
                    var self = this;
                    var data = JSON.parse(responseText)
                    //上传图片请求状态
                    if (data.success) {
                        let filName = data.data
                        let lastTipNum = filName.substr(filName.lastIndexOf('/', filName.lastIndexOf('/') - 1) + 1);
                        console.log("lastTipNum", lastTipNum)
                        let index = lastTipNum.lastIndexOf("\/");
                        let succ = {}
                        succ[filName] = data.data
                        //图片回显
                        return JSON.stringify({
                            data,
                            data,
                            data: {
                                errFiles: [],
                                succMap: succ
                            }
                        })
                    } else {
                        Message({
                            message: "图片上传失败",
                            type: 'error',
                        })
                    }

                },
                error(msg) {
                    console.log(msg + "上传失败了")
                },
            }
        });
        this.Init();
    },
    methods: {
        Init() {
            AllSite()
                .then(res => {
                    if (res.success) {
                        this.siteInfo = res.data
                    }
                    else {
                        Message({
                            message: res.msg,
                            type: 'error'
                        })
                    }
                }),
                ArticleTags()
                    .then(res => {
                        if (res.success) {
                            this.articleTags = res.data
                        }
                        else {
                            Message({
                                message: res.msg,
                                type: 'error'
                            })
                        }
                    })
        },
        handleSelect(key, keyPath) {
            console.log(key, keyPath);
        },
        getValue() {
            return this.contentEditor.getValue();     //获取 Markdown 内容
        },
        getHTML() {
            return this.contentEditor.getHTML();      //获取 HTML 内容
        },
        setValue(value) {
            return this.contentEditor.setValue(value);     //设置 Markdown 内容
        },
        cancelForm() {
            this.dialog = false;
        },
        handleRemove(res) {
            console.log(res);
        },
        handlePictureCardPreview(res) {
            this.dialogImageUrl = res.url
            this.dialogVisible = true;
        },
        handleImgSuccess(res){
            this.fileListPic=[]
            this.ruleForm1.imgUrl = res.data;
            this.fileListPic.push({
                'url': res.data
            })
        },
        handleSuccess(res) {
            this.fileList = []
            this.articleForm.coverUrl = res.data;
            this.fileList.push({
                'url': res.data
            })
        },
        async add_article(formName) {
            try {
                const valid = await new Promise(resolve => {
                this.$refs[formName].validate(valid => {
                        resolve(valid);
                    });
                });
                if (!valid) {
                    return;
                }
                if (this.getValue() === '\n') {
                    Message.warning('文章内容不能为空');
                    return;
                }
                if (this.articleForm.coverUrl === '') {
                    Message.warning('需要上传文章封面');
                    return;
                }
                this.articleForm.articleContent = this.getValue();
                const res = await publishArticle(this.articleForm, this.$store.state.token, this.tagList);
                if (res.success) {
                    Message.success(res.data);
                    this.articleForm = {
                        articleAbstract: '',
                        articleTitle: '',
                        articleContent: '',
                        siteId: '',
                        coverUrl: '',
                    };
                    this.tagList=[]
                    this.fileList = [];
                    this.setValue('');
                    this.$refs.drawer.closeDrawer();
                } else {
                    Message.error(res.msg);
                }
            } catch (error) {
                console.error(error);
                Message.error('提交文章失败');
            }
        },
        handleRemovePic(file, fileList) {
            console.log(file, fileList);
        },
        handlePreviewPic(file) {
            console.log(file);
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
            if (valid) {
                this.ruleForm1.userId = this.$store.state.id
                NewPicAlbum(this.ruleForm1,this.$store.state.token)
                .then(res=>{
                    if(res.success){
                        Message({
                            type:'success',
                            message: res.data
                        })
                        this.$refs.upload.clearFiles();
                        this.ruleForm1 = {
                            userId:"",
                            picalbumContent: '',
                            imgUrl:'',
                        }
                    }
                    else{
                        Message({
                            type: 'error',
                            message: res.msg
                        })
                    }
                })
            } else {
                console.log('error submit!!');
                return false;
            }
            });
        },
    },
    created(){
        this.baseURL = request.defaults.baseURL
    }
}
</script>
<style>
.wrapper {
    max-width: 1024px;
    margin: 0 auto;
    padding: 10px;
    flex: 1;
    width: 100%;
    box-sizing: border-box;
}

.fn-50 {
    height: 100px;
}

.vditor {
    display: flex;
    flex-direction: column;
    border: none;
    border-radius: 3px;
    box-sizing: border-box;
    margin-top: 10px;
}

.vditor-toolbar {
    border-top: 1px solid #d1d5da;
}

.demo-drawer__content {
    padding: 15px;
}

.demo-drawer__footer {
    display: flex;
    justify-content: right;
}

.el-select {
    width: 100%;
}
.demo-ruleForm {
    margin-top: 10px;
}
.el-upload--picture-card {
    width: 220px;
}
</style>