<template>
  <div class="content-box">
    <div class="content">
      <div class="commentPagecontent">
        <div class="container">
          <div class="handle-box">
            <el-input placeholder="筛选评论" v-model="searchWord"></el-input>
            <el-select v-model="value" placeholder="请选择筛选方式">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            <el-button type="primary" icon="el-icon-search" @click="searchByWord()">搜索</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteAll()">批量删除</el-button>
          </div>

          <el-table height="500px" border size="small" :data="tableData" :header-cell-style="{color:'#848484', fontSize: '13px', backgroundColor: '#qua'}" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column label="ID" prop="commentNid" width="50" align="center"></el-table-column>
            <el-table-column label="内容" align="center">
              <template v-slot="scope">
                <p style="height: 50px; overflow: scroll">
                  {{ scope.row.commentContent }}
                </p>
              </template>
            </el-table-column>

            <el-table-column label="用户" width="120" align="center">
              <template v-slot="scope">
                <el-tag type="warning" v-if="scope.row.userName === '已注销'">{{ scope.row.userName }}</el-tag>
                <div v-else>{{ scope.row.userName }}</div>
              </template>
            </el-table-column>
            <el-table-column label="相关文章" width="200" align="center">
              <template v-slot="scope">
                <a :href="'/article/'+scope.row.articleId" target="_blank">{{ scope.row.articleTitle }}</a>
              </template>
            </el-table-column>
            <el-table-column label="根评论" width="200" align="center">
              <template v-slot="scope">
                <el-tag type="success" v-if="scope.row.rootComment === 'root'">{{ scope.row.rootComment }}</el-tag>
                <div v-else>{{ scope.row.rootComment }}</div>
              </template>
            </el-table-column>
            <el-table-column label="父评论"  width="200" align="center">
              <template v-slot="scope">
                <el-tag type="success" v-if="scope.row.parentComment === 'root'">{{ scope.row.parentComment }}</el-tag>
                <div v-else>{{ scope.row.parentComment }}</div>
              </template>
            </el-table-column>

            <el-table-column label="点赞数" prop="starCount" width="80" align="center"></el-table-column>
            <el-table-column label="评论数" prop="commentCount" width="80" align="center"></el-table-column>
            <el-table-column label="创建时间"  width="160" align="center">
            <template #default="scope">
              {{ scope.row.createDate|formatDate }}
            </template>
            </el-table-column>

            <el-table-column label="操作" width="120" align="center">
              <template v-slot="scope">
                <el-button type="text" icon="el-icon-delete" class="red" @click="deleteRow(scope.row.commentNid)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination class="pagination" background  :current-page="queryInfo.pagenum"
            :page-size="queryInfo.pagesize" :total="total" @current-change="handleCurrentChange">
          </el-pagination>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { getCommnetList, deleteComment, deleteComments, getCommentListBySearchWord } from "@/api/comment.js"
import { Message } from "element-ui";
import {formatDate} from "../../utils/time"
export default {
  data() {
    return {
      searchWord: "",
      multipleSelection: [],
      options: [{
        value: '选项1',
        label: '内容'
      }, {
        value: '选项2',
        label: '用户'
      }, {
        value: '选项3',
        label: '相关文章'
      }],
      value: '',
      tableData: [],


      delVisible: false,
      total: 1,//数据条数
      queryInfo: {
        // 当前页数
        pagenum: 1,
        // 显示一页多少条数据
        pagesize: 7
      }
    }
  },
  methods:
  {
    getData() {
      getCommnetList(this.queryInfo)
        .then((res) => {
          this.tableData = res.data.list;
          this.total = res.data.total;
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
    deleteAll() {
      deleteComments(this.multipleSelection)
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
    handleSelectionChange(val) {
      this.multipleSelection = val;

    },
    handleCurrentChange(newPage) {
      //把最新的页码（newPage）赋值给 动态的 pagenum 
      this.queryInfo.pagenum = newPage
      
      //获取到最新显示的页码值  重新发送axios请求 这里是封装好的请求方法
      if(this.searchWord===""){
        this.getData()
      }
      else{
        this.searchByWord()
      }
    },

    deleteRow(id) {
      deleteComment(id)
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

      getCommentListBySearchWord(temp)
        .then((res) => {
          console.log(res);
          this.tableData = res.data.list;
          this.total = res.data.total;
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
    searchByWord() {
      if (this.searchWord === "") {
        this.getData();
      } else {
        this.getDataBySearchWord();
      }
    }

  },
  computed:
  {

  },
  watch:
  {
  },

  created() {
    this.getData();
  }

}

</script>

<style src="../../assets/css/commentPage/commentPage.css"></style>
