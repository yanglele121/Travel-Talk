package com.example.travel.controller;

import com.example.travel.bean.ArticleBean;
import com.example.travel.bean.CommentBean;
import com.example.travel.bean.UserBean;
import com.example.travel.service.*;
import com.example.travel.utils.UserThreadLocal;
import com.example.travel.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.travel.utils.ArticleRankingUtil.calculateScore;
import static java.lang.Math.min;

@RestController
@RequestMapping("/article")

public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;
    @Autowired
    TagService tagService;
    @Autowired
    FollowService followService;
    @Autowired
    UserLikeArticleService userLikeArticleService;
    @Autowired
    UserLikeCommentService userLikeCommentService;
    @Autowired
    CollectService collectService;
    @Autowired
    CommentService commentService;

    private ArticleBean coverAllAttribute(Map<String, Object> params, ArticleBean articleBean, boolean isCreate) {

        if (params.get("articleNid") != null) {
            articleBean.setArticleNid(Integer.parseInt(params.get("articleNid").toString()));
        }
        if (params.get("articleTitle") != null) {
            articleBean.setArticleTitle(params.get("articleTitle").toString());
        }
        if (params.get("articleAbstract") != null) {
            articleBean.setArticleAbstract(params.get("articleAbstract").toString());
        }
        if (params.get("articleContent") != null) {
            articleBean.setArticleContent(params.get("articleContent").toString());
        }
        if (params.get("articleAuthor") != null) {
            articleBean.setArticleAuthor(params.get("articleAuthor").toString());
        }
        if (params.get("coverUrl") != null) {
            articleBean.setCoverUrl(params.get("coverUrl").toString());
        } else {
            if (isCreate) {
                //待修，默认封面
                articleBean.setCoverUrl("http://localhost:8080/upload/userAvatarUrl/default.jpg");
            }
        }
        //对可为空/前端可能不传的字段的判断
        if (params.get("articleRecommend") != null) {
            articleBean.setArticleRecommend(Integer.parseInt(params.get("articleRecommend").toString()));
        } else {
            if (isCreate) {
                //默认值都以这种方式设置
                articleBean.setArticleRecommend(0);
            }
        }
        if (params.get("lookCount") != null) {
            articleBean.setLookCount(Integer.parseInt(params.get("lookCount").toString()));
        } else {
            if (isCreate) {
                articleBean.setLookCount(0);
            }
        }
        if (params.get("starCount") != null) {
            articleBean.setStarCount(Integer.parseInt(params.get("starCount").toString()));
        } else {
            if (isCreate) {
                articleBean.setStarCount(0);
            }
        }
        if (params.get("collectedCount") != null) {
            articleBean.setCollectedCount(Integer.parseInt(params.get("collectedCount").toString()));
        } else {
            if (isCreate) {
                articleBean.setCollectedCount(0);
            }
        }
        if (params.get("commentCount") != null) {
            articleBean.setCommentCount(Integer.parseInt(params.get("commentCount").toString()));
        } else {
            if (isCreate) {
                articleBean.setCommentCount(0);
            }
        }
        if (params.get("createDate") != null) {
            articleBean.setCreateDate(LocalDateTime.parse(params.get("createDate").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            if (isCreate) {
                articleBean.setCreateDate(LocalDateTime.now());
            }
        }
        if (params.get("articleStatus") != null) {
            articleBean.setArticleStatus(Integer.parseInt(params.get("articleStatus").toString()));
        } else {
            if (isCreate) {
                articleBean.setArticleStatus(0);
            }
        }
        if (params.get("siteId") != null) {
            articleBean.setSiteId(Integer.parseInt(params.get("siteId").toString()));
        }
        return articleBean;
    }

    private static boolean checkArticle(ArticleBean articleBean, boolean isCreate) {
        if (articleBean.getArticleAbstract() == null || articleBean.getArticleContent() == null
                || articleBean.getArticleTitle() == null || articleBean.getCoverUrl() == null
                || articleBean.getArticleAuthor() == null || articleBean.getSiteId() == null) {
            return false;
        }
        if (isCreate) {
            if (articleBean.getArticleRecommend() == null) {
                articleBean.setArticleRecommend(0);
            }
            if (articleBean.getArticleStatus() == null) {
                articleBean.setArticleStatus(0);
            }
            if (articleBean.getCollectedCount() == null) {
                articleBean.setCollectedCount(0);
            }
            if (articleBean.getLookCount() == null) {
                articleBean.setLookCount(0);
            }
            if (articleBean.getStarCount() == null) {
                articleBean.setStarCount(0);
            }
            if (articleBean.getCommentCount() == null) {
                articleBean.setCommentCount(0);
            }
            if (articleBean.getCreateDate() == null) {
                articleBean.setCreateDate(LocalDateTime.now());
            }
        }
        return true;
    }

    @PostMapping("/ArticleList")
    public Result getArticleList(@RequestBody Pageparam pageparam) {
        int pageNum = pageparam.getPagenum();
        int pageSize = pageparam.getPagesize();

        // 获取所有文章列表
        List<ArticleBean> articleList = articleService.getArticleList();
        if (articleList.isEmpty()) {
            return Result.fail(537, "文章获取失败");
        }

        // 对文章列表进行综合排序（根据点赞数、评论数、收藏数、浏览量和发布时间）
        articleList.sort((a1, a2) -> {
            double score1 = calculateScore(a1.getStarCount(), a1.getCommentCount(), a1.getCollectedCount(), a1.getLookCount(), a1.getCreateDate());
            double score2 = calculateScore(a2.getStarCount(), a2.getCommentCount(), a2.getCollectedCount(), a2.getLookCount(), a2.getCreateDate());
            return Double.compare(score2, score1);
        });

        // 分页处理结果
        int total = articleList.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        List<ArticleBean> pagedArticleList = articleList.subList(startIndex, endIndex);

        // 构建分页信息对象
        PageInfo<ArticleBean> pageInfo = new PageInfo<>();
        pageInfo.setList(pagedArticleList);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
        pageInfo.setPages((total + pageSize - 1) / pageSize);

        return Result.success(pageInfo);
    }


    @PostMapping("/admin/ArticleList")
    public Result AdminGetArticleList(@RequestBody Pageparam pageparam) {
        PageHelper.startPage(pageparam.getPagenum(), pageparam.getPagesize());
        List<ArticleBean> articleList = articleService.AdminGetArticleList();
        if (articleList.isEmpty()) {
            return Result.fail(537, "文章获取失败");
        }
        PageInfo<ArticleBean> pageInfo = new PageInfo<>(articleList);
        return Result.success(pageInfo);
    }


    @PostMapping("/getRelatedArticleListByKeyWord")
    public Result getRelatedArticleListByKeyWord(@RequestBody Map<String, Object> params) {
        String keyWord = params.get("keyWord").toString();
        int pageNum = Integer.parseInt(params.get("pageNum").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        List<Integer>tagList = (List<Integer>)params.get("tagList");
        List<ArticleBean> articleList;
        List<ArticleBean> resList = new ArrayList<>();
        int startIdx = (pageNum - 1) * pageSize;
        if(tagList.isEmpty()){
            articleList = articleService.getArticleList();
            for (int i = startIdx; i < min(startIdx + pageSize, articleList.size()); i++) {
                if (articleList.get(i).getArticleStatus() == 1) {
                    resList.add(articleList.get(i));
                }
            }
            manualPageVo manualPageVo = new manualPageVo(resList, articleList.size());
            return Result.success(manualPageVo);
        }
        //先根据关键字获取对应的文章列表：
        else{
            articleList = articleService.getArticleListByTitle(keyWord);
            List<ArticleBean> list = new ArrayList<>();
            //再根据tag筛选出包含所有对应标签的文章
            for (ArticleBean articleBean : articleList) {
                int flag = 1;
                for (Integer integer : tagList) {
                    if (!tagService.haveTag(articleBean.getArticleNid(), integer)) {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    list.add(articleBean);
                }
            }
            for (int i = startIdx; i < min(startIdx + pageSize, list.size()); i++) {
                if (list.get(i).getArticleStatus() == 1) {
                    resList.add(list.get(i));
                }
            }
            manualPageVo manualPageVo = new manualPageVo(resList, list.size());
            return Result.success(manualPageVo);
        }
    }


    //后台根据不同选项查找文章
    @PostMapping("/getArticleListBySearchWord")
    public Result getArticleListBySearchWord(@RequestBody PageparamAndSearchWord pageparamAndSearchWord) {
        PageHelper.startPage(pageparamAndSearchWord.getPagenum(), pageparamAndSearchWord.getPagesize());
        String searchWord = pageparamAndSearchWord.getSearchword();
        String value = pageparamAndSearchWord.getValue();
//        List<ArticleBean> articleBeans = articleService.AdminGetArticleList();
        List<ArticleBean> resList = new ArrayList<>();
        switch (value) {
            case "选项1":
//按文章标题搜索
                resList = articleService.getArticleListByTitle(searchWord);
                break;
            case "选项2":
//按作者搜索
                resList = articleService.getArticleListByAuthorName(searchWord);
                break;
            case "选项3":
                if (!searchWord.equals("0") && !searchWord.equals("1")) {
                    return Result.fail(578, "只能接受0或1");
                }
                resList = articleService.getArticleListByStatus(Integer.parseInt(searchWord));
                break;
            case "选项4":
//按推荐状态搜索
                if (!searchWord.equals("0") && !searchWord.equals("1")) {
                    return Result.fail(578, "只能接受0或1");
                }
                resList = articleService.getArticleListByRecommend(Integer.parseInt(searchWord));
                break;
        }
        PageInfo<ArticleBean> pageInfo = new PageInfo<>(resList);
        return Result.success(pageInfo);
    }

    // 获取热门文章
    @GetMapping("/recommend")
    public Result RecommendArticles() {
        // 先查询推荐文章，如果没有推荐文章，随机查询文章
        List<ArticleBean> recommendList = articleService.RecommendArticles();
        if (recommendList.isEmpty()) {
            //获取随机文章
            recommendList = articleService.RandomArticles();
        }
        return Result.success(recommendList);
    }

    //根据用户名获取该用户文章总数
    @GetMapping("/ArticleCount")
    public Result ArticleCount(@RequestParam String userName) {

        Integer Count = articleService.ArticleCount(userName);
        return Result.success(Count);
    }

    // 根据用户 id 获取文章列表
    @PostMapping(value = "/myarticles/{id}")
    public Result getArticleByNid(@PathVariable("id") Integer id, @RequestBody Pageparam pageparam) {

        PageHelper.startPage(pageparam.getPagenum(), pageparam.getPagesize());
        List<ArticleBean> articleList = articleService.getArticleListByAuthor(id);

        if (articleList.isEmpty()) {
            return Result.success(null);
        }
        PageInfo<ArticleBean> pageInfo = new PageInfo<>(articleList);

        return Result.success(pageInfo);
    }

    @PostMapping("/getArticleDetailsById")
    public Result getArticleDetailsById(@RequestBody QueryInfo queryInfo) {
        ArticleVo articleVo = new ArticleVo();

        int articleId = queryInfo.getArticleNid();
        int userId = queryInfo.getUserNid();
        String authorName = articleService.getArticleByNid(articleId).getArticleAuthor();
        int authorId = userService.getUserByName(authorName).getUserNid();

        //查询该用户是否关注了文章的作者
        articleVo.setFollowed(followService.isFanOf(authorId, userId));
        //查询该用户是否点赞了该文章
        articleVo.setLiked(userLikeArticleService.isLiked(userId, articleId));
        //查询该用户是否收藏了该文章
        articleVo.setCollected(collectService.isCollected(userId, articleId));

        //获取文章评论列表
        List<FatherCommentVo> fatherCommentVoList = new ArrayList<>();
        //先获取父评论列表
        List<CommentBean> rootCommentList = commentService.getRootCommentListByArticleId(articleId);
        for (int i = 0; i < rootCommentList.size(); i++) {
            //找到所有子评论：
            List<SonCommentVo> sonCommentVosList = new ArrayList<>();
            List<CommentBean> sonCommentList = commentService.getSonCommentListByRootNid(rootCommentList.get(i).getCommentNid());
            //将所有子评论封装成sonCommentVo,并组合成列表
            for (int j = 0; j < sonCommentList.size(); j++) {
                CommentBean son = sonCommentList.get(j);
                SonCommentVo sonCommentVo = new SonCommentVo(son.getCommentNid(), son.getCommentContent(), son.getCreateDate().toString(),
                        son.getUserId(), userService.getUserByNid(son.getUserId()).getUserName(),
                        userService.getUserByNid(commentService.getCommentByNid(son.getParentCommentId()).getUserId()).getUserName(),
                        userService.getUserByNid(son.getUserId()).getUserAvatarUrl(), son.getStarCount(), false);
                //判断该用户用有没有给子评论点赞，更新liked
                sonCommentVo.setLiked(userLikeCommentService.isLiked(userId, son.getCommentNid()));
                sonCommentVosList.add(sonCommentVo);
            }
            //将父评论封装成FatherCommentVo
            CommentBean father = rootCommentList.get(i);
            FatherCommentVo fatherCommentVo = new FatherCommentVo(father.getCommentNid(), father.getCommentContent(), father.getCreateDate().toString(),
                    father.getCommentCount(), father.getStarCount(), false, father.getUserId(),
                    userService.getUserByNid(father.getUserId()).getUserName(), userService.getUserByNid(father.getUserId()).getUserAvatarUrl(),
                    false, sonCommentVosList);
            //判断用户有没有给父评论点赞，更新liked
            fatherCommentVo.setLiked(userLikeCommentService.isLiked(userId, father.getCommentNid()));
            fatherCommentVoList.add(fatherCommentVo);
        }
        //放到articleVo中返回给前端
        articleVo.setFatherCommentList(fatherCommentVoList);
        return Result.success(articleVo);
    }

    // 根据文章id找到文章详情
    @GetMapping("/{id}")
    public Result getArticleByArticleNid(@PathVariable("id") Integer id) {
        ArticleBean articleBean = articleService.getArticleByNid(id);
        if (articleBean == null || articleBean.getArticleStatus() != 1) {
            return Result.fail(404, "该文章已下架或不存在");
        }
        articleBean.setLookCount(articleBean.getLookCount() + 1);
        articleService.updateArticle(articleBean);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> authorMap = new HashMap<>();
        resultMap.put("article", articleBean);
        UserBean author = userService.getUserByName(articleBean.getArticleAuthor());
        authorMap.put("userNid", author.getUserNid());
        authorMap.put("userAvatarUrl", author.getUserAvatarUrl());
        resultMap.put("author", authorMap);
        return Result.success(resultMap);
    }

    @PostMapping("/getRelatedArticle")
    public Result getRelatedArticle(@RequestBody Map<String, Object> data) {
        Integer authorId = Integer.parseInt(data.get("authorId").toString());
        Integer articleId = Integer.parseInt(data.get("articleId").toString());
        List<ArticleBean> articleBeanList = articleService.getArticleListByAuthor(authorId);
        List<ArticleBean> articleBeans=new ArrayList<>();
        for (ArticleBean article:articleBeanList) {
            if(article.getArticleStatus()==1){
                articleBeans.add(article);
            }
        }
        List<ArticleBean> relatedArticleList = new ArrayList<>();
        int idx = -1;
        for (int i = 0; i < min(3, articleBeans.size() - 1); i++) {
            idx++;
            if (Objects.equals(articleBeans.get(idx).getArticleNid(), articleId)) {
                i--;
                continue;
            }
            relatedArticleList.add(articleBeans.get(idx));
        }
        return Result.success(relatedArticleList);
    }


    @PostMapping(value = "/publish")
    public Result addArticle(@RequestBody Map<String, Object> params) {
        Map<String, Object> article = (Map<String, Object>) params.get("article");
        List<Integer> tags = (List<Integer>) params.get("tagList");
        ArticleBean articleBean = new ArticleBean();
        articleBean.setArticleTitle(article.get("articleTitle").toString());
        articleBean.setCoverUrl(article.get("coverUrl").toString());
        articleBean.setArticleAbstract(article.get("articleAbstract").toString());
        articleBean.setArticleContent(article.get("articleContent").toString());
        articleBean.setSiteId(Integer.parseInt(article.get("siteId").toString()));
        UserBean userBean = UserThreadLocal.get();
        articleBean.setArticleAuthor(userBean.getUserName());
        boolean status = checkArticle(articleBean, true);
        if (!status) {
            return Result.fail(522, "缺失重要信息");
        }
        int rows = articleService.addArticle(articleBean);
        if (rows <= 0) {
            return Result.fail(523, "文章上传失败");
        }
        for (int i = 0; i < tags.size(); i++) {
            int addArticleTag = tagService.AddArticleTags(articleBean.getArticleNid(), tags.get(i));
        }
        return Result.success("文章上传成功,待管理员审核");
    }

    @PostMapping(value = "/updateArticle")
    public Result updateArticle(@RequestBody ArticleBean articleBean) {
        ArticleBean article = articleService.getArticleByNid(articleBean.getArticleNid());
        if (article == null) {
            return Result.fail(666, "文章不存在");
        }
        articleService.updateArticle(articleBean);
        return Result.success("更新文章成功");

    }


    @PostMapping(value = "/deleteArticleByNid/{id}")
    public Result deleteArticleByNid(@PathVariable("id") Integer id) {

        ArticleBean article = articleService.getArticleByNid(id);
        if (article != null) {
            int status = articleService.deleteArticleByNid(id);
            if (status == 0) {
                return Result.fail(510, "删除文章失败！");
            }
            return Result.success("删除文章成功");
        } else {
            return Result.fail(509, "文章不存在！");
        }

    }

    @PostMapping(value = "/deleteArticleByNids")
    public Result deleteArticleByNids(@RequestBody List<ArticleBean> articleBeans) {

        for (ArticleBean articleBean : articleBeans) {

            ArticleBean article = articleService.getArticleByNid(articleBean.getArticleNid());

            if (article == null) {
                return Result.fail(509, "文章不存在！");
            }

            int status = articleService.deleteArticleByNid(articleBean.getArticleNid());
            if (status == 0) {
                return Result.fail(555, "删除文章失败");
            }

        }
        return Result.success("删除文章成功");

    }

    @GetMapping("/getArticleNum")
    public Result getArticleNum(){
        return Result.success(articleService.getArticleNum());
    }

}
