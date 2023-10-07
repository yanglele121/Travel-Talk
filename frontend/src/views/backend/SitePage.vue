<template>
  <div class="content-box">
      <div class="content">
  <div class="SitePagecontent">
    <div class="container">
      <div class="handle-box">
        <el-input placeholder="筛选景点" v-model="searchWord"></el-input>
        <el-select v-model="value" placeholder="请选择筛选方式">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="searchByWord()">搜索</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="centerDialogVisible = true">添加景点</el-button>
        <el-button type="danger" icon="el-icon-delete" @click="deleteAll()">批量删除</el-button>
      </div>
      <el-table height="500px" border size="small" :data="tableData" :header-cell-style="{color:'#848484', fontSize: '13px', backgroundColor: '#qua'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40" align="center"></el-table-column>
        <el-table-column label="ID" prop="siteNid" width="50" align="center"></el-table-column>
        <el-table-column label="景点图片"  width="110" align="center">
          <template #default="scope">
          <el-image class="table-td-thumb" :src="scope.row.sitePicUrl" :z-index="10"
            :preview-src-list="[scope.row.sitePicUrl]" preview-teleported>
          </el-image>
          <el-upload
              :action="baseURL+'/uploadSite'" 
              list-type="picture"
              :on-success="handleSuccess" :limit="1">
              <el-button size="small"  @click="changeCurrentSite(scope.row.siteNid)">更新图片</el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column label="景点" prop="siteName" width="120" align="center"></el-table-column>
        <el-table-column label="评分" prop="siteScore" width="120" align="center"></el-table-column>
        <el-table-column label="地区" prop="siteAddr" width="100" align="center"></el-table-column>
        <el-table-column label="官网" prop="siteUrl" width="170" align="center"></el-table-column>
        <el-table-column label="简介" prop="siteDesc">
          <template v-slot="scope">
            <p style="height: 100px; overflow: scroll">
              {{ scope.row.siteDesc }}
            </p>
          </template>
        </el-table-column>


        <el-table-column label="操作" width="220" align="center">
          <template v-slot="scope">
            <el-button type="text" icon="el-icon-edit" @click="editRow(scope.row)">编辑</el-button>
            <el-button type="text" class="red" icon="el-icon-delete" @click="deleteRow(scope.row.siteNid)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" background  :current-page="queryInfo.pagenum"
        :page-size="queryInfo.pagesize" :total="total" @current-change="handleCurrentChange">
      </el-pagination>

    </div>
    <!-- 添加 -->
    <el-dialog title="添加景点" center :visible.sync='centerDialogVisible'>
      <el-form ref="registerForm" label-width="80px" :model="registerForm" :rules="registeRules">
        <el-form-item label="景点名" prop="siteName">
          <el-input v-model="registerForm.siteName"></el-input>
        </el-form-item>
        <el-form-item label="地区" prop="siteAddr">
          <el-input v-model="registerForm.siteAddr"></el-input>
        </el-form-item>
        <el-form-item label="评分" prop="siteScore">
          <el-input v-model="registerForm.siteScore"></el-input>
        </el-form-item>
        <el-form-item label="官网" prop="siteUrl">
          <el-input v-model="registerForm.siteUrl"></el-input>
        </el-form-item>
        <el-form-item label="景点介绍" prop="siteDesc">
          <el-input type="textarea" v-model="registerForm.siteDesc"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="pageaddsite('registerForm')">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑弹出框 -->

    <el-dialog class="editContent" title="编辑" center :visible.sync='editVisible'>

      <el-form ref="registerForm" label-width="60px" :model="editForm" :rules="registeRules">

        <el-form-item label="景点" prop="siteName">
          <el-input v-model="editForm.siteName"></el-input>
        </el-form-item>

        <el-form-item label="地区" prop="siteAddr">
          <el-input v-model="editForm.siteAddr"></el-input>
        </el-form-item>

        <el-form-item label="评分" prop="siteScore">
          <el-input v-model="editForm.siteScore"></el-input>
        </el-form-item>

        <el-form-item label="官网" prop="siteUrl">
          <el-input v-model="editForm.siteUrl"></el-input>
        </el-form-item>

        <el-form-item label="简介" prop="siteDesc">
          <el-input type="textarea" v-model="editForm.siteDesc"></el-input>
        </el-form-item>

      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit('registerForm')">确 定</el-button>
        </span>
      </template>

    </el-dialog>

  </div>
      </div>


  </div>
