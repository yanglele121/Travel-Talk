<template>
  <div>
    <el-dialog title="修改个人信息" :visible.sync="dialogVisible" width="60%" :before-close="handleClose">
      <el-form :model="form" :rules="rules" ref="form" label-width="150px">
        <div class="updateinfo">
          <div class="left">
            <el-form-item class="myavatar" label="头像" prop="userAvatarUrl">
              <img style="width:45%; height: 150px;" :src="form.userAvatarUrl">
              <el-upload class="avatar-uploader" style="width: 53%;"
                :action="baseUrl+'/uploadUserProfile'"
                list-type="picture-card"
                :show-file-list="false" 
                :on-success="uploadSuccess"
                :before-upload="beforeAvatarUpload"
                >
                <i class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="form.userName"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="userPassword">
              <el-input v-model="form.userPassword" show-password></el-input>
            </el-form-item>           
            <el-form-item label="年龄" prop="userAge">
              <el-input v-model="form.userAge"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="userSex">
              <el-radio-group v-model="form.userSex">
                <el-radio label="男"></el-radio>
                <el-radio label="女"></el-radio>
                <el-radio label="其他"></el-radio>
              </el-radio-group>
            </el-form-item>
            

          </div>
          <div class="right">
            <el-form-item label="ID" prop="userNid">
              <el-input v-model="form.userNid" disabled></el-input>
            </el-form-item>
            <el-form-item label="地区" prop="userAddr">
              <el-input v-model="form.userAddr" disabled></el-input>
            </el-form-item>
            <el-form-item label="个性签名" prop="userDesc">
              <el-input v-model="form.userDesc"></el-input>
            </el-form-item>
            <el-form-item label="手机" prop="userTel">
              <el-input v-model="form.userTel"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="userEmail">
              <el-input v-model="form.userEmail"></el-input>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="submit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { userInfo, updateUser } from "@/api/user.js";
import request from "@/request"
export default {
  name: "PersonalDia",
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
      dialogVisible: false,
      imageUrl:"",
      baseUrl:'',
      form: {
        userAvatarUrl: "",
        userPassword: "",
        userName: "",
        userAge: Number,
        userEmail: "",
        userTel: "",
        userSex: "",
        userNid: Number,
        userAddr: "",
        userDesc: "",
      },
      rules: {
        userName: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        userPassword: [
          { required: true, message: "密码不能为空", trigger: "blur" },
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
    };
  },
  mounted() {
    this.load();
  },
  created(){
    this.baseUrl = request.defaults.baseURL
  },
  methods: {
    open() {
      this.dialogVisible = true;
    },
    load() {
      if(this.$store.state.id){
      userInfo(this.$store.state.id,true)
        .then((res) => {
          Object.assign(this.form, res.data);
        })
        .catch((err) => {
          this.$message.error(err);
        });
    }
  },
      uploadSuccess(res){
        console.log(res)
      this.imageUrl=res.imgURL
      this.form.userAvatarUrl=this.imageUrl
    },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 4;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 4MB!');
        }
        return isJPG && isLt2M;
      },
    submit() {
      updateUser(this.form)
        .then((res) => {
          if(res.code==200){
          this.dialogVisible = false;
          this.$store.commit('SET_NAME',this.form.userName)
          this.$store.commit('SET_AVATAR',this.form.userAvatarUrl)
          localStorage.setItem('store', JSON.stringify(this.$store.state))
          this.$message.success(res.data)
          this.$emit("flesh");
        }else{
          this.$message.error(res.msg)
        }
        })
        .catch((err) => {
          this.$message.error(err);
        });
    },
    handleClose() {
      this.dialogVisible = false;
      this.$emit("flesh");
    },
  },
};
</script>

<style scoped>
.updateinfo {
  height: 350px;
  overflow: auto;
}

.left {
  /* width: 330px; */
  float: left;
  width: 48%;
}
.myavatar >>> .el-form-item__content{
  margin-left: 0;
  display: flex;
  justify-content: space-between;
}
.right {
  width: 48%;
  overflow: hidden;
}

 .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 50px;
    height: 50px;
    line-height: 100px;
    text-align: center;
}
.el-upload--picture-card {
    background-color: #fbfdff;
    border: 1px dashed #c0ccda;
    border-radius: 6px;
    box-sizing: border-box;
    width: 100%;
    height: 100%;
    line-height: 100%;
    vertical-align: top;
}
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  } 
</style>