<template>
    <div class="site-main">
        <el-container>
            <div style="display:flex; flex-direction: column; width: 30%;">
                <el-input v-model="search" placeholder="请输入内容" style="width: 90%;margin-left: 10px;"></el-input>
                <div class="siteLeft" id="siteLeft" style="overflow: auto;">
                    <el-table :data="tableData.filter(data => !search || data.siteName.toLowerCase().includes(search.toLowerCase()))" style="width: 100%;" class="site-card" :show-header="false"
                    highlight-current-row>
                    <el-table-column :span="1">
                        <template slot-scope="scope">
                            <el-row>
                                <el-col :span="8">
                                    <img class="desc-img" :src=scope.row.sitePicUrl />
                                </el-col>
                                <el-col :span="16">
                                    <div class="grid-cont-right" style="cursor:pointer" @click="detail(scope.$index,scope.row.siteNid)">
                                        <h2>{{ scope.row.siteName }}</h2>

                                        <p><i style="color:#42b983; margin-top: 10px;"
                                                class="el-icon-location">景点地址</i>&nbsp;&nbsp;{{
                                                    scope.row.siteAddr }}</p>

                                        <p><i style="color:#42b983; margin-top: 10px;"
                                                class="el-icon-medal">景点评分</i>&nbsp;&nbsp;{{
                                                    scope.row.siteScore }}</p>
                                    </div>
                                </el-col>
                            </el-row>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
            <el-aside width="70%" :key="dataKey">
                <el-card class="box-card">
                    <div class="right" id="mainright" style="overflow: auto;">
                        <div class="top">
                            <div class="left">
                                <img :src=currentSiteInfo.sitePicUrl slot="error" />
                            </div>
                            <div class="right">
                                <h2>{{ currentSiteInfo.siteName }}<i class="fa-solid fa-star" style="margin-right: 5px;"
                                        :class="{ 'liked': collected }" @click="changeSiteCollect()"></i>
                                    <el-button v-if="!scored" type="text" style="position: absolute;right: 40px;"
                                        @click="dialogVisible=true"><i class="fa-solid fa-marker"></i></el-button>
                                </h2>
                                <el-dialog
                                title="景点评分"
                                :visible.sync="dialogVisible"
                                width="20%">
                                <p>人生至少要有两次冲动，一为奋不顾身的爱情，一为说走就走的旅行，请你对此景点打分吧</p>
                                <p>你的评分为: <b style="color: rgb(66, 136, 206);">{{score}}</b></p>
                                <el-rate v-model="score" style="margin-top: 10px; text-align: center;"></el-rate>
                                <span slot="footer" class="dialog-footer">
                                    <el-button @click="dialogVisible = false">取 消</el-button>
                                    <el-button type="primary" @click="JudgeScore()">确 定</el-button>
                                </span>
                                </el-dialog>
                                <el-rate v-model="currentSiteInfo.siteScore" disabled show-score text-color="#ff9900"
                                    style="margin-top: 10px;">
                                </el-rate>
                                <p><i style="color:#42b983; margin-top: 10px;"
                                        class="el-icon-location">景点地址</i>&nbsp;&nbsp;&nbsp;{{ currentSiteInfo.siteAddr
                                        }}
                                </p>
                                <p><i style="color:#42b983; margin-top: 10px;"
                                        class="el-icon-medal">景点评分</i>&nbsp;&nbsp;&nbsp;{{
                                            currentSiteInfo.siteScore }}
                                </p>
                                <p><i style="color:#42b983; margin-top: 10px;"
                                        class="el-icon-s-management">景点介绍</i>&nbsp;&nbsp;&nbsp;{{ currentSiteInfo.siteDesc
                                        }}
                                </p>
                                <p><i style="color:#42b983; margin-top: 10px;" class="el-icon-link">官网</i>
                                    <router-link to="" @click.native="linkToSiteUrl(currentSiteInfo.siteUrl)"
                                        class="link-type" stype="color:red">
                                        &nbsp;&nbsp;&nbsp;{{ currentSiteInfo.siteUrl }}
                                    </router-link>
                                </p>
                            </div>
                        </div>
                        <div class="bottom">
                            <ul class="article" v-for="item in articles ">
                                <li>
                                    <div class="left">
                                        <img :src="item.coverUrl" alt="">
                                    </div>

                                    <div class="right">
                                        <h2><a style="margin-bottom: 10px;" :href="'/article/' + item.articleNid">{{
                                            item.articleTitle }}</a></h2>

                                        <p style="margin-bottom: 10px;">{{ item.articleAbstract }}</p>

                                        <div class="article_info" style="margin-bottom: 10px;">
                                            <span>
                                                <i class="fas fa-clock"></i>{{ item.createDate | formatDate | formatTime
                                                }}
                                            </span>
                                            <span>
                                                <i class="far fa-eye"></i>{{ item.lookCount | formatStarCounts }} <!--浏览量-->
                                            </span>
                                            <span>
                                                <i class="fas fa-thumbs-up"></i>{{ item.starCount | formatStarCounts }}
                                            </span>
                                            <span>
                                                <i class="far fa-comments"></i>{{ item.collectedCount | formatStarCounts }}
                                            </span>

                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </el-card>
            </el-aside>
        </el-container>
    </div>
