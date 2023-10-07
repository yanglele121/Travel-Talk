<template>
<el-carousel :interval="4000" type="card" height="440px">
    <el-carousel-item v-for="item in recommendarticle" :key="item.articleNid">
        <img @click="view(item.articleNid)" class="banner-img" :src="item.coverUrl" alt="无法加载图片" style="object-fit: cover; width: 100%; height: 100%;" />
    </el-carousel-item>
</el-carousel>
</template>

<script>
import { RecommendArticle } from "@/api/article"
export default {
    name: 'HomeCarousel',
    data() {
        return {
            recommendarticle:[],
        }
    },
    mounted(){
        this.load()
    },
    methods:{
        // 获取推荐位文章
        load(){
            RecommendArticle()
            .then((res)=>{
                this.recommendarticle = res.data
            })
        },
        view(id){
            this.$router.push({path:`/article/${id}`})
        }
    }
}
</script>

<style>
.banner-img {
    width: 100%;
    /* height: 100%; */
    position: relative;
    object-fit: cover;
}


.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
}
</style>
