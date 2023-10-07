<template>
    <div class="container">
        <form class="search_box" >
            <div>
                行旅谈 | 搜索
            </div>
            <div>
                <input class="search_key_input" type="text" placeholder="搜索你想要的内容"  v-model="search_key">
            </div>
            <form @submit.prevent>
                <div>
                    <button type="submit" @click="search(0)">
                        <i class="fa fa-search"></i>
                        搜索
                    </button>
                </div>
            </form>
        </form>

        <div class="between">
            <!--value表示搜索到的相关的文章，景点，用户各自的数量-->
            <el-badge :value="articleNum" class="item">
                <span class="searchtag" @click="showArticle()" :class="{ 'actived': isShowingArticle }">文章</span>
            </el-badge>
            <el-badge :value="siteNum" class="item">
                <span class="searchtag" @click="showhSite()" :class="{ 'actived': isShowingSite }">景点</span>
            </el-badge>
            <el-badge :value="userNum" class="item">
                <span class="searchtag" @click="showUser()" :class="{ 'actived': isShowingUser }">用户</span>
            </el-badge>
        </div>

        <el-divider></el-divider>


        <div class = "content">

            <div v-if=isShowingArticle>
                <!-- 5-22: -->
                <div class="tagList" v-if="articleTagList.length > 0">
                    <ul>
                        <li v-for="(tag,index) in articleTagList" :key="tag.tagArticleNid" v-show="tag.articleCount>0">
                            <div  v-if = !ArticleTagIsChoosen[index]  @click = "changeArticleTagStatus(index)">
                                {{ tag.articleTag }}
                            </div>
                            <div v-else @click = "changeArticleTagStatus(index)">
                                <span style=" background-color: lightblue ">{{ tag.articleTag }} </span>
                            </div>
                        </li>
                    </ul>
                </div>

                <ul class="detail" style="list-style-type: none; margin-top: 20px;">
                    <div class="image-container">
                        <li v-for="(article, index) in relatedArticleList" style="margin-bottom: 40px; position: relative;">
                            <div>
                                <a :href="'/article/' + article.articleNid">
                                    <img :src="article.coverUrl" style="width: 100%; height: 180px; object-fit: inherit; border-radius: 5px;" alt="">
                                </a>
                            </div>
                            <p style="margin-bottom: 5px;"><a :href="'/article/' + article.articleNid" class="article_title">{{ article.articleTitle }}</a></p>
                            <div>
                                <span style="position:absolute; top:150px; left: 10px; color: #ffffff;">
                                    <i class="far fa-eye" style="color: #ffffff;"></i>&nbsp;{{ article.lookCount | formatStarCounts }}
                                </span>
                                <span style="position:absolute; top:150px; left: 80px; color: #ffffff;"><i class="fa-solid fa-thumbs-up" style="color: #ffffff;"></i>
                                    {{ article.starCount|formatStarCounts }}</span>
                                <span style="font-size: 10px; color: #969ba2;">
                                    <i class="fa-solid fa-at" style="margin-right: 2px;"></i>{{ article.articleAuthor}}
                                </span>
                                <span style="font-size: 10px; color: #969ba2;">
                                    <i class="fas fa-clock" style="margin-left: 5px; margin-right: 2px"></i>{{ article.createDate | formatTime }}
                                </span>
                            </div>
                        </li>
                    </div>
                </ul>
                <div v-if = "totalArticle != 0">
                    <el-pagination  background :total="totalArticle"
                        :current-page="pageNumArticle" :page-size="pageSizeArticle"
                        @current-change="handleCurrentChangeArticle">
                    </el-pagination>
                </div>
            </div>

            <div v-else-if=isShowingSite>

                <!-- 5-22 -->
                <div style="display: flex;">
                    <h3 style=" margin-bottom: 5px; margin-left: 45px; line-height: 30px;">请选择你感兴趣的地点：</h3>
                    <region-selects :area="false" style=" margin-bottom: 20px; " v-model="region" @change = "changeReginSelect"/>
                </div>
                <ul class="detail" style="list-style-type: none">
                    <div class="image-container">
                        <li v-for="(site, index) in relatedSiteList" style="margin-bottom: 40px; position: relative;">
                            <div>
                                <a :href="'/site?siteId='+ site.siteNid">
                                    <img :src="site.sitePicUrl" style="width: 100%; height: 180px; object-fit: inherit; border-radius: 5px;" alt="">
                                </a>
                            </div>
                            <p  style="cursor: pointer;"><a :href="'/site?siteId='+ site.siteNid" class="article_title">{{ site.siteName }}</a></p>
                            <div>
                                <span style="position:absolute; top:150px; left: 10px; color: #ffffff;">
                                    <i class="fa-solid fa-location-dot" style="margin-right: 5px;"></i>{{ site.siteAddr}}
                                </span>
                                <span style="position:absolute; top:120px; left: 10px; color: #ffffff;">
                                    <i class="fa-solid fa-star" style="margin-right:5px;"></i>{{ site.siteScore }}
                                </span>
                            </div>
                        </li>
                    </div>
                </ul>
                <div v-if = "totalSite != 0">
                    <el-pagination layout="prev, pager, next " background :total="totalSite"
                        :current-page="pageNumSite" :page-size="pageSizeSite"
                        @current-change="handleCurrentChangeSite">
                    </el-pagination>
                </div>
            </div>

            <div v-else-if=isShowingUser>
                <ul class="userDetail" style="list-style-type: none">
                        <li v-for="(user, index) in relatedUserList" class="userCard">
                            <div class="left">
                                <a :href="'/personal/' + user.userNid">
                                    <img :src="user.userAvatarUrl" style="width: 100px; height: 100px; border-radius: 50%  " alt="">
                                </a>
                            </div>
                            <div class="right">
                                <h2>{{ user.userName }}</h2>
                                <p><el-tag size="small">{{ fanCounts[index] }}</el-tag>粉丝数</p>
                                <p><el-tag size="small">{{ articleCounts[index] }}</el-tag>攻略数</p>
                            </div>
                            
                            
                        </li>
                </ul>
                <div v-if = "totalUser != 0">
                    <el-pagination layout="prev, pager, next " background :total="totalUser"
                        :current-page="pageNumUser" :page-size="pageSizeUser"
                        @current-change="handleCurrentChangeUser">
                    </el-pagination>
                </div>
            </div>

        </div>

    </div>
