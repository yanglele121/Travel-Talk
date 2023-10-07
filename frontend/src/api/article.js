import request from '@/request'

export function getArticles(query, page) {
  return request({
    url: '/articles',
    method: 'post',
    data: {
      page: page.pageNumber,
      pageSize: page.pageSize,
      name: page.name,
      sort: page.sort,
      year: query.year,
      month: query.month,
      tagId: query.tagId,
      categoryId: query.categoryId
    }
  })
}

export function RecommendArticle() {
  return request({
    url: '/article/recommend',
    method: 'get'
  })
}

export function getHotArticlesByCatrgory(categoryId) {
  return request({
    url: `/articles/categoryhot/${categoryId}`,
    method: 'post'
  })
}

export function getHotArticesByTag(tagId) {
  return request({
    url: `/articles/taghot/${tagId}`,
    method: 'post'
  })
}

export function getNewArtices() {
  return request({
    url: '/articles/new',
    method: 'post'
  })
}

export function getcarousel() {
  return request({
    url: '/articles/carousel',
    method: 'post'
  })
}

export function getSecondCarousel() {
  return request({
    url: '/articles/secondCarousel',
    method: 'post'
  })
}

export function viewArticle(id) {
  return request({
    url: `/articles/view/${id}`,
    method: 'post'
  })
}


export function getArticlesByTag(id) {
  return request({
    url: `/article/tag/${id}`,
    method: 'post'
  })
}

export function Allarticles(pageparam){
  return request({
    url:'/article/ArticleList',
    method: 'post',
    data:pageparam
  })
}

export function publishArticle(article,token,tagList) {
  const data = {
    article,
    tagList
  }
  return request({
    headers: {'Authorization': token},
    url: '/article/publish',
    method: 'post',
    data
  })
}

// export function updateArticle(article,token) {
//   return request({
//     headers: {'Authorization': token},
//     url: '/articles/updateArticle',
//     method: 'post',
//     data: article
//   })
// }

export function myArticleCount(userName){
  return request({
    url:`/article/ArticleCount?userName=${userName}`,
    method: 'get'
  })
}

export function getArticleById(id) {
  return request({
    url: `/article/${id}`,
    method: 'get'
  })
}

export function getRelatedArticle(msg) {
  return request({
    url: '/article/getRelatedArticle',
    method: 'post',
    data : msg
  })
}

export function getRelatedArticleListByKeyWord(msg) {
  return request({
    url: '/article/getRelatedArticleListByKeyWord',
    method: 'post',
    data : msg
  })
}



export function getArticleDetailsById(queryInfo) {
  return request({
    url: `/article/getArticleDetailsById`,
    method: 'post',
    data: queryInfo
  })
}

export function selectArticle(article) {
  return request({
    url: '/articles/select',
    method: 'post',
    data: article
  })
}
 

export function getArticleList(pageparam)
{
  return request(
    {
      url:'/article/admin/ArticleList',
      method:'post',
      data:pageparam,
    }
  )
}

export function deleteArticle(id) {
  return request({
    url: `/article/deleteArticleByNid/${id}`,
    method: 'post'
  })
}

export function getArticleListBySearchWord(temp)
{
  return request(
    {
      url:`/article/getArticleListBySearchWord`,
      method:'post',
      data:temp,
    }
  )
}



export function deleteArticles(multipleSelection) {
  return request({
    url: `/article/deleteArticleByNids`,
    method: 'post',
    data: multipleSelection,
  })
}

export function updateArticle(temp)
{
  return request({
    url:`/article/updateArticle`,
    method:'post',
    data:temp
  })
}

export function getArticleNum(){
  return request({
    url:'/article/getArticleNum',
    method: 'get'
  })
}
