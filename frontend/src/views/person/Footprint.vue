<template>
  <div>
    <el-button v-show="this.$route.params.id === this.$store.state.id" type="text" class="select"
      @click="dialogVisible = true">添加</el-button>
    <el-dialog class="dia" title="选择你已打卡地" :visible.sync="dialogVisible" width="326px" :modal="false" top="3vh"
      :before-close="handleClose">
      <div>
        <span style="margin-right: 10px;">旅游地点</span>
        <region-selects :area="false" v-model="region" @change="change" />
      </div>
      <div class="myblock">
        <span class="demonstration">旅游日期</span>
        <el-date-picker v-model="travelTime" type="date" format="yyyy 年 M 月 d 日" value-format="yyyy-M-d"
          placeholder="选择日期">
        </el-date-picker>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addFootPrint()">确 定 添 加</el-button>
      </span>
    </el-dialog>
    <el-card id="mymap" style="height: 500px; width: 100%; background-color: #68b2d95c;">
    </el-card>
  </div>
</template>

<script>

const echarts = require('echarts/lib/echarts');
import china from "../../assets/map/china.json";
import cityFull from "../../assets/map/city.json";
import { GeoComponent } from 'echarts/components';
import { ScatterChart } from 'echarts/charts';
import { VisualMapComponent } from 'echarts/components';
import { RegionSelects } from 'v-region'
import { myfootprint, footprint } from "@/api/user"
import { Message } from "element-ui"
echarts.use([VisualMapComponent]);
echarts.use([ScatterChart]);
echarts.use([GeoComponent]);
export default {
  name: "Footprint",
  inject: ["reload"],
  components: {
    RegionSelects
  },
  data() {
    return {
      mydata: "",
      travelTime: '',
      warehouseList: [],
      // 需要高亮的省
      highlightAreaList: [],
      // option 也可以在这里定义
      region: {
        province: { key: String, value: String },
        city: { key: String, value: String },
        area: { key: String, value: String },
        town: { key: String, value: String }
      },
      dialogVisible: false
    }
  },
  mounted() {
    myfootprint(this.$route.params.id)
      .then((res) => {
        res.data.forEach((element) => {
          this.highlightAreaList.push(element.cityName.split('·')[0]);
          let cityName = element.cityName.split('·')[1]
          let tmp = cityName
          if (cityName.slice(-1) === '市') {
            tmp = cityName.substr(0, cityName.length - 1)
          }
          let item = {
            name: cityName,
            value: cityFull.city[tmp],
            time: element.travelTime,
          }
          this.warehouseList.push(item)
        })
      })
    setTimeout(() => {
      this.createChinaMap()
    }, 200)
  },
  methods: {
    createChinaMap() {
      echarts.registerMap('china', china);
      let myCharts = echarts.init(document.getElementById("mymap"));
      // 地图默认中心
      let defaultCenter = [117.283042, 31.86119]
      // 仓库列表（定位）

      // 标题

      let option = {
        myCharts: myCharts,
        warehouseList: this.warehouseList,
        highlightAreaList: this.highlightAreaList,
        defaultCenter: defaultCenter
      }
      this.setOption(option)
    },
    addFootPrint() {
      if (this.value === null) {
        Message({
          message: "未选择日期",
          type: "warning"
        })
      }
      if (this.mydata.province === undefined || this.mydata.city === undefined) {
        Message({
          message: "未选择城市",
          type: "warning"
        })
      } else {
        let provinceName = this.mydata.province.value
        let cityName = this.mydata.city.value
        let data = {
          cityName: provinceName + '·' + cityName,
          travelTime: this.travelTime
        }
        footprint(this.$route.params.id, data)
          .then((res) => {
            if (res.success) {
              this.reload()
              Message({
                message: res.data,
                type: "success"
              })
            }
            else {
              Message({
                message: res.msg,
                type: "error"
              })
            }
          })
      }
      this.dialogVisible = false
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => { });
    },
    /**
     * option = {
     *  myChart: echarts对象 必须
     *  warehouseList: 仓库信息
     *  highlightAreaList： 需要高亮的省
     *  center： 地图初始中心
     * }
     */
    setOption(option) {
      option.highlightAreaObjList = this.buildHighlightAreaObjList(option.highlightAreaList)
      option.myCharts.setOption({
        title: {
          text: option.title,
        },
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            if (Array.isArray(option.highlightAreaList) && option.highlightAreaList.indexOf(params.name) != -1) {
              return params.name + "<br>xxx仓库 159.3吨<br>xxx仓库 59.1吨"
            } else {
              return params.name
            }
          },
        },
        visualMap: {
          show: false,
        },
        geo: {
          // 指定区域设置样式和
          regions: option.highlightAreaObjList,
          center: option.center,
          // show: false,
          map: 'china',
          silent: false,
          itemStyle: {
            color: '#004981c9', //地图颜色   
            borderColor: '#064f85',// 边框颜色
            borderWidth: 1
          },
          label: {
            color: "rgb(0,0,0)", //文字颜色
            show: false,
            formatter: function (params) {
              return params.name
            },
            rich: {
            }
          },
          zoom: 1.2, //缩放
          roam: true, //鼠标可拖动、缩放
        },
        series: [
          {
            symbolSize: function (data) {
              return Math.sqrt(data[2]) / 5e2;
            },
            emphasis: {
              show: false,
              focus: 'item',
              itemStyle: {
                color: 'rgb(252, 1, 26)'
              }
            },
            tooltip: {
              formatter: function (params) {
                return params.name + " " + params.data.time
              }
            },
            type: 'scatter',
            coordinateSystem: 'geo',
            symbolSize: 15,
            symbol: 'image://data:image/png;base64,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',

            data: option.warehouseList

          },
        ]
      })
    },
    buildHighlightAreaObjList(highlightAreaList) {
      let objList = []
      if (Array.isArray(highlightAreaList)) {
        highlightAreaList.forEach(item => {
          objList.push({
            name: item,
            itemStyle: {
              color: 'red',
              areaColor: 'rgba(255, 223, 51,0.4)'
            }
          })
        })
      }
      return objList

    },
    change(data) {
      this.mydata = data
      console.log(this.mydata)
    }
  }
}
</script>

<style scoped lang="scss">
.img {
  width: 80px;
  height: 80px;
}

.dia {
  display: flex;
  max-height: 400px;
}

.select {
  position: absolute;
  z-index: 1;
  top: 20px;
  /* border: none; */
  left: 326px;
}

.el-dialog__body {
  padding: 20px 20px 0px 20px;

  >div {
    display: flex;
    margin-bottom: 20px;
    align-items: center;
  }

  .myblock {
    display: block;
  }

  .demonstration {
    margin-right: 10px;
  }
}
</style>