</template>
    
<script>
import '@fortawesome/fontawesome-free/css/all.css'
import {getRelatedArticleListByKeyWord} from '../../api/article';
import {getRelatedSiteListByKeyWord} from '../../api/site';
import {getRelatedUserListByKeyWord} from '../../api/user';
import { formatDate, formatTime } from '../../utils/time';
//5-22
import { ArticleTags } from "@/api/tag.js";
import { RegionSelects } from 'v-region'

export default {
    //5-22
    name : 'Search',
    components: {
        RegionSelects
    },


    data() {
        return {
            
            search_key: "",

            isShowingArticle : true,
            isShowingSite : false,
            isShowingUser : false,

            articleNum : 0,
            siteNum : 0,
            userNum : 0,
            
            //5-22:
            ArticleTagIsChoosen : [],
            choosenArticleTagList : [],

            relatedArticleList : [],
            relatedSiteList : [],
            relatedUserList : [],
            //5-22:
            articleTagList : [],
            
            

            totalArticle : 0,
            pageNumArticle : 1,
            pageSizeArticle : 10,

            totalSite : 0,
            pageNumSite : 1,
            pageSizeSite : 10,

            totalUser : 0,
            pageNumUser : 1,
            pageSizeUser : 10,

            fanCounts:[],
            articleCounts:[],


            //5-22:
            region:{
                province: { key: String, value: String },
                city: { key: String, value: String },
            },
            selectedPlace : "",
        };
    },

    methods: {

        showArticle() {
            this.isShowingArticle = true;
            this.isShowingSite = false;
            this.isShowingUser = false;
        },
        showhSite() {
            this.isShowingArticle = false;
            this.isShowingSite = true;
            this.isShowingUser = false;
        },
        showUser() {
            this.isShowingArticle = false;
            this.isShowingSite = false;
            this.isShowingUser = true;
        },

        //5-22：查找文章时加入相关tag检索
        searchArticle(option){
            //option:0表示是由tag跳转过来的需要检索route
            if(this.$route.params.tagId != undefined && option == 0){
                for(var i = 0 ; i < this.articleTagList.length ; i++){
                    if(this.articleTagList[i].tagArticleNid == this.$route.params.tagId){
                        this.$set(this.ArticleTagIsChoosen,i,true);
                        break;
                    }
                };
            }
            this.choosenArticleTagList = new Array();
            for(var i = 0 ; i < this.ArticleTagIsChoosen.length ; i++){
                if(this.ArticleTagIsChoosen[i]){
                    this.choosenArticleTagList.push(this.articleTagList[i].tagArticleNid);
                }
            };
            getRelatedArticleListByKeyWord({
                keyWord : this.search_key,
                pageNum : this.pageNumArticle,
                pageSize : this.pageSizeArticle,
                tagList : this.choosenArticleTagList,
            }).then((res)=>{
                this.relatedArticleList = res.data.list;
                this.totalArticle = res.data.total;
                this.articleNum = this.totalArticle;
            });
        },

        //5-22：查找景点时加入感兴趣的地区：
        searchSite(){
            getRelatedSiteListByKeyWord({
                keyWord : this.search_key,
                pageNum : this.pageNumSite,
                pageSize : this.pageSizeSite,
                selectedPlace : this.selectedPlace,
            }).then((res)=>{
                this.relatedSiteList = res.data.list;
                this.totalSite = res.data.total;
                this.siteNum = this.totalSite;
            });
            
        },
        searchUser(){
            getRelatedUserListByKeyWord({
                keyWord : this.search_key,
                pageNum : this.pageNumUser,
                pageSize : this.pageSizeUser,
            }).then((res)=>{
                this.relatedUserList = res.data.manualPageVo.list;
                this.totalUser = res.data.manualPageVo.total;
                this.fanCounts=res.data.fanCounts
                this.articleCounts=res.data.articleCounts
                this.userNum = this.totalUser;
            });
        },
        //5-22：获取tagList
        getArticleTagList(){
            ArticleTags().then((res)=>{
                this.articleTagList = res.data;
                this.ArticleTagIsChoosen = new Array(this.articleTagList.length);
                for(var i = 0 ; i < this.ArticleTagIsChoosen.length ; i++){
                    this.ArticleTagIsChoosen[i] = false;
                };
                if(this.$route.params.tagId != undefined){
                    this.searchArticle(0);
                }
            });
        },
        //5-22
        changeArticleTagStatus(index){
            this.$set(this.ArticleTagIsChoosen,index,!this.ArticleTagIsChoosen[index]);
        },

        search(option) {
            //option : -1代表初始化,0代表点击搜索,其余代表分页变化

            //查出相关的文章,并更新数据和分页信息：
            if(option == 1 || option == 0 || option == -1){
                this.searchArticle(1);
            }
            //查出相关的景点,并更新数据和分页信息：
            if(option == 2 || option == 0 || option == -1){
                if(this.$route.params.selectedPlace == undefined){
                    this.searchSite();
                }
            }
            //查出相关的用户,并更新数据和分页信息：
            if(option == 3 || option == 0 || option == -1){
                this.searchUser();
            }

            //5-22:
            if(option == -1){
                this.getArticleTagList();
                if(this.$route.params.selectedPlace != undefined){
                    this.showhSite();
                    this.selectedPlace = this.$route.params.selectedPlace;
                    this.searchSite();
                }
            }

            this.search_key = "";
        },

        handleCurrentChangeArticle(newPage) {
            this.pageNumArticle = newPage;
            this.search(1);
        },
        handleCurrentChangeSite(newPage) {
            this.pageNumSite = newPage;
            this.search(2);
        },
        handleCurrentChangeUser(newPage) {
            this.pageNumUser = newPage;
            this.search(3);
        },
        //5-22:
        changeReginSelect(data){
            this.selectedPlace = "";
            if(data.province != undefined){
                this.selectedPlace += data.province.value;
                this.selectedPlace = this.selectedPlace.slice(0,-1);
            }
            if(data.city != undefined){
                this.selectedPlace += '·';
                this.selectedPlace += data.city.value;
                this.selectedPlace = this.selectedPlace.slice(0,-1);
            }
        },
    },
    created() {
        this.search(-1);
        
    }

}

