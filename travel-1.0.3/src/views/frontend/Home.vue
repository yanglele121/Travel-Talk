<template>
    <div class="home">
        <div class="top">
            <HomeCarousel cards="visited" />
        </div>
        <main>
            <div class="main">
                <div class="left">
                    <div class="hot_site card">
                        <div class="title">
                            <h2>高分景点</h2>
                            <div class="switch_site_category">
                                <span :active="this_category === 'hot'" @click="switch_article_category('hot')">热门</span>
                                <span :active="this_category === 'local'" @click="switch_article_category('local')"> 同城
                                </span>
                            </div>
                        </div>
                        <div class="body">
                            <ul v-show="this_category === 'hot'" class="hot">
                                <!-- 循环景点 -->
                                <li v-for="site in hotSite" :key="site.siteNid">
                                    <div class="left">
                                        <div>
                                            <img :src="site.sitePicUrl" alt="">
                                        </div>
                                    </div>
                                    <div class="right">
                                        <div @click="ToSite(site.siteNid)" style="cursor: pointer;">
                                            <h3>{{ site.siteName }}</h3>
                                            <p>{{ site.siteDesc }}</p>
                                        </div>
                                        <span>{{ site.siteAddr }}</span>
                                    </div>
                                </li>
                            </ul>
                            <ul v-show="this_category === 'local'" class="back">
                                <!-- {% for backend in backend_list %} -->
                                <!-- 循环后端 -->
                                <li v-for="lsite in localhotSite" :key="lsite.siteNid">
                                    <div class="left">
                                        <div>
                                            <img :src="lsite.sitePicUrl" alt="">
                                        </div>
                                    </div>
                                    <div class="right">
                                        <div @click="ToSite(lsite.siteNid)" style="cursor: pointer;">
                                            <h3>{{ lsite.siteName }}</h3>
                                            <p>{{ lsite.siteDesc }}</p>
                                        </div>
                                        <span>{{ lsite.siteAddr }}</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="all_articles card">
                        <div class="title">
                            <h2>用户游记</h2>
                        </div>
                        <div class="body">
                            <ul class="detail">
                                <li v-for="(article, index) in articles">
                                    <div class="left">
                                        <div>
                                            <img :src="article.coverUrl" alt="">
                                        </div>
                                    </div>
                                    <div class="right">
                                        <h2><a :href="'/article/' + article.articleNid">{{ article.articleTitle }}</a></h2>
                                        <p>{{ article.articleAbstract }}</p>
                                        <div class="article_info">
                                            <span @click="personal(userData[index].userNid)" style="cursor: pointer;">
                                                <img v-if="isShow" :src="userData[index].userAvatarUrl"
                                                    style="width: 20px; height: 20px; border-radius: 50%; vertical-align: bottom;"
                                                    alt="">{{ article.articleAuthor }}
                                            </span>

                                            <span v-if="AboutSite[index]">
                                                <i class="fas fa-location-dot"></i>{{ AboutSite[index].siteName }}
                                            </span>

                                            <span>
                                                <i class="fas fa-clock"></i>{{ article.createDate | formatDate | formatTime
                                                }}
                                            </span>
                                            <span>
                                                <i class="far fa-eye"></i>{{ article.lookCount | formatStarCounts }}
                                            </span>
                                            <span>
                                                <i class="fas fa-thumbs-up"></i>{{ article.starCount | formatStarCounts }}
                                            </span>
                                            <span>
                                                <i class="far fa-comments"></i>{{ article.commentCount | formatStarCounts }}
                                            </span>
                                            <span>
                                                <i class="fas fa-star-half-alt"></i>{{ article.collectedCount }}
                                            </span>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                            <el-pagination layout="prev, pager, next " background :total="totalPage"
                                :current-page="queryInfo.pagenum" :page-size="queryInfo.pagesize"
                                @current-change="handleCurrentChange">
                            </el-pagination>
                        </div>
                    </div>
                </div>
                <div class="right">
                    <div class="announcement card">
                        <div class="title">
                            <h2>旅行快报</h2>
                        </div>
                        <div class="body" style="padding: 0; position: relative;">
                            <el-carousel :interval="5000" height="270px" arrow="always" indicator-position="none">
                                <el-carousel-item v-for="item in items" :key="item.index">
                                    <img :src="item.coverUrl" style="height: 80%; width:100%;"
                                        @click="toAnnouncement(item.announcementContent)">
                                    <h3 style="position: absolute; left:10px;top: 220px; color: #000000; display: -webkit-box;
                        -webkit-line-clamp: 1;
                        -webkit-box-orient: vertical;
        
                        overflow: hidden;
                        text-overflow: ellipsis;">{{ item.announcementTitle }}</h3>
                                    <span style="position: absolute; left:10px;top: 250px;">{{ item.createDate | formatDate
                                    }}</span>
                                </el-carousel-item>
                            </el-carousel>
                        </div>
                    </div>
                    <div class="me_card card">
                        <div class="pb" :style="{ backgroundImage: 'url(' + MyInfo.userAvatarUrl + ')' }"></div>
                        <div class="info">
                            <h1>{{ MyInfo.userName }}</h1>
                            <span>{{ MyInfo.userDesc }}</span>
                        </div>
                        <div class="detail">
                            <div class="col">
                                <h3> {{ articleCount }} </h3>
                                <h4>文章数量</h4>
                            </div>
                            <div class="col">
                                <h3>{{ fanCount }}</h3>
                                <h4>粉丝数量</h4>
                            </div>
                            <div class="col">
                                <h3>{{ followCount }}</h3>
                                <h4>关注数量</h4>
                            </div>
                        </div>
                        <div class="buttons">
                            <button id="follow"><a :href='"/personal/" + MyInfo.userNid' target="_blank">个人主页</a>
                            </button>
                            <a href="/createCenter" target="_blank"><button id="message">创作空间</button></a>
                        </div>
                    </div>
                    <div class="tags card">
                        <div class="title">
                            <h2>热门目的地</h2>
                        </div>
                        <div class="body">
                            <ul>
                                <li v-for="city in hotCity" :key="city.site_addr">
                                    <el-badge :value=city.count class="item">
                                        <a href="/" target="_blank">
                                            <b>
                                                <router-link
                                                    :to="{ name: 'search', params: { selectedPlace: city.site_addr } }">
                                                    <i>{{ city.site_addr | formatcity }}</i>
                                                </router-link>
                                            </b>
                                        </a>
                                    </el-badge>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="site_info card">
                        <div class="title">
                            <h2>旅行家日记</h2>
                        </div>
                        <div id="charts11" ref="graph"
                            style="width: 100%; height: 300px; background-color: white;border-radius: 0 0 5px 5px;">
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</template>

