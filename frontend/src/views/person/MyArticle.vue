<template>
  <div class="block">
    <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="文章" name="first">
      <div class="myart1">
      <article-item v-for="a in allData" :key="a.articleNid" v-bind="a" />
      <el-empty v-if="allData.length == 0" :image-size="250" description="暂未发表任何攻略哦"></el-empty>
    </div>
    <el-pagination v-if="totalPage" layout="prev, pager, next " 
    :total="totalPage"
    :current-page="queryInfo.pagenum" 
    :page-size="queryInfo.pagesize"
    @current-change="handleCurrentChange" 
    >
    </el-pagination>
    </el-tab-pane>
    <el-tab-pane label="圈子" name="second">
      <el-card>

      <div class="myart2">
      <div  class="myPic"  v-for="a in allData1" :key="a.articleNid" v-bind="a" >
        <img class="imgUrl" v-image-preview style="height: 180px; width:100%; line-height: 2; margin-right:5px" :src= a.imgUrl />
        <div class="info">{{ a.picalbumContent }}</div>          
      </div>

      <el-empty v-if="allData1.length == 0" :image-size="250" description="暂未发表任何圈子哦"></el-empty>
    </div>
  </el-card>
    <el-pagination v-if="totalPage1" layout="prev, pager, next " 
    :total="totalPage1"
    :current-page="queryInfo1.pagenum" 
    :page-size="queryInfo1.pagesize"
    @current-change="handleCurrentChange1" 
    >
    </el-pagination>

    </el-tab-pane>
  </el-tabs>
    
  </div>
</template>

<script>
import { myArticle,getPicAlbumListByUserId } from "@/api/user.js";
import ArticleItem from '../../components/article/ArticleItem.vue';
export default {
  components: { ArticleItem },
  name: "MyArticle",
  data() {
    return {
      activeName:'first',
      allData: [],
      totalPage: 0,
      queryInfo: {
        // 当前页数
        pagenum: 1,
        // 显示一页多少条数据
        pagesize: 2
      },
      //chenhao
      allData1: [],
      totalPage1: 0,
      queryInfo1: {
        // 当前页数
        pagenum: 1,
        // 显示一页多少条数据
        pagesize: 6
      }
      //chenhao
    };
  },
  mounted() {
    this.load();
    this.load1()
  },
  methods: {
    load() {
      myArticle(this.$route.params.id,this.queryInfo)
        .then((res) => {
          this.allData = res.data.list
          this.totalPage = res.data.total
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleSizeChange(newsize) {
      //这里conso 选中第几页 最新的值
      //最新的条数（newsize）赋值给 动态的 pagesie
      this.queryInfo.pagesize = newsize
      //获取到最新一页显示的数据  重新发送axios请求 这里是封装好的请求方法
      this.load()
    },
    handleCurrentChange(newPage) {
      //把最新的页码（newPage）赋值给 动态的 pagenum 
      this.queryInfo.pagenum = newPage
      //获取到最新显示的页码值  重新发送axios请求 这里是封装好的请求方法
      this.load()
    },
    handleClick(tab,event){
      // console.log(tab)
    },
    load1()
    {
      getPicAlbumListByUserId(this.$route.params.id,this.queryInfo1)
      .then((res) => {
          this.allData1 = res.data.list
          this.totalPage1 = res.data.total
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleSizeChange1(newsize) {
      //这里conso 选中第几页 最新的值
      //最新的条数（newsize）赋值给 动态的 pagesie
      this.queryInfo1.pagesize = newsize
      //获取到最新一页显示的数据  重新发送axios请求 这里是封装好的请求方法
      this.load1()
    },
    handleCurrentChange1(newPage) {
      //把最新的页码（newPage）赋值给 动态的 pagenum 
      this.queryInfo1.pagenum = newPage
      //获取到最新显示的页码值  重新发送axios请求 这里是封装好的请求方法
      this.load1()
    },

  },
};
</script>

<style>
.myPic
{
  border-radius: 5px;
  background-color: var(--main_bg);
  float: left;
  width: 31%;
  box-sizing: border-box;
  padding: 10px;
  margin-left: 2%;
  margin-bottom: 2%;
  height:240px;
}
.myart2 {
  display: flex;
  flex-wrap: wrap;
  margin-top: 5px;
  width: 100%;

}
.myart1 {
  line-height: 30px;
}
.block{
  height: 500px;
}
</style>