</script>
    
<style src="../../assets/css/search/search.css"></style>

<style> #content {
     position: absolute;
     /*必须*/
     top: 800px;
     /*top必须，大小可控制*/
     bottom: 0;
     /*bottom必须，一直延申到当前块底部*/
     width: 100%;
 }
.image-container {
    padding: 0 50px; /* 左右留白为20px */
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    grid-gap: 10px;
}
 .text-container {
  width: 300px; /* 容器的宽度 */
  overflow: hidden; /* 超出容器的内容隐藏 */
  white-space: nowrap; /* 将文字限制为一行 */
}

 .infoUser {
     display: flex;
     align-items: center;
     justify-content: space-between;
     height: 20px;
     color: #666;
     font-size: 12px;
     transition: color 1s;
 }

.tagList {
  margin-top: 20px;
  border-radius: 5px;
  border: 1px dashed #d7d7d7;
  display: block;
  overflow: hidden;
}
.tagList > ul {
  width: 100%;
  margin-left: 20px;
}
.tagList > ul li {
  width: 120px;
  margin-top: 10px;
  margin-bottom: 10px;
  float: left;
  display: inline;
  font-size: 18px;
  height: 22px;
  line-height: 22px;
  align-items: center;
  color: #666;
  padding-left: 2%;
  cursor: pointer;
}

</style>
<!-- <style scoped src="../../assets/css/article/article.css"></style> -->
<style lang="scss">
::-webkit-scrollbar {
    width: 0 !important;
}

::-webkit-scrollbar {
    width: 0 !important;
    height: 0;
}
.actived {
    color: var(--active);
}
.el-pagination {
    margin-top: 30px;
    text-align: center; 
    margin-bottom: 30px;
}
.el-badge__content.is-fixed {
    position: absolute;
    top: 12px;
    right: -3px;
    transform: translateY(-50%) translateX(100%);
}
.article_title {
    font-weight: bold;
}
.article_title:hover{
    color:var(--active);
}
.userDetail{
    display: flex;
    flex-wrap: wrap;
    padding-left: 80px;
    .userCard{
    width: 50%;
    box-sizing: border-box;
    display: flex;
    padding: 10px 0;
    .left{
        width: 15%;
    }
    .right{
        width: 83%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        >h2{
            line-height: 10px;
        }
        >p{
            color:cadetblue;
        }
    }
}
}

</style>