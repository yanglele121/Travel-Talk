import request from '@/request'


//找到所有文章tag返回前端提供选择
export function ArticleTags(){
  return request({
    url:'/Tags/ArticleTags',
    method: 'get',
  })
}


//根据文章id找到该文章的所有tag
export function getArticleTag(id) {
  return request({
    url: `/Tags/${id}`,
    method: 'get',
  })
}


export function newtag(tag){
  return request({
    url:'/Tags/new-tag',
    method:'post',
    data:tag
  })
}

export function updateTag(tag){
  return request({
    url:'/Tags/renew-tag',
    method:'post',
    data:tag
  })
}

export function deleteTag(tag){
  return request({
    url:'/Tags/delete-tag',
    method:'post',
    data:tag
  })
}

export function getTagDetail(id) {
  return request({
    url: `/Tags/detail/${id}`,
    method: 'get',
  })
}
