<template>
  <div>
    <Header></Header>
    <Aside></Aside>
  </div>

</template>

<script>

import Header from "../../components/Header.vue";
import Aside from "../../components/Aside.vue";
export default {
  name: 'AdminHome',
  components: {
        Header,Aside
    },
}

</script>

<style scoped>

</style>
