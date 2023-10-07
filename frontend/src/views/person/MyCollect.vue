<template>
  <div class="block">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="收藏文章" name="first">
        <div class="myart1">
          <article-item v-for="a in allData" :key="a.articleNid" v-bind="a" />
          <el-empty v-if="allData.length == 0" :image-size="250" description="暂未收藏任何攻略哦"></el-empty>
        </div>
        <el-pagination v-if="totalPage" layout="prev, pager, next " :total="totalPage" 
          :current-page="queryInfo.pagenum"
          :page-size="queryInfo.pagesize" 
          @current-change="handleCurrentChange">
        </el-pagination>
      </el-tab-pane>
      <el-tab-pane label="收藏景点" name="second" >
        <el-card>
          <div class="head-site">
          <div class="myCollectedSite"  v-for="a in allData1" :key="a.siteNid" v-bind="a" @click="ToSite(a.siteNid)">
            
            <img class="site-img" :src=a.sitePicUrl />
            <h4>{{ a.siteName }}</h4>

            <p><i style="color:#42b983; margin-top: 8px;"
                class="el-icon-location">景点地址</i>&nbsp;&nbsp;{{
                    a.siteAddr }}</p>

            <p><i style="color:#42b983; margin-top: 8px;"
                class="el-icon-medal">景点评分</i>&nbsp;&nbsp;{{
                    a.siteScore }}</p>
        </div>
    
          <el-empty v-if="allData1.length == 0" :image-size="250" description="暂未收藏任何景点哦"></el-empty>
       
        </div>
        
        </el-card>
        <el-pagination v-if="totalPage1" layout="prev, pager, next " :total="totalPage1" :current-page="queryInfo1.pagenum"
          :page-size="queryInfo1.pagesize" @current-change="handleCurrentChange1">
        </el-pagination>
       
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { getCollectArticleListByUserId ,getCollectSiteListByUserId} from "@/api/like.js";
import ArticleItem from '../../components/article/ArticleItem.vue';
export default {
  components: { ArticleItem },
  name: "MyCollect",
  data() {
    return {
      activeName: 'first',
      allData: [],
      allData1: [],
      totalPage: 0,
      queryInfo: {
        // 当前页数
        pagenum: 1,
        // 显示一页多少条数据
        pagesize: 2
      },
      totalPage1: 0,
      queryInfo1: {
        // 当前页数
        pagenum: 1,
        // 显示一页多少条数据
        pagesize: 6
      },
    };
  },
  mounted() {
    this.load();
    this.load1();
  },
  methods: {
    ToSite(id){
            this.$router.push(`/site?siteId=${id}`)
        },
    load() {
      getCollectArticleListByUserId(this.$route.params.id, this.queryInfo)
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
    },
    load1()
    {
      getCollectSiteListByUserId(this.$route.params.id,this.queryInfo1)
        .then((res)=>
        {
          this.allData1=res.data.list
          this.totalPage1=res.data.total
          // console.log(res.data)
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
    handleCurrentChange1(newPage)
    {
      //把最新的页码（newPage）赋值给 动态的 pagenum 
      
      //获取到最新显示的页码值  重新发送axios请求 这里是封装好的请求方法
      this.queryInfo1.pagenum= newPage
      this.load1()
    },

  },
};
</script>

<style>
.el-card {
  border-radius: 0;
}

.el-card:not(:first-child) {
  margin-top: 5px;

}

.myart1 {
  line-height: 30px;
}

.myart1 {
  line-height: 30px;
  
}

.site-img
{
  height: 140px;
  position: relative;
  width: 100%;
}


.myCollectedSite
{
  border-radius: 5px;
  background-color: var(--main_bg);
  float: left;
  width: 31%;
  box-sizing: border-box;
  padding: 5px;
  margin-left: 2%;
  margin-bottom: 2%;
  
  height:240px;
}
</style>