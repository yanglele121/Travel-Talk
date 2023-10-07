<template>
  <div class="error-page">
    <img src="../../assets/img/logo.png" @click="go_home()" alt="" title="回到首页">
    <div class="error-code">404</div>
  </div>
</template>

<script>
export default
{
    name:"NotFound",
    methods:{
      go_home(){
        this.$router.push("/");
      }
    }
}
</script>
<style scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  flex-wrap: nowrap;
  width: 100%;
  height: 100vh;
  padding: 11rem 0;
  box-sizing: border-box;
}
img{
    cursor: pointer;
    margin-top: 80px;
  }
.error-code {
  font-size: 250px;
  font-weight: bolder;
  color: #000000;
}
</style>
