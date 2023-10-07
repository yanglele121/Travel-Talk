<template>
  <div class="fanorfollow_box">
    <div class="fanorfollow" v-for="(item, index) in allData">
      <div class="fanorfollow_left">
        <img class="fanorfollow_img" v-image-preview :src="item.userAvatarUrl" />
      </div>
      <div class="fanorfollow_info">
        <div class="fanorfollow_info_top">
          <span
            style="color: #666; max-width: 180px"
            @click="personal(item.userNid)"
            >{{ item.userName }}</span
          >
        </div>
        <div class="fanorfollow_info_bottom">
          <span @click="personal(item.userNid)">{{ item.userDesc }}</span>
        </div>
      </div>
      <div class="fanorfollow_botton">
        <el-button
          @click="follow(item.userNid)"
          type="primary"
          size="small"
          round
          icon="el-icon-check"
          v-text="myisfollowid.indexOf(item.userNid) > -1 ? '已关注' : '关注'"
        ></el-button>
      </div>
    </div>
    <div>
      <el-empty
        v-if="allData.length == 0"
        :image-size="250"
        description="这里什么都没有哟"
      ></el-empty>
    </div>
  </div>
</template>

<script>
import { myFollow, myFan, addFollow, deleteFollow } from "@/api/follow.js";

export default {
  name: "MyFanAndFollow",
  inject: ["reload"],
  data() {
    return {
      allData: [],
      isfollow: true,
      followData: {
        fanId: "",
        followId: "",
      },
      isfollowid: [],
      myisfollowid: []
    };
  },
  watch: {
    $route(to, from) {
      if (to.path == `/personal/myfan/${this.$route.params.id}`) {
        myFan(this.$route.params.id)
          .then((res) => {
            this.allData = res.data;
            myFollow(this.$route.params.id).then((res) => {
                res.data.forEach((element) => {
                  this.isfollowid.push(element.userNid);
                });
              });
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        myFollow(this.$route.params.id)
          .then((res) => {
            this.allData = res.data;
            res.data.forEach((element) => {
              this.isfollowid.push(element.userNid);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
  },
  mounted() {
    if(this.$store.state.id!==""){
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
    this.load();
  },
  methods: {
    load() {
      if (
        this.$route.path == `/personal/myfan/${this.$route.params.id}`
      ) {
        myFan(this.$route.params.id)
          .then((res) => {
            this.allData = res.data;
              myFollow(this.$route.params.id).then((res) => {
                res.data.forEach((element) => {
                  this.isfollowid.push(element.userNid);
                });
              });
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        myFollow(this.$route.params.id)
          .then((res) => {
            this.allData = res.data;
            res.data.forEach((element) => {
              this.isfollowid.push(element.userNid);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    follow(id) {
      if (!this.$store.state.id) {
        this.$message({
          showClose: true,
          message: "请登录后再进行操作哦",
          type: "warning",
        });
        return;
      }
      this.followData.followId = id;
      this.followData.fanId = this.$store.state.id;
      if (this.myisfollowid.indexOf(this.followData.followId) > -1) {
        this.isfollow = true;
      } else {
        this.isfollow = false;
      }
      if (this.isfollow) {
        deleteFollow(this.followData,this.$store.state.token)
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
        addFollow(this.followData,this.$store.state.token)
          .then((res) => {
            this.isfollow = true;
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
    },
    personal(id) {
      this.$router.push({ path: `/personal/${id}` });
    },
  },
};
</script>

<style>
.fanorfollow_box :hover {
  border-width: 1px;
  border-color: deepskyblue;
}
.fanorfollow {
  padding: 15px 40px 15px 30px;
  height: 50px;
  display: flex;
  align-items: center;
  border: 1px solid #ebebeb;
}
.fanorfollow :hover {
  border-width: 1px;
  border-color: deepskyblue;
}
.fanorfollow_left {
  width: 60px;
  height: 60px;
}
.fanorfollow_img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 1px solid #ebebeb;
  vertical-align: top;
}
.fanorfollow_info {
  display: inline-block;
  margin-left: 20px;
  -webkit-box-flex: 1;
  -ms-flex-positive: 1;
  flex-grow: 1;
  overflow: hidden;
}
.fanorfollow_info_top {
  display: inline-block;
  font-size: 10;
  line-height: 14px;
  vertical-align: top;
  cursor: pointer;
}
.fanorfollow_info_top :hover {
  color: deepskyblue;
}
.fanorfollow_info_bottom {
  line-height: 14px;
  color: #999;
  margin-top: 5px;
  cursor: pointer;
}
.fanorfollow_info_bottom :hover {
  color: deepskyblue;
}
</style>