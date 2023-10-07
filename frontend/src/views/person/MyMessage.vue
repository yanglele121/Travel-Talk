<template>
  <div v-show="$store.state.id===$route.params.id" class="content">
    <el-card v-for="item in items" class="box-card">
      <h3>{{ item.title }}</h3>
      <span>{{ item.createDate|formatDate }}</span>
      <p>{{ item.content }}</p>
    </el-card>
  </div>
</template>

<script>
import { getMyNotification } from "@/api/notification"
import { formatDate } from "../../utils/time"
export default{
  name:'MyMessage',
  data(){
    return {
      items:[]
    }
  },
  mounted(){
    this.load()
  },
  methods: {
    async load(){
      const resList = await getMyNotification(this.$store.state.id)
      if(resList.success){
        this.items = resList.data
      }
    }
  }
}
</script>
<style lang="scss">
.content{
  height: 580px;
  display: flex;
  flex-wrap: wrap;
  justify-content: left;
  overflow: auto;
  .box-card{
    height: 200px;
    width: 98%;
    margin-bottom: 20px;
    p{
      margin-top: 10px;
    }
  }
}

</style>