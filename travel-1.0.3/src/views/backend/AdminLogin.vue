<template>
  <div class="login-container">
    
    <div class="title">行旅谈后台管理</div>

    <div class="login">
      <el-form >
        <el-form-item prop="username">
          <el-input v-model="username" placeholder="username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="password" v-model="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="login-btn" type="primary" @click="submitForm()">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { AdminLogin } from "@/api/login"
import { Message } from "element-ui";
export default
{
  
  data()
  {
    return{
      username:"",
      password:"",
    };
  },
  methods:
    {
      async submitForm()
      {
        try{
          const res =await AdminLogin(this.username,this.password)
          if(res.success){
            localStorage.setItem("adminToken",res.data);
            this.$router.push('dashboard');
          }
      }catch(error){
        Message({
          type:"warning",
          message: error
        })
      }
      }
      
    }
}

</script>
<style src="../../assets/css/adminLogin/adminLogin.css"></style>

