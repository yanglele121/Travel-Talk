<template>
  <div class="content-box">
    <div class="content">
      <div class="container">
        <el-row class="first" :gutter="20">
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="card-content user" @click="toPath('consumers')">
                <div class="card-left">
                  <i class="el-icon-user"></i>
                </div>
                <div class="card-right">
                  <div class="card-num">{{ userCount }}</div>
                  <div>用户总数</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="card-content site" @click="toPath('sites')">
                <div class="card-left">
                  <i class="el-icon-wind-power"></i>
                </div>
                <div class="card-right">
                  <div class="card-num">{{ siteCount }}</div>
                  <div>景点总数</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="card-content article" @click="toPath('articles')">
                <div class="card-left">
                  <i class="el-icon-document"></i>
                </div>
                <div class="card-right">
                  <div class="card-num">{{ articleCount }}</div>
                  <div>文章总量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="card-content comment" @click="toPath('comments')">
                <div class="card-left">
                  <i class="el-icon-s-comment"></i>
                </div>
                <div class="card-right">
                  <div class="card-num">{{ commentCount }}</div>
                  <div>评论总量</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <h3>用户性别比例</h3>
            <el-card class="cav-info" shadow="hover" :body-style="{ padding: '0px' }" id="userSex">
            </el-card>
          </el-col>
          <el-col :span="12">
            <h3>七日用户活跃</h3>
            <el-card class="cav-info" shadow="hover" :body-style="{ padding: '0px' }" id="songStyle"></el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <h3>用户地区分布</h3>
            <el-card class="cav-info_1" shadow="hover" :body-style="{ padding: '0px' }" id="country"></el-card>
          </el-col>
          <el-col :span="12">
            <h3>热门打卡地统计</h3>
            <el-card class="cav-info_1" shadow="hover" :body-style="{ padding: '0px' }" id="singerSex"></el-card>
          </el-col>
        </el-row>

      </div>
    </div>
  </div>
</template>

