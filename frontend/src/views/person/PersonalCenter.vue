<template>
  <div>
    <div class="PersonTop">
      <div class="PersonTop_img">
        <img v-image-preview :src="userAvatarUrl" />
      </div>
      <div class="PersonTop_text">
        <div class="user_text">
          <div class="user_name">
            <span> {{ userName }} </span>
            <img class="user_sex" v-if="userSex === '男'" src="../../assets/img/男性.svg" alt="">
            <img class="user_sex" v-else-if="userSex === '女'" src="../../assets/img/女性.svg" alt="">
            <img class="user_sex" v-else src="../../assets/img/性别.svg" alt="">
          </div>
          <div class="user_qianming">
            <span> {{ userDesc }}</span>
          </div>
          <div class="user_anniu">
            <el-button class="el-icon-edit" v-if="this.$route.params.id === this.$store.state.id"
              :key="this.$route.params.id" type="primary" size="medium" plain @click="edit">编辑</el-button>
            <el-button v-else @click="follow" type="primary" size="medium" icon="el-icon-check" v-text="myisfollowid.indexOf(parseInt(this.$route.params.id)) > -1 ? '已关注' : '关注'
              "></el-button>
          </div>
        </div>
        <div class="user_num">
          <div style="cursor: pointer">
            <div style="cursor: pointer" @click="myfan">
              <div class="num_number">{{ fanCounts }}</div>
              <span class="num_text">粉丝</span>
            </div>
          </div>
          <div style="cursor: pointer">
            <div style="cursor: pointer" @click="myfollow">
              <div class="num_number">{{ followCounts }}</div>
              <span class="num_text">关注</span>
            </div>
          </div>
          <div>
            <div class="num_number">{{ starCounts | formatStarCounts }}</div>
            <span class="num_text">获赞</span>
          </div>
        </div>
      </div>
    </div>
    <div class="person_body">
      <div class="person_body_left">
        <el-card class="box-card" :body-style="{ padding: '0px' }">
          <div slot="header" class="clearfix">
            <span class="person_body_list" style="border-bottom: none">个人中心</span>
          </div>
          <el-menu router active-text-color="#00c3ff" class="el-menu-vertical-demo">
            <el-menu-item index="footprint" :route="{ name: 'footprint', params: $route.params.id }">
              <i class="el-icon-location-outline"></i>
              <span slot="title">足迹</span>
            </el-menu-item>
            <el-menu-item v-show="$store.state.id===$route.params.id" index="mymessage" :route="{ name: 'mymessage', params: $route.params.id }">
              <i class="el-icon-bell"></i>
              <span slot="title">消息</span>
            </el-menu-item>
            <el-menu-item index="myarticle" :route="{ name: 'myarticle', params: $route.params.id }">
              <i class="el-icon-edit-outline"></i>
              <span slot="title">发帖</span>
            </el-menu-item>
            <el-menu-item index="mycollect" :route="{ name: 'mycollect', params: $route.params.id }">
              <i class="el-icon-document"></i>
              <span slot="title">收藏</span>
            </el-menu-item>
            <el-menu-item index="myfan" :route="{ name: 'myfan', params: $route.params.id }">
              <i class="el-icon-tableware"></i>
              <span slot="title">粉丝</span>
            </el-menu-item>
            <el-menu-item index="myfollow" :route="{ name: 'myfollow', params: $route.params.id }">
              <i class="el-icon-circle-plus-outline"></i>
              <span slot="title">关注</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </div>
      <div class="person_body_right">
        <router-view></router-view>
      </div>
    </div>
    <personal-dia ref="dia" @flesh="reload" />
  </div>
</template>
<script>
const ele = require('element-ui')
import { userInfo } from "@/api/user"
import { mystarCount } from "../../api/star";
import PersonalDia from "./PersonalDia.vue";
import formatStarCounts from "../../utils/data"

