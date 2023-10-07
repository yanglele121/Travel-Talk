import request from '@/request'


export function getCommentsByArticle(id) {
  return request({
    url: `/comments/article/${id}`,
    method: 'get'
  })
}

export function publishComment(comment,token) {
  return request({
    headers: {'Authorization': token},
    url: '/comments/create/change',
    method: 'post',
    data: comment
  })
}

export function getCommentsCount(articleId) {
  return request({
    url: `/comments/count/${articleId}`,
    method: 'post'
  })
}

export function getCommnetList(queryInfo)
{
  return request({
    url:`/comments/getCommentList`,
    method:'post',
    data:queryInfo,
  })
}

export function deleteComment(id) {
  return request({
    url: `/comments/deleteCommentByNid/${id}`,
    method: 'post',
  })
}

export function deleteComments(multipleSelection) {
  return request({
    url: `/comments/deleteCommentByNids`,
    method: 'post',
    data: multipleSelection,
  })
}
export function deleteCommentByParentNid(commentId) {
  return request({
    url: `/comments/deleteCommentByParentNid/${commentId}`,
    method: 'post',
  })
}
export function getCommentListBySearchWord(temp)
{
  return request(
    {
      url:`/comments/getCommentListBySearchWord`,
      method:'post',
      data:temp,
    }
  )
}


export function addComment(comment,token) {
  const data = {
    comment
  }
  return request({
    //访问受保护的资源：当用户需要访问受保护的资源时，客户端需要提供身份令牌以证明该用户已经通过身份验证并获得了授权。
    //登陆后才能使用的功能都要加这个
    headers: {'Authorization': token},
    url: '/comments/addComment',
    method: 'post',
    data
  })
}

export function getCommentNum(){
  return request({
    url:'/comments/getCommentNum',
    method: 'get'
  })
}