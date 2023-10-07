<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      background-color="#ffffff"
      active-text-color="#30a4fc"
      :default-active="activePath"
    >
      <el-menu-item index="/admin/dashboard" @click="toPath('dashboard')">
        <i class="el-icon-s-home"></i>
        <span>系统首页</span>
      </el-menu-item>
      <el-menu-item index="/admin/announcements" @click="toPath('announcements')">
        <i class="el-icon-notebook-2"></i>
        <span>资讯管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/consumers" @click="toPath('consumers')">
        <i class="el-icon-user"></i>
        <span>用户管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/sites" @click="toPath('sites')">
        <i class="el-icon-wind-power"></i>
        <span>景点管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/tags" @click="toPath('tags')">
        <i class="el-icon-price-tag"></i>
        <span>标签管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/articles" @click="toPath('articles')">
        <i class="el-icon-document"></i>
        <span>文章管理</span>
      </el-menu-item>

      <el-menu-item index="/admin/comments" @click="toPath('comments')">
        <i class="el-icon-s-comment"></i>
        <span>评论管理</span>
      </el-menu-item>

    </el-menu>
  </div>
</template>

<script>
import '@fortawesome/fontawesome-free/css/all.css'
export default {
  name: 'Aside',
  data() {
    return {
      activePath:"",
    }
    },
    methods:
    {
      toPath(path)
      {
        
        this.$router.push(path);

        this.activePath=path;
        
      }

    },
    watch:{
        $route(to){
          //  console.log(to.path);//到哪去
           this.activePath=to.path;
        }
      },
    created()
    {
      
      
    }
  
}
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 60px;
  bottom: 0;
  overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar > ul {
  height: 100%;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 150px;
}
</style>
