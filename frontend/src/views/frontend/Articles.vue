<template>
  <div>
    <div>
      <img class="articleUrl" :src="article.coverUrl" />
    </div>

    <div class="article">
      <div>
        <el-container class="head">
          <div><img class="head-left" :src="author.userAvatarUrl" @click="personal(author.userNid)" /> </div>
          <div class="head-right">
            <div class="title">
              <h1 style="white-space:nowrap; overflow-wrap:normal"> {{ article.articleTitle }}</h1>
            </div>
            <div class="userinfo">
              <div class="user">
                <i class="username" style="font-style: normal;" @click="personal(author.userNid)">{{ article.articleAuthor
                }}</i>
                <el-button v-show="author.userNid && $store.state.id !== author.userNid.toString()" type="primary"
                  size="medium" icon="el-icon-check" v-text="isFollowed ? '已关注' : '关注'"
                  @click="changeFollow()"></el-button>
              </div>
            </div>
            <div><span style="margin-right: 10px;"><i class="fa-solid fa-eye" style="margin-right: 5px;"></i>{{
              article.lookCount | formatStarCounts
            }}</span>
              <span style="margin-right: 10px;"><i class="fa-solid fa-heart" style="margin-right: 5px;"
                  :class="{ 'liked': liked }" @click="changeArticleLike()"></i>
                {{ article.starCount | formatStarCounts }}</span>

              <span style="margin-right: 10px;"> <i class="fa-solid fa-star" style="margin-right: 5px;"
                  :class="{ 'liked': collected }" @click="changeArticleCollect()"></i>{{
                    article.collectedCount }} </span>
              <span style="margin-right: 10px;"><i class="fa-solid fa-clock" style="margin-right: 5px;"></i>{{
                article.createDate | formatTime }}</span>
            </div>
          </div>
        </el-container>
        <div class="tagList" v-if="tags.length > 0">
          <ul>
            <li v-for="item in tags" :key="item.articleTag"><i class="fa-solid fa-tag" style="color: #ff9e00;"></i>&nbsp;
              <router-link :to="{ name: 'search', params: { tagId: item.tagArticleNid } }">{{ item.articleTag
              }}</router-link>
            </li>
          </ul>
        </div>
        <el-container class="body">
          <el-card class="body-left">

            <div class="content">
              <markdown-it-vue class="md-body" :content="articleContent" :options="mdOptions" />
            </div>
            <el-container class="bottom">

              <div class="bottom-head">
                <!-- 评论区域如果当前用户有头像则显示头像,没有则显示默认头像 -->
                <img class="comment-title" :src="userAvatarUrl" />

                <!-- 评论框 -->
                <textarea class="bottom-head-input" type="text" placeholder="只是一直在等你而已，才不是想被评论呢～" ref="input"
                  v-model="firstComments"></textarea>
                <!-- 发布按钮 -->
                <button class="bottom-head-button" @click="addRootComment()"
                  :style="{ backgroundColor: bgColor }">发表评论</button>
              </div>

              <div class="bottom-content">
                <!-- 一级评论 -->
                <div class="first" v-for="(item, commentId) in comments" :key="item.commentId">
                  <div class="first-head">
                    <div class="first-head-left">
                      <img class="comment-title" :src="item.userAvatarUrl" alt="" />
                    </div>
                    <div class="first-head-right">

                      <!-- 一级评论用户昵称 -->
                      <div class="first-username">{{ item.username }}</div>

                      <!-- 一级评论评论内容 -->
                      <div class="first-comment">
                        {{ item.content }}
                      </div>
                      <div class="first-time">
                        <!-- 一级评论发布时间 -->
                        <span class="time">{{ item.date | formatTime }}</span>
                        <div>
                          <span><i class="fa-solid fa-comment" @click="comment_input(item)"
                              style="margin-right: 5px;"></i>{{ item.comment_num }}</span>
                          <span><i class="fa-solid fa-thumbs-up" :class="{ 'liked': item.liked }"
                              @click="changeCommentLike(item)" style="margin-right: 5px;"></i>{{ item.like_number
                              }}</span>

                          <span
                            v-if="$store.state.id === item.user_id.toString() || $store.state.id === author.userNid.toString()"><i
                              class="fa-sharp fa-solid fa-trash" @click="comment_delete(item.commentId)"
                              style="margin-left: 15px;"></i></span>
                        </div>
                      </div>
                      <div>
                        <span @click="changeFallbacked(item)"
                          style="cursor: pointer;margin-bottom: 10px;color: blue; ">共{{ item.comment_num }}条回复</span>
                      </div>


                      <!-- 回复一级评论 -->
                      <div class="reply-comment" v-if="lastReplyId == item.commentId">
                        <img class="comment-avatar" :src="userAvatarUrl" />
                        <textarea type="text" class="text" placeholder=" 请输入评论. . ." v-model="childComments"></textarea>

                        <!-- 0为回复一级评论 -->
                        <button class="button" @click="addReply(item.commentId)"
                          :style="{ backgroundColor: bgColor1 }">发表评论</button>
                      </div>
                    </div>
                  </div>

                  <div class="first-body">
                    <!-- 次级评论 -->
                    <div class="second" v-if="item.fallbacked">
                      <ul>
                        <li class="second-index" v-for="(sons, index) in item.sonCommentList" :key="sons.commentId">
                          <div class="second-top">
                            <!-- 次级评论头像,该用户没有头像则显示默认头像 -->
                            <div class="second-top-left">
                              <img class="second-img" :src="sons.userAvatarUrl" />
                            </div>
                            <div class="second-top-right">
                              <!-- 次级评论用户昵称 -->

                              <!-- 次级评论内容 xxx回复xxx：评论内容-->
                              <div class="second-comment">
                                <span class="second-username">{{ sons.username }}</span>
                                <span class="second-reply">&nbsp;回复<span class="to_reply">{{ sons.to_username }} : </span>
                                </span>
                                <p>{{ sons.content }}</p>
                              </div>
                              <div class="second-time">
                                <!-- 次级评论评论时间 -->
                                <span class="time">{{ sons.date | formatTime }}</span>
                                <!-- 次级评论评论 -->
                                <span><i class="fa-solid fa-comment" @click="sons_comment_input(sons)"
                                    style="margin-right: 5px;"></i></span>
                                <!-- 次级评论点赞 -->
                                <span><i class="fa-solid fa-thumbs-up" :class="{ 'liked': sons.liked }"
                                    @click="changeCommentLike(sons)" style="margin-right: 5px;"></i>{{ sons.like_number
                                    }}</span>
                                <span
                                  v-if="$store.state.id === sons.user_id.toString() || $store.state.id === author.userNid.toString()"><i
                                    class="fa-sharp fa-solid fa-trash" @click="comment_delete(sons.commentId)"
                                    style="margin-left: 15px;"></i></span>

                              </div>

                            </div>

                          </div>

                          <!-- 回复次级评论 -->
                          <div class="reply-comment reply_li" v-if="lastReplyId == sons.commentId" style="display: flex;">
                            <img class="comment-avatar" :src="userAvatarUrl" />
                            <textarea type="text" placeholder="尊重是评论打动人心的入场券" v-model="childComments"></textarea>
                            <!-- 1为回复次级评论 -->
                            <button @click="addReply(item.commentId)" :style="{ backgroundColor: bgColor1 }">发表评论</button>
                          </div>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </el-container>

          </el-card>

          <div class="body-right" id="bodyRight" ref="pronbit">
            <div>
              <div>
                <p>相关目的地：{{ site.siteAddr }}</p>
              </div>
              <img class="siteUrl" :src="site.sitePicUrl" @click="ToSite(site.siteNid)" style="cursor: pointer;">
              <div class="siteName">{{ site.siteName }}</div>
            </div>
            <div class="float-window">
              <div class="info">
                <p>相关游记</p>
                <el-carousel class="infooutside1" indicator-position="outside1">
                  <el-empty v-if="relatedArticleList.length === 0" description="暂无推荐内容" style="height: 170px"></el-empty>
                  <el-carousel-item v-else v-for="item in relatedArticleList" :key="item.articleNid">
                    <i class="fa-solid fa-eye">
                      {{ item.lookCount }}
                    </i>
                    <!-- </i> -->
                    <a :href="'/article/' + item.articleNid">
                      <img class="infoImg" :src="item.coverUrl">
                    </a>
                    <i class="infoArticleTitle">{{ item.articleTitle }}</i>
                  </el-carousel-item>
                </el-carousel>
              </div>
              <div class="articleSliderlist" id="el-scrollbar" ref="slider">
                <p>文章目录</p>

                <el-scrollbar style="height:300px;" id="el_scrollbar">
                  <div class="sliderlisr" v-for="(item, index) in slider_list" @click="go_side_bar(item.pos, $event)">
                    <span class="slider">{{ index + 1 }}/&nbsp;&nbsp;&nbsp;{{ item.c }}</span>
                  </div>
                </el-scrollbar>

              </div>

            </div>

          </div>

        </el-container>

      </div>

    </div>

  </div>