</template>

<script>
import { getSiteList, addSite, deleteSite, deleteSites, updateSite, getSiteListBySearchWord,updateSiteUrl } from "@/api/site.js"
import { Message } from "element-ui";
import request from "@/request"
export default {

  data() {
    return {
      baseURL:"",
      searchWord: "",
      multipleSelection: [],
      options: [{
        value: '选项1',
        label: '景点名'
      }, {
        value: '选项2',
        label: '地区'
      }],
      value: '',
      tableData: [],
      tempData: [],
      picUrl: "",
      centerDialogVisible: false,
      editVisible: false,
      delVisible: false,
      total: 0,
      currentSiteNid : 0,
      registerForm: {

        siteName: "",
        siteScore: "",
        siteAddr: "",
        siteDesc: "",
        siteUrl: "",
      },
      registeRules: {
        siteName: [{
          required: true,
          message: "用户名为必填项",
          trigger: "blur",
        }],
        siteAddr: [{
          required: true,
          message: "地区为必填项",
          trigger: "blur",
        }],

        siteScore: [
          { required: true, message: '请输入评分', trigger: 'blur' }
        ],
        siteUrl: [{
          required: true,
          message: "官网为必填项",
          trigger: "blur",
        }],

      },
      editForm: {
        siteNid: "",
        siteName: "",
        siteScore: "",
        siteAddr: "",
        siteDesc: "",
        siteUrl: "",
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
    getData() {
      this.clearForm()
      getSiteList(this.queryInfo)
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
          this.$router.push("/404")
        });
    },

    deleteAll() {

      deleteSites(this.multipleSelection)
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
    changeCurrentSite(id){
      this.currentSiteNid = id;
    },
    //5-22
    handleSuccess(res) {
      updateSiteUrl({
        siteId : this.currentSiteNid,
        siteUrl : res.data,
      }).then((data)=>{
        this.getData();
        this.$forceUpdate();
      });
    },

    editRow(row) {
      this.editVisible = true;


      this.editForm.siteNid = row.siteNid;

      this.editForm.siteName = row.siteName;
      this.editForm.siteScore = row.siteScore;
      this.editForm.siteUrl = row.siteUrl;
      this.editForm.siteAddr = row.siteAddr;
      this.editForm.siteDesc = row.siteDesc;
      // console.log(this.editForm);
    },
    saveEdit(formName)//保存修改内容
    {

      //向后端发送请求 再从新得到数据


      this.$refs[formName].validate((valid) => {
        if (valid) {
          updateSite(this.editForm)
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
          addSite(this.registerForm)
            .then((res) => {
              // console.log(res);
              this.getData();
              this.centerDialogVisible = false;
              Message({
                message: res.data,
                type: "success"
              })

            })
            .catch((err) => {
              Message({
                message: err,
                type: "error"
              })
            })

        }
      });

    },



    deleteRow(id) {
      deleteSite(id)
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
      this.queryInfo.pagenum = this.currentPage;
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

      getSiteListBySearchWord(temp)
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
          // this.$router.push("/404")
        });
    },
    searchByWord() {
      if (this.searchWord === "") {
        this.getData();
      } else {
        this.getDataBySearchWord();
      }
      // console.log(this.tableData);
    },
    clearForm(){
      this.registerForm.siteName= "",
      this.registerForm.siteScore= "",
      this.registerForm.siteAddr="",
      this.registerForm.siteDesc= "",
      this.registerForm.siteUrl=""
    }




  },
  computed:
  {

  },
  watch:
  {

  },
  created() {
    this.baseURL = request.defaults.baseURL
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
