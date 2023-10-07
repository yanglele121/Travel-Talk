<template>
  <div class="content-box">
    <div class="content">
      <div class="articlePagecontent">
        <div class="container">
          <div class="handle-box">
            <el-input placeholder="筛选文章" v-model="searchWord"></el-input>
            <el-select v-model="value" placeholder="请选择筛选方式">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            <el-button type="primary" icon="el-icon-search" @click="searchByWord()">搜索</el-button>
            <el-button type="danger" @click="deleteAll()">批量删除</el-button>
          </div>
          <el-table height="500px" border size="small" :data="tableData"
            :header-cell-style="{ color: '#848484', fontSize: '13px', backgroundColor: '#qua' }"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center"></el-table-column>
            <el-table-column label="ID" prop="articleNid" width="60" align="center"></el-table-column>
            <el-table-column label="文章封面" width="110" align="center">
              <template #default="scope">
                <el-image class="table-td-thumb" :src="scope.row.coverUrl" :z-index="10"
                  :preview-src-list="[scope.row.coverUrl]" preview-teleported>
                </el-image>
              </template>
            </el-table-column>
            <el-table-column label="标题" prop="articleTitle" width="120" align="center"></el-table-column>
            <el-table-column label="作者" prop="articleAuthor" width="100" align="center"></el-table-column>
            <el-table-column label="摘要" width="160" align="center">
              <template v-slot="scope">
                <p style="height: 80px; overflow: scroll">
                  {{ scope.row.articleAbstract }}
                </p>
              </template>
            </el-table-column>
            <el-table-column label="观看数" prop="lookCount" width="80" align="center"></el-table-column>
            <el-table-column label="点赞数" prop="starCount" width="80" align="center"></el-table-column>
            <el-table-column label="收藏数" prop="collectedCount" width="80" align="center"></el-table-column>
            <el-table-column label="评论数" prop="commentCount" width="80" align="center"></el-table-column>

            <el-table-column label="审核状态" width="100" align="center">
              <template v-slot="scope">
                <el-switch v-model="scope.row.articleStatus" active-color="#13ce66" inactive-color="#ff4949"
                  :active-value=1 :inactive-value=0 @change="editArticleStatus(scope.row)">
                </el-switch>
              </template>

            </el-table-column>

            <el-table-column label="推荐状态" width="100" style="align=center">
              <template v-slot="scope">
                <el-switch v-model="scope.row.articleRecommend" active-color="#13ce66" inactive-color="#ff4949"
                  :active-value=1 :inactive-value=0 @change="editRecommend(scope.row)">
                </el-switch>
              </template>

            </el-table-column>

            <el-table-column label="操作" width="220" align="center">
              <template v-slot="scope">
                <el-button type="text" class="blue" icon="el-icon-document" @click="detail(scope.row)">详情</el-button>
                <el-button type="text" class="blue" icon="el-icon-edit-outline" @click="feedback(scope.row)">反馈</el-button>
                <el-button type="text" class="red" icon="el-icon-delete"
                  @click="deleteRow(scope.row.articleNid)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination class="pagination" background :current-page="currentPage" :page-size="queryInfo.pagesize"
            :total="total" @current-change="handleCurrentChange">
          </el-pagination>
          <el-dialog class="advice" title="审核意见" :visible.sync="dialogVisible" width="30%">
            <label for="reason" style="padding-left: 5px;">审核未通过原因</label>
            <el-select style="width:100%; margin-top: 10px; margin-bottom: 10px;" v-model="reasons" multiple filterable default-first-option placeholder="请选择未通过原因">
              <el-option v-for="item in reason_options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            <label for="advice" style="padding-left: 5px; margin-top: 10px;">修改意见</label>
            <el-input v-model="advice" style="width: 100%; margin-top: 10px;" placeholder="请输入修改意见"></el-input>
            <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="sendMsg()">发 送</el-button>
            </span>
          </el-dialog>
        </div>
      </div>
    </div>
    <article-dia ref="dia" />
  </div>
</template>

