<template>
  <div class="header">
    <div class="collapse-btn" >
      <img  class="logoimg" src="../assets/yjtp.png">
    </div>
    
    <div class="logo">行旅谈后台管理</div>
    <div class="header-right">
      <div class="header-user-con">
        <div class="user-avator">
          <img src="../assets/1.jpg" />
        </div>
        <el-dropdown class="user-name" trigger="click" >
          <span class="el-dropdown-link">
            {{ admin.adminName }}
            <i class="el-icon-caret-bottom"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item  @click.native="logout()">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script>
import { getCurAdmin,AdminLogOut } from "@/api/login"
import { Message } from "element-ui"
export default {
  name: 'Header',
  data() {
        return {
            admin:""
        }
    },
  created(){
    if(location.pathname.indexOf("dashboard")!==-1
    ||location.pathname.indexOf("announcements")!==-1
    ||location.pathname.indexOf("articles")!==-1
    ||location.pathname.indexOf("consumers")!==-1
    ||location.pathname.indexOf("sites")!==-1
    ||location.pathname.indexOf("comments")!==-1
    ){
        this.getAdmin()
    }
    
  },
  methods:{
    async getAdmin() {
  try {
    let token = localStorage.getItem("adminToken");
    if (token !== null) {
      const res = await getCurAdmin(token);
      if (res.success) {
        this.admin = res.data;
      }
    }
  } catch (error) {
    // 处理异常情况
    Message({
          type: "warning",
          message: error
        });
        this.$router.push("/admin/login");
    // 执行适当的错误处理逻辑，例如显示错误消息、重定向到错误页面等
  }
    },
    async logout(){
      let token = localStorage.getItem("adminToken")
      const res =await AdminLogOut(token)
      if(res.success){
        localStorage.removeItem("adminToken")
        this.$router.push("/admin/login")
      }
    }
  }
  
}

</script>

<style scoped>
.logoimg
{
  width:70px;
  height:50px;
}
.header {
  position: fixed;
  z-index: 100;
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  font-size: 20px;
  color: #2c3e50;
  background: #ffff;
  box-shadow: 0px 0px 8px 2px rgba(0, 0, 0, 0.3);
}

.collapse-btn {
  display: flex;
  padding: 0 21px;
  cursor: pointer;
}

.header .logo {
  width: 250px;
  font-weight: bold;
}

.header-right {
  position: absolute;
  right: 0;
  padding-right: 30px;
}

.header-user-con {
  display: flex;
  align-items: center;
}

.user-name {
  margin-left: 10px;
}

.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.el-dropdown-link {
  cursor: pointer;
}

.el-dropdown-menu__item {
  text-align: center;
}

/* .collapse-btn {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100%;
	float: left;
	padding: 0 21px;
	cursor: pointer;
} */
</style>
