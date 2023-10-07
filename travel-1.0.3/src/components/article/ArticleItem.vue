<template>
  <el-card class="me-area" :body-style="{ padding: '16px' }">
    <div class="me-article-header">
      <a @click="view(articleNid)" class="me-article-title" style="cursor: pointer;">{{ articleTitle }}</a>
      <span class="me-pull-right me-article-count">
        <i class="el-icon-star-off"></i>&nbsp;{{ collectedCount }}
      </span>
      <span class="me-pull-right me-article-count">
        <i class="el-icon-chat-dot-round"></i>&nbsp;{{ commentCount }}
      </span>
      <span class="me-pull-right me-article-count">
        <i class="fa-regular fa-thumbs-up"></i>&nbsp;{{ starCount | formatStarCounts }}
      </span>
      <span class="me-pull-right me-article-count">
        <!-- <img style="height:13px;width:13px" src="../../../static/category/good.png"></img>&nbsp;{{goodCounts}} -->
      </span>
      <span class="me-pull-right me-article-count">
        <i class="el-icon-view"></i>&nbsp;{{ lookCount }}
      </span>
    </div>
    <div class="me-artile-description">
      {{ articleAbstract }}
    </div>
    <div class="preimg" v-if="coverUrl">
      <div style="margin-right:30px;width:120px" v-for="item in imgList">
        <img v-image-preview style="cursor:pointer;" :src="item">
      </div>
    </div>
    <div class="me-article-footer">
      <span class="me-article-author" @click="editArticle(articleNid)">
        <i class="el-icon-edit"></i>&nbsp;{{ articleAuthor }}
      </span>
      <el-tag v-for="t in tags" :key="t.articleTag" size="mini" type="warning">{{ t.articleTag }}</el-tag>
      <el-tag type="success" v-if="articleStatus===1" size="mini">已发布</el-tag>
      <el-tag type="danger" v-else size="mini">未审核</el-tag>
      <div class="me-pull-right me-article-time">
        <span class="me-pull-right me-article-count">
          <i class="el-icon-time"></i>&nbsp;{{ createDate | formatDate | formatTime  }}
        </span>
      </div>
    </div>

  </el-card>
</template>

<script>
import { formatDate, formatTime } from '../../utils/time';
import { formatStarCounts } from "../../utils/data"
import { getArticleTag } from "@/api/tag.js";
import { selectUserByName } from "@/api/user.js"
export default {
  name: "ArticleItem",
  props: {
    articleNid: Number,
    articleTitle: String,
    commentCount: Number,
    lookCount: Number,
    articleAbstract: String,
    articleAuthor: String,
    coverUrl: String,
    createDate: String,
    starCount: Number,
    collectedCount: Number,
    articleStatus: Number,
  },

  data() {
    return {
      imgList: [],
      tags: [],
    };
  },
  mounted() {
    if (this.coverUrl) {
      if (this.coverUrl.indexOf(",") != -1) {
        this.imgList = this.coverUrl.split(",");
      } else {
        this.imgList.push(this.coverUrl);
      }
    }
    getArticleTag(this.articleNid)
      .then(res => {
        this.tags = res.data;
      })
  },
  methods: {
    view(id) {
      this.$router.push({ path: `/article/${id}` });
    },
    editArticle(id){
      console.log(id)
    }
  },
};
</script>

<style scoped >
.me-article-header {
  /*padding: 10px 18px;*/
  padding-bottom: 10px;
}

.me-article-title {
  font-weight: 600;
}

.me-article-icon {
  padding: 3px 8px;
}

.me-article-count {
  color: #a6a6a6;
  padding-left: 20px;
  font-size: 13px;
}

.me-pull-right {
  float: right;
}

.me-artile-description {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  font-size: 13px;
  line-height: 24px;
  margin-bottom: 10px;
}

.me-article-author {
  color: #a6a6a6;
  padding-right: 18px;
  font-size: 13px;
  cursor: pointer;
}

.me-article-author-img {
  width: 15px;
  height: 15px;
}

.me-article-author-avatar {
  width: 30px;
  height: 30px;
  vertical-align: text-bottom;
  border-radius: 50%;
}

.el-tag {
  margin-left: 6px;
}

.me-article-time {
  margin-top: 20px;
}

.leftimg {
  /* width:11%;
  height:11%; */
  width: 100px;
  height: 90px;
  float: left;
  margin-right: 20px;
}

img {
  width: 100%;
  height: 100%;
}

.preimg {
  width: 700px;
  height: 90px;
  margin-bottom: 10px;
  display: flex;
}</style>
