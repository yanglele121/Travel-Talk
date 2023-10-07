<template>
  <div class="content-box">
    <div class="content">
  <div class="container">
    <div class="handle-box">
      <el-input v-model="searchWord" placeholder="筛选用户"></el-input>
      <el-select v-model="value" placeholder="请选择筛选方式">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-search" @click="searchByWord()">搜索</el-button>
      <el-button type="primary" icon="el-icon-plus" @click="centerDialogVisible = true">添加用户</el-button>
      <el-button type="danger" @click="deleteAll()">批量删除</el-button>
    </div>

    <el-table height="500px" border size="small" :data="tableData" :header-cell-style="{color:'#848484', fontSize: '13px', backgroundColor: '#qua'}" @selection-change="handleSelectionChange">

      <el-table-column type="selection" width="60" align="center"></el-table-column>
      <el-table-column label="ID" prop="userNid" width="50" align="center"></el-table-column>

      <el-table-column label="用户头像" width="102" align="center">
        <template #default="scope">
          <el-image class="table-td-thumb" :src="scope.row.userAvatarUrl" :z-index="10"
            :preview-src-list="[scope.row.userAvatarUrl]" preview-teleported>
          </el-image>
        </template>

      </el-table-column>

      <el-table-column label="用户名" prop="userName" width="80" style="align:center;"></el-table-column>
      <el-table-column label="密码" prop="userPassword" width="80" align="center"></el-table-column>
      <el-table-column label="年龄" prop="userAge" width="60" align="center">
      </el-table-column>
      <el-table-column label="性别" width="60" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.userSex === '男'">{{ scope.row.userSex }}</el-tag>
          <el-tag v-else-if="scope.row.userSex === '女'" type="danger">{{ scope.row.userSex }}</el-tag>
          <el-tag v-else type="info">{{ scope.row.userSex }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" prop="userEmail" width="120" align="center"></el-table-column>
      <el-table-column label="手机号码" prop="userTel" width="120" align="center"></el-table-column>
      <el-table-column label="简介" width="120" align="center">
        <template v-slot="scope">
          <p style="height: 100px; overflow: scroll">
            {{ scope.row.userDesc }}
          </p>
        </template>
      </el-table-column>

      <el-table-column label="地址" width="100">
        <template v-slot="scope">
          <i class="el-icon-location-outline">{{ scope.row.userAddr }}</i>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="90">
        <template v-slot="scope">
          {{ scope.row.dateJoined | formatDate }}
        </template>
      </el-table-column>

      <el-table-column label="最近登录" width="90">
        <template v-slot="scope">
          {{ scope.row.lastLogin | formatDate }}
        </template>
      </el-table-column>
      <el-table-column label="ip" prop="userIp" width="100"></el-table-column>
      <el-table-column label="操作" width="90" align="center">
        <template v-slot="scope">
          <el-button type="text" class="red" icon="el-icon-delete" @click="deleteRow(scope.row.userNid)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>

    <el-pagination class="pagination" background :current-page="currentPage" :page-size="queryInfo.pagesize" :total="total"
      @current-change="handleCurrentChange">
    </el-pagination>

    <!-- 添加 -->
    <el-dialog title="添加用户" center :visible.sync='centerDialogVisible'>
      <el-form label-width="80px" ref="registerForm" :model="registerForm" :rules="registeRules">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="registerForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="userPassword">
          <el-input v-model="registerForm.userPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="userSex">
          <el-radio-group v-model="registerForm.userSex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
            <el-radio label="其他"></el-radio>
          </el-radio-group>

        </el-form-item>
        <el-form-item label="年龄" prop="userAge">
          <el-input v-model="registerForm.userAge"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="userTel">
          <el-input v-model="registerForm.userTel"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="userEmail">
          <el-input v-model="registerForm.userEmail"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="userDesc">
          <el-input type="textarea" v-model="registerForm.userDesc"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addConsumer('registerForm')">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</div>
</div>
</template>

<script>
import { getUserList, addUser, deleteUser, deleteUsers, getUserListBySearchWord } from "@/api/user.js"
import { Message } from "element-ui";
import { formatDate } from "../../utils/time"
export default {
  data() {
    let checkAge = (rule, value, callback) => {
      let reg = /^(?:[1-9][0-9]?|1[01][0-9]|120)$/; //年龄是1-120之间有效
      if (value == undefined) {
        callback([new Error("帐号输入不合法")]);
      } else {
        if (value && value.length > 0) {
          if (!reg.test(value)) {
            callback([new Error("年龄输入不合法")]);
          } else {
            callback();
          }
        } else if (value.length == 0) {
          callback();
        } else {
          callback(new Error("年龄输入不合法"));
        }
      }
    }
    return {
      searchWord: "",// 记录输入框输入的内容
      centerDialogVisible: false,
      multipleSelection: [],
      options: [{
        value: '选项1',
        label: '用户名'
      }, {
        value: '选项2',
        label: 'ID'
      }, {
        value: '选项3',
        label: '性别'
      },{
        value: '选项4',
        label: '地址'
      }],
      value: '',
      tableData: [],
      tempData: [],
      registerForm: {
        userName: "",
        userPassword: "",
        userAge: null,
        userSex: "",
        userEmail: "",
        userTel: "",
        userDesc: "",

      },
      registeRules: {
        userName: [{
          required: true,
          message: "用户名为必填项",
          trigger: "blur",
        }],
        userPassword: [{
          required: true,
          message: "密码为必填项",
          trigger: "blur",
        },
        {
          min: 6,
          max: 30,
          message: "密码长度在6-30之间",
          trigger: "blur",
        }],
        userSex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        userAge: [{
          required: true,
          message: '请填写合法年龄',
          validator: checkAge,
          trigger: 'blur',
        },
        {
          pattern: /^-?\d+\.?\d*$/,
          message: '必须为数字',
          trigger: 'blur',
        }],
        userTel: [{
          pattern: /^1[3456789]\d{9}$/,
          message: '手机号码格式不正确',
          trigger: 'blur'
        }],
        userEmail: [{
          pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/,
          message: '邮箱格式不正确',
          trigger: 'blur'
        }]
      },
      currentPage: 1,//当前页
      total: 0,//数据条数
      queryInfo: {
        // 当前页数
        pagenum: 1,
        // 显示一页多少条数据
        pagesize: 4
      }

    }
  },
  methods:
  {
    getData() {
      this.clearForm()
      getUserList(this.queryInfo)
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

    deleteAll()//
    {
      console.log(this.multipleSelection);

      deleteUsers(this.multipleSelection)
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

      deleteUser(id)
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
    addConsumer(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          addUser(this.registerForm)
            .then((res) => {
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

    getDataBySearchWord() {
      // this.queryInfo.pagenum = this.currentPage;
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

      getUserListBySearchWord(temp)
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

    },
    clearForm(){
        this.registerForm.userName= ""
        this.registerForm.userPassword=""
        this.registerForm.userAge=null
        this.registerForm.userSex=""
        this.registerForm.userEmail= ""
        this.registerForm.userTel= ""
        this.registerForm.userDesc= ""
    }



  },
  computed:
  {


  },

  watch:
  {
    
  },

  mounted() {
    this.getData();
  }

}
</script>
   

</script>

<style src="../../assets/css/consumerPage/consumerPage.css">

</style>
