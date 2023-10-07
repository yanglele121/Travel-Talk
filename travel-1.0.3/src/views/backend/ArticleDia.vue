<template>
    <div>
        <el-dialog title="文章详情" :visible.sync="dialogVisible"  width="60%" :fullscreen="full">
            <i class="el-icon-full-screen" @click="isFull()"></i>
            <div class="content">
                <h2 style="text-align: center; margin-bottom: 10px;">{{ articleTitle }}</h2>
                <el-image style="width: 100%; height: 300px;" :src="coverUrl" fit="fill"></el-image>
                <el-card class="box-card" shadow="hover">
                    <i class="el-icon-reading" style="color:rgb(20, 162, 162); margin-right: 5px;"></i>
                    {{ articleAbstract }}
                </el-card>
                <div class="tagList" v-if="tags.length > 0">
                    <ul>
                        <li v-for="item in tags" :key="item.articleTag"><i class="fa-solid fa-tag"
                                style="color: #ff9e00;"></i>&nbsp;{{ item.articleTag }}</li>
                    </ul>
                </div>
                <markdown-it-vue class="md-body" :content="articleContent" />
            </div>
        </el-dialog>
    </div>
</template>
<script>
import MarkdownItVue from 'markdown-it-vue'
import { getArticleTag } from "@/api/tag.js";
export default {
    name: "ArticleDia",
    components: { MarkdownItVue },
    data() {
        return {
            dialogVisible: false,
            articleTitle: String,
            articleAbstract: String,
            articleAuthor: String,
            coverUrl: String,
            createDate: String,
            articleContent: String,
            tags:Array,
            full:false,
        }
    },
    methods: {
        isFull(){
            this.full=!this.full
        },
        async open(article) {
            this.full=false
            this.articleContent = article.articleContent
            this.articleAbstract=article.articleAbstract
            this.articleTitle = article.articleTitle
            this.coverUrl = article.coverUrl
            this.dialogVisible = true
            const tagRes =await getArticleTag(article.articleNid);
            this.tags = tagRes.data;
            console.log(tagRes)
        }
    }
}
</script>

<style lang="scss">
.md-body {
    
    margin-top: 20px;
    img {
        width: 100%;
    }
}

.el-dialog__body {
    height: 480px;
    overflow: auto;
}
.tagList {
        margin-top: 20px;
        border-radius: 5px;
        border: 1px dashed #d7d7d7;
        display: block;
        overflow: hidden;
        >ul {
            width: 100%;
            margin-left: 20px;

            li {
                width: 160px;
                margin-top: 18px;
                margin-bottom: 18px;
                float: left;
                display: inline;
                font-size: 18px;
                height: 22px;
                line-height: 22px;
                align-items: center;
                color: #666;
                padding-left: 5%;
                cursor: pointer;
            }
        }
    }
    .box-card{
        margin-top: 10px;
    }
</style>