</template>

<script>
import $ from 'jquery'
import '@fortawesome/fontawesome-free/css/all.css'
import { SiteInfo, AllSite, AddUserSite, DeleteUserSite, AddScore } from "@/api/site"
import { formatDate, formatTime } from '../../utils/time';
import { formatStarCounts } from "../../utils/data"
import { Message } from 'element-ui';
export default {
    name: 'site_page',
    data() {
        return {
            search: '',
            dataKey: 0,
            scrollPosition: 0,
            currentSiteInfo: {},
            articles: [],
            tableData: [],
            siteId: "",
            index: 0,
            ids: {},
            userId: 0,
            scored: false,//用户对于此景点的评分状态
            collected: false,//用户对于此景点的收藏状态,
            dialogVisible:false,
            score:0
        }
    },
    methods: {
        go_to_top() {
            $('#mainright').animate({ scrollTop: 0 }, 1000)
        },
        changeSiteCollect()//修改用户收藏状态
        {
            this.collected = !this.collected;
            if (this.collected)//收藏
            {
                AddUserSite(this.ids)

            }
            else//取消收藏
            {
                DeleteUserSite(this.ids)
            }
        },
        
        JudgeScore(){
            let temp={
                userId:this.userId,
                siteId:this.siteId,
                score:this.score
            }
            AddScore(temp)
            .then((res)=>{
                if(res.success){
                    this.load();
                    this.tableData[this.index].siteScore=this.currentSiteInfo.siteScore
                    this.dialogVisible=false,
                    this.score=0
                    Message({
                        type:"success",
                        message:"评分成功"
                    })
                }
            
            })  
        },
        detail(index,id) {
            if(this.tableData[index].siteNid===id){
            this.index = index 
            }
            else{
                for(let i=0;i<this.tableData.length;i++){
                    if(this.tableData[i].siteNid===id){
                        this.index=i
                    }
                }
            }
            this.go_to_top()
            this.siteId = this.tableData[this.index].siteNid
            this.userId = parseInt(this.$store.state.id)
            this.ids = {
                userId: this.userId,
                siteId: this.siteId,
            }
            SiteInfo(this.ids)
                .then((res) => {
                    this.currentSiteInfo = res.data.siteInfo
                    this.articles = res.data.articles
                    this.collected = res.data.collected
                    this.scored = res.data.scored
                })
        },
        ToSite(id){
            this.$router.push(`/site?siteId=${id}`)
        },
        load() {
            this.dataKey++;
            AllSite().then((res) => {
                this.tableData = res.data
                if (this.$route.query.siteId) {
                    this.siteId = this.$route.query.siteId
                }
                else {
                    this.siteId = this.tableData[this.index].siteNid
                }
                this.userId = parseInt(this.$store.state.id)
                this.ids = {
                    userId: this.userId,
                    siteId: this.siteId,
                }
                // console.log(ids);

                SiteInfo(this.ids)
                    .then((res) => {
                        this.currentSiteInfo = res.data.siteInfo
                        // console.log("222222")
                        // console.log(this.currentSiteInfo)
                        // console.log(res.data.siteInfo)
                        this.articles = res.data.articles
                        this.collected = res.data.collected
                        this.scored = res.data.scored
                        //  console.log(res.data.collected)
                    })
            })
        },
        linkToSiteUrl(url) {
            window.open(url, "_blank");
        },
    },
    mounted() {
        this.load()
    }
};
</script>
<style>
.liked {
    color: red;
}
.el-dialog__body{
    padding: 7px 7px;
}
.site-main {
    height: 100%;
    margin-top: 20px;
}

.site-card {
    padding: 0px;
    margin-bottom: 10px;
    padding-right: 30px;
}

.site-card th {
    border: none;
}

.el-table--border,
.el-table--group {
    border: none;
}

.el-header,
.el-footer {
    background-color: #f2f2f2;
    color: #333;
    text-align: center;
    margin-right: 30px;
    line-height: 60px;
    border-radius: 5px;
}

.el-aside {
    background-color: white;
    color: #333;
    text-align: center;
}
.el-dialog__header {
    padding: 18px 7px 5px;
}
.el-dialog__footer {
    padding: 9px 10px 10px;
    text-align: right;
    box-sizing: border-box;
}
.el-main {
    background-color: white;
    color: #333;
    text-align: center;
}

.desc-img {
    width: 100%;
    padding-left: 0px;
    background-color: lightslategray;
}

.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    text-align: left;
    padding-left: 10px;
    font-size: 14px;
    color: black;
    /* margin-top: -10px; */
}

.grid-cont-right h2 {
    color: #42b983;
}

.el-scrollbar__bar {
    transition: none;
}

.el-scrollbar:hover>.el-scrollbar__bar {
    transition: none;
}

.el-scrollbar__thumb {
    transition: none;
    transform: none;
}
</style>

<style scoped src="../../assets/css/site/site.css"></style>