import {
  myFollow,
  addFollow,
  deleteFollow,
  followAndFanCount,
} from "@/api/follow.js";
export default {
  components: { PersonalDia },
  name: 'personal_page',
  inject: ["reload"],
  data() {
    return {
      fanCounts: "",
      editable: true,
      userAvatarUrl: "",
      userName: "",
      userSex: "",
      userDesc: "",
      dialogVisible: false,
      imgURL: '',
      followCounts: "",
      starCounts: "",
      isfollow: false,
      followData: {
        fanId: "",
        followId: "",
      },
      isfollowid: [],
      myisfollowid: [],
    }
  },
  watch: {
    $route(to, from) {
      if (to.path == `/personal/${this.$store.state.id}`) {
        this.reload();
      } else if (to.path == `/personal/${this.$route.params.id}`) {
        this.reload();
      }
    },
  },
  created() {
    if (localStorage.getItem('store')) {
      this.$store.replaceState(
        Object.assign(
          {},
          this.$store.state,
          JSON.parse(localStorage.getItem('store'))
        )
      )
    }
  },
  mounted() {
    if(this.myisfollowid.indexOf(this.$route.params.id)>-1){
      this.isfollow=true
    }
    this.load();
  },
  methods: {
    load() {
      userInfo(this.$route.params.id, false)
        .then(res => {
          if (res.success) {
            this.userAvatarUrl = res.data.userAvatarUrl;
            this.userName = res.data.userName;
            this.userSex = res.data.userSex;
            this.userDesc = res.data.userDesc;
          }
        })
        .catch((err) => {
          console.log(err);
        });
      mystarCount(this.$route.params.id)
        .then((res) => {
          this.starCounts = res.data;
        })
        .catch((err) => {
          console.log(err)
        })
        //当前登录用户的关注列表
      if (this.$store.state.id !== "") {
        myFollow(this.$store.state.id)
          .then((res) => {
            res.data.forEach((res) => {
              this.myisfollowid.push(res.userNid);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      }
      //当前路径用户的关注列表
      myFollow(this.$route.params.id)
        .then((res) => {
          res.data.forEach((res) => {
            this.isfollowid.push(res.userNid);
          });
        })
        .catch((err) => {
          console.log(err);
        });
        //关注数和粉丝数
      followAndFanCount(this.$route.params.id)
        .then((res) => {
          this.followCounts = res.data.followCounts;
          this.fanCounts = res.data.fanCounts;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    myfollow() {
      this.$router.push({
        path: `/personal/myfollow/${this.$route.params.id}`,
      });
    },
    myfan() {
      this.$router.push({
        path: `/personal/myfan/${this.$route.params.id}`,
      });
    },
    follow() {
      if (!this.$store.state.id) {
        this.$message({
          showClose: true,
          message: "请登录后再进行操作哦",
          type: "warning",
        });
      } else {
        this.followData.followId = this.$route.params.id;
        this.followData.fanId = this.$store.state.id;
        if (this.myisfollowid.indexOf(parseInt(this.followData.followId)) > -1) {
          this.isfollow = true;
        } else {
          this.isfollow = false;
        }
        if (this.isfollow) {
          deleteFollow(this.followData, this.$store.state.token)
            .then((res) => {
              this.isfollow = false;
              this.$message({
                showClose: true,
                message: "已取消关注",
                type: "success",
              });
              this.reload();
            })
            .catch((err) => {
              console.log(err);
            });
        } else if (!this.isfollow) {
          addFollow(this.followData, this.$store.state.token)
            .then((res) => {
              this.isfollow = true;
              this.myisfollowid.push(this.followData.followId)
              this.$message({
                showClose: true,
                message: "已成功关注",
                type: "success",
              });
              this.reload();
            })
            .catch((err) => {
              console.log(err);
            });
        }
      }
    },
    edit() {
      this.$refs.dia.open();
    },
  },
};
</script>
  
<style scoped>
.me-video-player {
  background-color: transparent;
  width: 100%;
  height: 100%;
  object-fit: fill;
  display: block;
  position: fixed;
  left: 0;
  z-index: 0;
  top: 0;
}

.PersonTop {
  width: 1000px;
  height: 140px;
  padding-top: 20px;
  background-color: #FFF;
  margin-top: 30px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1)
}

.PersonTop_img {
  width: 120px;
  height: 120px;
  background-color: #8c939d;
  margin-right: 24px;
  margin-left: 20px;
  overflow: hidden;
  border-radius: 50%;
}

.PersonTop_img img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.PersonTop_text {
  height: 120px;
  width: 880px;
  display: flex;
}

.user_text {
  width: 60%;
  height: 100%;
  line-height: 30px;
}

.user_name {
  font-weight: bold;
}

.user_sex {
  width: 20px;
}

.user-v {
  margin-bottom: -5px;
}

.user-v-img {
  width: 15px;
  height: 15px;
}

.user-v-font {
  font-size: 15px;
  color: #00c3ff;
}

.user_qianming {
  font-size: 14px;
  color: #999;
}

.user_num {
  width: 40%;
  height: 100%;
  display: flex;
  align-items: center;
}

.user_num>div {
  text-align: center;
  border-right: 1px dotted #999;
  box-sizing: border-box;
  width: 80px;
  height: 40px;
  line-height: 20px;
}

.num_text {
  color: #999;
}

.num_number {
  font-size: 20px;
  color: #333;
}

.el-menu-item>span {
  font-size: 16px;
  color: #999;
}

/*下面部分样式*/
.person_body {
  width: 1000px;
  margin-top: 210px;
  display: flex;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 5px;
}

.person_body_left {
  width: 27%;
  height: 600px;
  border-radius: 5px;
  margin-right: 3%;
  text-align: center;
}

.person_body_list {
  width: 100%;
  height: 50px;
  margin-top: 25px;
  font-size: 22px;
  border-bottom: 1px solid #f0f0f0;
  background-image: -webkit-linear-gradient(left,
      rgb(42, 134, 141),
      #e9e625dc 20%,
      #3498db 40%,
      #e74c3c 60%,
      #09ff009a 80%,
      rgba(82, 196, 204, 0.281) 100%);
  -webkit-text-fill-color: transparent;
  -webkit-background-clip: text;
  -webkit-background-size: 200% 100%;
  -webkit-animation: masked-animation 4s linear infinite;
}

.el-menu-item {
  margin-top: 22px;
}

.person_body_right {
  width: 70%;
  /* height: 500px; */
  border-radius: 5px;
  background-color: white;
}

.box-card {
  height: 580px;
}

/*ui样式*/
.el-button {
  width: 84px;
}
</style>
  