<script>
const echarts = require('echarts/lib/echarts');
import china from "../../assets/map/china.json";
import cityFull from "../../assets/map/city.json";
import { GeoComponent } from 'echarts/components';
import { ScatterChart } from 'echarts/charts';
import { VisualMapComponent } from 'echarts/components';
import { ToolboxComponent } from 'echarts/components';
import { MapChart } from 'echarts/charts';
import { PieChart } from 'echarts/charts';
import { LineChart } from 'echarts/charts';
echarts.use([LineChart]);
echarts.use([PieChart]);
echarts.use([MapChart]);
echarts.use([ToolboxComponent]);
echarts.use([VisualMapComponent]);
echarts.use([ScatterChart]);
echarts.use([GeoComponent]);
import { getUserAddr, getUserToAddr, getUserSexRate, RecentLogin } from "@/api/user"
import { getSiteNum } from "@/api/site"
import { getCommentNum } from "@/api/comment"
import { getArticleNum } from "@/api/article"
import { formatProvince } from "../../utils/city.js"
import '@fortawesome/fontawesome-free/css/all.css'
export default {
  name: 'InfoPage',
  data() {
    return {
      userCount: 0,
      siteCount: 10000,
      articleCount: 10010,
      commentCount: 123123,
    }
  },
  mounted() {
    echarts.registerMap('china', china);
    this.createUserAreaMap();
    this.createHotAreaMap();
    this.createSexRate();
    this.createHotSite();
    this.getSiteNum();
    this.getArticleNum();
    this.getCommentNum();
  },
  methods: {
    toPath(path) {
      this.$router.push(path);
    },
    async createUserAreaMap() {
      try {
        let user_data = []
        const res = await getUserAddr();
        user_data = res.data
        let dMax = -1
        user_data.forEach(element => {
          element.name = formatProvince(element.name);
          dMax=dMax>element.value?dMax:element.value
        });
        const myCharts = this.initMap('country');
        const option = {
          myCharts: myCharts,
          title: "用户地址统计",
          data: user_data,
          name: "用户地址",
          min: 0,
          max: dMax
        };
        this.setMapOption(option.myCharts, option.title, option.data, option.name, option.min, option.max);
      } catch (error) {
        console.error(error);
      }
    },
    async createHotAreaMap() {
      try {
        const hotData = await getUserToAddr();
        const myCharts = this.initMap('singerSex');
        let dMax=-1;
        hotData.data.forEach(element=>{
          dMax=dMax>element.value?dMax:element.value
        })
        const option = {
          myCharts: myCharts,
          title: "热门省份统计",
          data: hotData.data,
          name: '热门省份',
          min: 0,
          max: dMax
        };
        this.setMapOption(option.myCharts, option.title, option.data, option.name, option.min, option.max);
      } catch (error) {
        console.error(error);
      }
    },
    async createSexRate() {
      try {
        let resdata = []
        let data = [];
        let category = [];
        let res = await getUserSexRate();
        resdata = res.data
        resdata.forEach((element) => {
          category.push(element.name)
          data.push(element.value)
          this.userCount += element.value
        })

        const myCharts = this.initMap('userSex');
        const option = {
          title: {
            text: "各性别用户占比",
            subtext: "截至今日网站用户人数:" + this.userCount,
            left: 'right'
          },
          toolbox: {
          show: true,
          left: 'left',
          top: 'top',
          feature: {
            dataView: { readOnly: false },
            restore: {},
            saveAsImage: {}
          }
        },
          legend: {},
          tooltip: {
            trigger: 'axis',
            showContent: true
          },
          grid: [
            { top: '20%', right: '45%', left: '5%', height: '70%' }, // 调整折线图的宽度
            { top: '10%', right: '5%', left: '60%', height: '80%' } // 调整饼图的位置和宽度
          ],
          xAxis: {
            type: "value",
            data: data
          },
          yAxis: {
            type: "category",
            data: category
          },
          series: [
            {
              type: 'bar',
              barWidth: 30,
              data: data,
              itemStyle: {
                color: function (params) {
                  //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                  var colorList = ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622'];
                  return colorList[params.dataIndex]
                },
              },
              seriesLayoutBy: 'column'
            },

            {
              name: 'Pie',
              type: 'pie',
              id: 'pie',
              radius: ['25%', '50%'],
              center: ['80%', '50%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              itemStyle: {
                color: function (params) {
                  //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                  var colorList = ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622'];
                  return colorList[params.dataIndex]
                },
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 40,
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: res.data,
            }
          ]
        };
        myCharts.on('updateAxisPointer', function (event) {
          const xAxisInfo = event.axesInfo[0];
          if (xAxisInfo) {
            const dimension = xAxisInfo.value + 1;
            myCharts.setOption({
              series: {
                id: 'pie',
                label: {
                  formatter: '{b}: {@[' + dimension + ']} ({d}%)'
                },
                encode: {
                  value: dimension,
                  tooltip: dimension
                },
              }
            });
          }
        })
        myCharts.setOption(option);
      } catch (error) {
        console.error(error);
      }
    },
    async createHotSite() {
      try {
        const res =await RecentLogin()
        const myCharts = this.initMap("songStyle")
        let option;
        // 配置信息
        option = {
          title: {
            text: '七日用户活跃',
            left: 'right'
          },
          
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              label: {
                backgroundColor: '#6a7985'
              }
            }
          },
          legend: {
            data: ['登录', '注册']
          },
          toolbox: {
          show: true,
          left: 'left',
          top: 'top',
          feature: {
            dataView: { readOnly: false },
            restore: {},
            saveAsImage: {}
          }
        },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              boundaryGap: false,
              data: res.data.date
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          // 数据可以从后端去请求
          series: [
            {
              name: '登录',
              type: 'line',
              stack: 'Total',
              areaStyle: {},
              smooth: true,//设置折线图平滑
              emphasis: {
                focus: 'series'
              },
              data: res.data.login_count
            },
            {
              name: '注册',
              type: 'line',
              stack: 'Total',
              smooth: true,//设置折线图平滑
              areaStyle: {},
              emphasis: {
                focus: 'series'
              },
              data: res.data.sign_count
            },
          ]
        };
        myCharts.setOption(option);
      } catch (error) {
        console.error(error);
      }
    },
    async getSiteNum(){
      try{
        const res =await getSiteNum()
        this.siteCount = res.data
      }catch(error){
        console.error(error)
      }
    },
    async getArticleNum(){
      try{
        const res =await getArticleNum()
        this.articleCount = res.data
      }catch(error){
        console.error(error)
      }
    },
    async getCommentNum(){
      try{
        const res =await getCommentNum()
        this.commentCount = res.data
      }catch(error){
        console.error(error)
      }
    },
    initMap(elementId) {
      const myCharts = echarts.init(document.getElementById(elementId));
      return myCharts;
    },
    setMapOption(myCharts, title, data, name, min, max) {
      myCharts.setOption({
        title: {
          text: title,
          left: 'right'
        },
        tooltip: {
          trigger: 'item',
          showDelay: 0,
          transitionDuration: 0.2,
          padding: [10, 15],
          backgroundColor: '#fff',
          borderWidth: .25, //区域边框宽度
          borderColor: '#2882f5', //区域边框颜色
        },
        visualMap: {
          left: 'right',
          min: min,
          max: max,
          inRange: {
            color: [
              '#313695',
              '#4575b4',
              '#74add1',
              '#abd9e9',
              '#e0f3f8',
              '#ffffbf',
              '#fee090',
              '#fdae61',
              '#f46d43',
              '#d73027',
              '#a50026'
            ]
          },
          text: ['High', 'Low'],
          calculable: true
        },
        toolbox: {
          show: true,
          left: 'left',
          top: 'top',
          feature: {
            dataView: { readOnly: false },
            restore: {},
            saveAsImage: {}
          }
        },
        geo: {
          map: 'china',
          //aspectScale: 0.75, //长宽比  
          zoom: 1.2,
          roam: false,
          itemStyle: {
            borderWidth: 2, //设置外层边框
            borderColor: '#168eff',
            shadowColor: '#a3d3ff', //底色阴影
            shadowOffsetX: 0,
            shadowOffsetY: 10,
            emphasis: {
              // show: false,
            }
          }
        },
        series: [
          {
            name: name,
            type: 'map',
            zoom: 1.2,
            roam: false,
            map: 'china',
            emphasis: {
              label: {
                show: true
              }
            },
            itemStyle: {
              borderWidth: .25, //区域边框宽度
              borderColor: '#168eff', //区域边框颜色
              areaColor: "#f4faff", //区域颜色
              emphasis: {
                borderWidth: .5, //鼠标滑过区域，区域边框宽度
                areaColor: "#168eff", //鼠标滑过区域背景色
                borderWidth: 1, //鼠标划过区域 区域边框
              }
            },
            data: data
          }
        ]
      });
    },

  }
}

window.addEventListener("resize", function () {
  echarts.init(document.getElementById('userSex')).resize();
  echarts.init(document.getElementById('country')).resize();
  echarts.init(document.getElementById('singerSex')).resize();
  echarts.init(document.getElementById('songStyle')).resize();
});

</script>

<style scoped src="../../assets/css/infoPage/infoPage.css"></style>