<script>
import { getArticleList, deleteArticle, deleteArticles, getArticleListBySearchWord, updateArticle } from "@/api/article.js"
import { Message } from "element-ui";
import ArticleDia from "./ArticleDia.vue"
import { sendAuditOpinion } from "@/api/notification"
import { selectUserByName } from "@/api/user"
export default {
  components: { ArticleDia },
  name: 'article_page',
  data() {
    return {
      advice:'',
      reason_options: [{
          value: '政治宗教',
          label: '政治宗教'
        }, {
          value: '封建迷信',
          label: '封建迷信'
        },{
          value: '低俗色情',
          label: '低俗色情'
        },{
          value: '暴力恐怖',
          label: '暴力恐怖'
        },{
          value: '广告推广',
          label: '广告推广'
        }],

      reasons: [],
      currentArticle:{},
      msg:{
        title:"",
        content: "",
        relatedArticle: "",
        userId: "",
      },
      searchWord: "",
      multipleSelection: [],
      dialogVisible: false,
      options: [{
        value: '选项1',
        label: '标题'
      }, {
        value: '选项2',
        label: '作者'
      }, {
        value: '选项3',
        label: '审核状态'
      }, {
        value: '选项4',
        label: '推荐状态'
      },],
      value: '',
      tableData: [],

      picUrl: "",//文章封面上传url

      delVisible: false,
      pageSize: 5,
      currentPage: 1,



      total: 0,//数据条数
      queryInfo: {
        // 当前页数
        pagenum: 1,
        // 显示一页多少条数据
        pagesize: 5
      }

    }
  },
  methods:
  {
    getData() {
      getArticleList(this.queryInfo)
        .then((res) => {
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
          // this.$router.push("/404")
        });
    },

    handleSelectionChange(val) {

      this.multipleSelection = val;
    },
    handleCurrentChange(newPage) {
      //把最新的页码（newPage）赋值给 动态的 pagenum 
      this.queryInfo.pagenum = newPage

      //获取到最新显示的页码值  重新发送axios请求 这里是封装好的请求方法
      if (this.searchWord === "") {
        this.getData()
      }
      else {
        this.searchByWord()
      }
    },
    deleteAll() {

      deleteArticles(this.multipleSelection)
        .then((res) => {
          this.getData();
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

    },

    handleImgSuccess() {

    },
    uploadUrl() {

    },

    async editArticleStatus(row){
      this.editStatus(row)
      if(row.articleStatus===1&&row.articleRecommend===0){
        await selectUserByName(row.articleAuthor)
          .then((res)=>{
            this.msg.userId = res.data.userNid
          })
          this.msg.title= "文章审核通知"
          this.msg.content="您的文章《"+row.articleTitle+"》已经通过审核",
          this.msg.relatedArticle=row.articleTitle,
          
          sendAuditOpinion(this.msg)
        }
    },
    async editRecommend(row){
      this.editStatus(row)
      if(row.articleRecommend===1&&row.articleStatus===1){
        await selectUserByName(row.articleAuthor)
          .then((res)=>{
            this.msg.userId = res.data.userNid
          })
          this.msg.title= "文章推荐通知"
          this.msg.content="您的文章《"+row.articleTitle+"》已经被推荐为热门",
          this.msg.relatedArticle=row.articleTitle,
          sendAuditOpinion(this.msg)
        }
    },

    async editStatus(row) {
     await updateArticle(row)
        .then((res) => {
          // this.getData();
          Message({
            message: res.data,
            type: "success"
          })

        })
        .catch((err) => {
          // this.getData();
          Message({
            message: err,
            type: "error"
          })
        })
    },




    deleteRow(id) {

      deleteArticle(id)
        .then((res) => {
          this.getData();
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
    },
    getDataBySearchWord() {
      //console.log(this.queryInfo)
      let temp = {
        pagenum: 1,
        pagesize: 5,
        searchword: "",
        value: '',
      }
      temp.pagenum = this.queryInfo.pagenum;
      temp.pagesize = this.queryInfo.pagesize;
      temp.searchword = this.searchWord;

      if (this.value == "") {
        temp.value = "选项1";
      }
      else {
        temp.value = this.value;
      }

      getArticleListBySearchWord(temp)
        .then((res) => {
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
    searchByWord() {
      if (this.searchWord === "") {
        this.getData();
      } else {
        this.getDataBySearchWord();

      }
    },
    detail(article) {
      this.$refs.dia.open(article);
    },
    feedback(currentArticle) {
      this.dialogVisible = true
      this.currentArticle = currentArticle
    },
    async sendMsg(){
      const res = await selectUserByName(this.currentArticle.articleAuthor)
      let id = res.data.userNid
      this.msg.title= this.reasons.toString()
      this.msg.content=this.advice,
      this.msg.relatedArticle=this.currentArticle.articleTitle,
      this.msg.userId=id
      const result = await sendAuditOpinion(this.msg)
      if(result.data===1){
        this.$notify({
          title: '反馈成功',
          message: '已成功向用户反馈您的修改建议',
          type: 'success'
        });
        this.dialogVisible=false
        this.reasons=[],
        this.advice=""
      }
      else{
        this.$notify({
          title: '反馈失败',
          message: '请稍后重试',
          type: 'danger'
        });
      }
      
    }
  },
  created() {
    this.getData();
  }

}

</script>

<style src="../../assets/css/articlePage/articlePage.css">
.site-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}
</style>