</template>

<script>
const ele = require('element-ui')
import MarkdownItVue from 'markdown-it-vue'
import 'markdown-it-vue/dist/markdown-it-vue.css'
import '@fortawesome/fontawesome-free/css/all.css'
import $ from "jquery"
import { formatDate, formatTime } from '../../utils/time'
import { formatStarCounts } from '../../utils/data'
import { getArticleDetailsById, getArticleById, getRelatedArticle } from "@/api/article.js"
import { addCommentLike, delCommentLike, addArticleLike, delArticleLike, addArticleCollect, delArticleCollect } from "@/api/like.js"
import { getArticleTag } from "@/api/tag.js";
import { addFollow, deleteFollow } from "@/api/follow.js";
import { SiteById } from "@/api/site.js";
import { addComment, deleteCommentByParentNid } from "@/api/comment.js"
function getDisTop(elem) {

  let realTop = elem.offsetTop;
  let parent = elem.offsetParent;
  while (parent !== null) {
    realTop += parent.offsetTop;
    parent = parent.offsetParent
  }
  return realTop
}

export default
  {
    name: 'Articles',
    components: {
      MarkdownItVue
    },
    data() {
      return {
        //后端交互需要的参数
        queryInfo: {
          articleNid: 0,
          userNid: 0
        },
        mdOptions: {
          html: true, // 允许解析 HTML 标签
          linkify: true, // 自动识别链接
          // 其他配置选项...
        },

        firstComments: "",//v-model
        childComments: "",//v-model

        //当前用户头像（created时赋值）
        userAvatarUrl: "",

        //已赋值：
        //文章信息
        article: {},
        //文章内容
        articleContent: "",
        //文章作者
        author: "",
        //文章标签
        tags: [],
        //文章目录
        slider_list: [],
        //二级回复窗口当前所在评论的id
        lastReplyId: -1,

        fallbackedList: {},


        //getArticleDetailsById获取以下信息：
        //当前用户是否关注了文章作者
        isFollowed: false,
        liked: false,//点赞状态
        collected: false,//收藏状态
        //文章评论
        comments: [],

        //另外获取
        //博主相关文章
        relatedArticleList: [],
        //相关景点
        siteUrl: "https://img.zcool.cn/community/01943d5ca2ae2ba801214168eac01a.jpg@1280w_1l_2o_100sh.jpg",
        siteName: "鼋头渚",
        site: "",
        //评论结构体，添加评论时传递到后端
        commentForm: {
          commentContent: "",
          userId: "",
          articleId: "",
          parentCommentId: "",
          rootCommentId: "",
        },
      }
    },
    methods: {

      debug() {
        console.log("clicked");
      },
      //删除评论
      async comment_delete(commentId) {
        try {
          await deleteCommentByParentNid(commentId);
          this.load(false);
        } catch (error) {
          console.error(`Failed to delete comment ${commentId}: ${error.message}`);
        }
      },
      //更改下拉显示子评论
      changeFallbacked(item) {
        const commentId = item.commentId.toString();
        if (commentId in this.fallbackedList) {
          this.fallbackedList[commentId] = !this.fallbackedList[commentId];
        } else {
          this.fallbackedList[commentId] = true;
        }
        item.fallbacked = this.fallbackedList[commentId];
      },
      //继承下拉子评论是否显示（用于不整体刷新界面，但是reload的时候）
      inheritanceFallbackedList(item) {
        const commentId = item.commentId.toString();
        if (commentId in this.fallbackedList) {

        } else {
          this.fallbackedList[commentId] = false;
        }
        item.fallbacked = this.fallbackedList[commentId];
      },
      //根据用户选择调整二级回复窗口当前所在评论
      comment_input(item) {
        this.lastReplyId = item.commentId;
        this.childComments = "";
        return;
      },
      sons_comment_input(sons) {
        this.lastReplyId = sons.commentId;
        this.childComments = "";
        return;
      },
      //添加根评论
      async addRootComment() {
        try {
          if (this.firstComments == "" || this.commentForm.userId == "") {
            return;
          }
          this.commentForm.commentContent = this.firstComments;
          this.commentForm.parentCommentId = 0;
          this.commentForm.rootCommentId = 0;
          await addComment(this.commentForm, this.$store.state.token);
          this.load(false);
          this.firstComments = "";
        } catch (error) {
          console.log(error);
        }
      },
      //添加子评论
      async addReply(rootCommentId) {
        try {
          if (this.childComments == "" || this.commentForm.userId == "") {
            return;
          }
          this.commentForm.commentContent = this.childComments;
          this.commentForm.parentCommentId = this.lastReplyId;
          this.commentForm.rootCommentId = rootCommentId;
          await addComment(this.commentForm, this.$store.state.token);
          this.load(false);
          this.childComments = "";
        } catch (error) {
          console.log(error);
        }
      },
      //评论点赞
      changeCommentLike(item) {
        if (this.commentForm.userId == "") {
          return;
        }
        if (item.liked) {
          item.liked = false;
          item.like_number--;
          delCommentLike({
            userId: this.commentForm.userId,
            commentId: item.commentId
          });
        } else {
          item.liked = true;
          item.like_number++;
          addCommentLike({
            userId: this.commentForm.userId,
            commentId: item.commentId
          });
        }
      },
      //文章点赞
      changeArticleLike() {
        if (this.commentForm.userId == "") {
          return;
        }
        if (this.liked) {
          this.liked = false;
          this.article.starCount--;
          delArticleLike({
            userId: this.$store.state.id,
            articleId: this.article.articleNid
          });
        } else {
          this.liked = true;
          this.article.starCount++;
          addArticleLike({
            userId: this.$store.state.id,
            articleId: this.article.articleNid
          });
        }
      },
      //文章收藏
      changeArticleCollect() {
        if (this.commentForm.userId == "" || this.commentForm.userId == this.author.userNid) {
          return;
        }
        if (this.collected) {
          this.$confirm('是否取消收藏?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            delArticleCollect({
              userId: this.$store.state.id,
              articleId: this.article.articleNid
            }).then(res => {
              if (res.success) {
                this.collected = false;
                this.article.collectedCount--;
                this.$message({
                  type: 'success',
                  message: '取消收藏成功!'
                });
              }
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消'
            });
          });

        } else {
          this.collected = true;
          this.article.collectedCount++;
          addArticleCollect({
            userId: this.$store.state.id,
            articleId: this.article.articleNid
          }).then(res=>{
            if(res.success){
              this.$message({
              type: 'success',
              message: '收藏成功！'
            });
            }
          });
        }
      },
      //用户关注博主
      changeFollow() {
        if (this.commentForm.userId == "" || this.commentForm.userId == this.author.userNid) {
          return;
        }
        if (this.isFollowed) {
          this.$confirm('确认取消关注？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteFollow({
              fanId: this.commentForm.userId,
              followId: this.author.userNid
            }, this.$store.state.token).then(res => {
              if (res.success) {
                this.isFollowed = false;
                this.$message({
                  type: 'success',
                  message: '取关成功!'
                });
              }
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消'
            });
          });
        } else {
          addFollow({
            fanId: this.commentForm.userId,
            followId: this.author.userNid
          }, this.$store.state.token).then(res => {
            if (res.success) {
              this.isFollowed = true;
              this.$message({
                type: 'success',
                message: '关注成功'
              })
            }
          })
        }
      },
      //获取文章
      async load(isInit) {
        try {
          if (isInit) {
            //获取文章信息和作者信息
            const res = await getArticleById(this.$route.params.id);
            this.article = res.data.article;
            this.articleContent = res.data.article.articleContent;
            this.author = res.data.author;
            //获取文章标签
            const tagRes = await getArticleTag(this.article.articleNid);
            this.tags = tagRes.data;
            //获取博主相关文章
            const articleList = await getRelatedArticle({
              authorId: this.author.userNid,
              articleId: this.article.articleNid
            });
            this.relatedArticleList = articleList.data;
            //获取相关景点
            const site = await SiteById(this.article.siteId);
            this.site = site.data
          }
          //获取其余信息：
          const ans = await getArticleDetailsById(this.queryInfo);
          this.isFollowed = ans.data.followed;
          this.liked = ans.data.liked;
          this.collected = ans.data.collected;
          this.comments = ans.data.fatherCommentList;
          for (let i = 0; i < this.comments.length; i++) {
            this.inheritanceFallbackedList(this.comments[i]);
          }
        } catch (err) {
          ele.Message({
            message: err,
            type: "error",
          });
          this.$router.push("/404");
        }
      },
      ToSite(id) {
        this.$router.push(`/site?siteId=${id}`)
      },
      personal(authorId) {
        this.$router.push({ path: `/personal/${authorId}` });
      },

      get_sliderbar() {
        const headings = $('.markdown-body').find('h1,h2,h3,h4');
        const slider_list = headings.map((index, heading) => ({
          tagName: heading.tagName,
          c: heading.innerText,
          pos: `#${heading.id}`
        })).get();
        slider_list.push({
          tagName: 'H1',
          c: '去评论吧!',
          pos: '.bottom-head'
        });
        this.slider_list = slider_list;
      },
      go_side_bar(selector, e) {
        $('.articleSliderlist .sliderlisr span').css('color', '');
        const top = getDisTop($(selector)[0]);
        e.target.style.color = '#ff9e00';
        $('html, body').animate({ scrollTop: top - 80 }, 1000);
      },
      init_scroll() {
        const sliderbar = document.querySelector('.float-window');
        const isArticlePage = location.pathname.includes('article');
        const top1 = isArticlePage ? $(sliderbar).offset().top : 0;
        window.addEventListener('scroll', function () {
          const top = document.documentElement.scrollTop;
          if (sliderbar) {
            sliderbar.classList.toggle('fixed', top >= top1);
          }
        });
      },
    },
    computed: {
      bgColor: function () {
        return this.firstComments === "" ? 'rgb(118, 211, 248)' : " #409EFF";
      },
      bgColor1: function () {
        return this.childComments === "" ? 'rgb(118, 211, 248)' : " #409EFF";
      },
    },
    created() {
      this.userAvatarUrl = this.$store.state.avatar
      this.queryInfo.articleNid = this.$route.params.id
      this.queryInfo.userNid = this.$store.state.id
      this.load(true)
      this.commentForm.articleId = this.$route.params.id
      this.commentForm.userId = this.$store.state.id
    },
    mounted() {
      setTimeout(this.get_sliderbar, 200);
      if (!location.pathname.includes('search')) {
        setTimeout(this.init_scroll, 100);
      }
    }
  }

</script>
<style scoped src="../../assets/css/article/article.css"></style>
<style>
.liked {
  color: red;
}

.unAttentioned,
.unAttentioned:focus,
.unAttentioned.is-active,
.unAttentioned:active {
  background-color: #409EFF;
}

.unAttentioned:hover {
  background: #3a90e5;
  border-color: #409EFF;
  color: #FFF;
}

.Attentioned,
.Attentioned:focus,
.Attentioned.is-active,
.Attentioned:active {
  background-color: #626567;
}

.Attentioned:hover {
  background: #626567;
  border-color: #409EFF;
  color: #FFF;
}

.el-carousel__container {
  height: 210px;
}
</style>