<script>
import HomeCarousel from '@/components/HomeCarousel.vue'
import '@fortawesome/fontawesome-free/css/all.css'
import { Allarticles, myArticleCount } from '../../api/article';
import { hotCity, hotSite, localhotSite, selectSitesByIds } from "@/api/site"
import { formatDate, formatTime } from '../../utils/time';
import { formatStarCounts } from "../../utils/data"
import { formatcity } from "../../utils/city"
import { userInfo, selectUsersByNames, getUserTravelDiary } from "@/api/user"
import {
    followAndFanCount,
} from "@/api/follow.js";
import { TopFiveAnnouncement } from "@/api/announcement"
const echarts = require('echarts/lib/echarts');
require('echarts/lib/component/title');
require('echarts/lib/component/tooltip');
require('echarts/lib/component/grid');
require('echarts/lib/component/legend');
require('echarts/lib/chart/bar');
export default {
    name: 'Home',
    components: {
        HomeCarousel,
    },
    data() {
        return {
            this_category: 'hot',
            totalPage: 0,
            articleCount: '',
            followCount: '',
            fanCount: '',
            nameList: [],
            idList: [],
            AboutSite: [],
            userData: [],
            isShow: false,
            hotCity: [],
            hotSite: [],
            localhotSite: [],
            queryInfo: {
                // 当前页数
                pagenum: 1,
                // 显示一页多少条数据
                pagesize: 10
            },
            MyInfo: {},
            items: [],
            articles: [],
        }
    },
    mounted() {
        // this.initCharts()
        this.load()
    },
    created() {
        this.$watch(
            () => this.$store.state,
            () => {
                this.CurrentInfo()
            },
            { deep: true }
        )
    },
    methods: {
        switch_article_category(category) {
            this.this_category = category
        },
        initCharts(data = "") {
            const d = this.$refs.graph
            const myChart = echarts.init(d);
            var option;

            //5-22
            if (data == "") {
                data = [0, 0, 0];
            }

            option = {
                color: ['rgba(0, 255, 238, 0.45)'],
                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    top: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'value',
                    boundaryGap: [0, 0.01]
                },
                yAxis: {
                    type: 'category',
                    data: ['我去过', '我聊过', '我想过']
                },
                series: [
                    {
                        type: 'bar',
                        data: data
                    }
                ]
            };
            myChart.resize()
            option && myChart.setOption(option);
        },
        async CurrentInfo() {
            if (this.$store.state.id) {
                try {
                    const res1 = await userInfo(this.$store.state.id, true)
                    this.MyInfo = res1.data
                    const res2 = await myArticleCount(this.$store.state.name)
                    this.articleCount = res2.data
                    const res3 = await followAndFanCount(this.$store.state.id)
                    this.followCount = res3.data.followCounts
                    this.fanCount = res3.data.fanCounts
                    const res4 = await getUserTravelDiary(this.$store.state.id);
                    this.initCharts(res4.data);
                } catch (err) {
                    console.log(err)
                }
            }
        },
        handleSizeChange(newsize) {
            //这里conso 选中第几页 最新的值
            //最新的条数（newsize）赋值给 动态的 pagesie
            this.queryInfo.pagesize = newsize
            //获取到最新一页显示的数据  重新发送axios请求 这里是封装好的请求方法
            this.partload()
        },
        handleCurrentChange(newPage) {
            //把最新的页码（newPage）赋值给 动态的 pagenum 
            this.isShow = false
            this.queryInfo.pagenum = newPage
            //获取到最新显示的页码值  重新发送axios请求 这里是封装好的请求方法
            this.partload()
        },
        async load() {
            await this.CurrentInfo()
            await this.partload()
            try {
                const res1 = await hotCity()
                this.hotCity = res1.data
                const res2 = await hotSite()
                this.hotSite = res2.data
                const res3 = await localhotSite(this.$store.state.addr)
                this.localhotSite = res3.data
                const res4 = await TopFiveAnnouncement()
                this.items = res4.data
            } catch (err) {
                console.log(err)
            }
        },
        async partload() {
            try {
                const res = await Allarticles(this.queryInfo)
                this.articles = res.data.list
                this.totalPage = res.data.total
                this.nameList = []
                this.idList = []
                res.data.list.forEach(element => {
                    this.nameList.push(element.articleAuthor)
                    this.idList.push(element.siteId)
                });

                const res1 = await selectUsersByNames(this.nameList)
                this.isShow = true
                this.userData = res1.data

                const res2 = await selectSitesByIds(this.idList)
                this.AboutSite = res2.data
            } catch (err) {
                console.log(err)
            }
        },

        ToSite(id) {
            this.$router.push(`/site?siteId=${id}`)
        },
        personal(id) {
            this.$router.push(`/personal/footprint/${id}`)
        },
        toAnnouncement(url) {
            window.location.href = url
        }

    }
}
</script>
<style scoped>
body {
    padding: 0;
    margin: 0;
}

.el-badge__content.el-badge__content--undefined.is-fixed {
    right: -4px;
}

.el-carousel__item:nth-child(2n) {
    background-color: #ffffff;
}

.el-carousel__item:nth-child(2n+1) {
    background-color: #ffffff;
}
</style>

<style scoped src="../../assets/css/home/index.css"></style>

