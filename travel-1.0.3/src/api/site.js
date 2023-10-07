import request from '@/request'

export function AllSite(){
    return request({
        url: '/Site/SiteList',
        method: 'get'
      })
}

//获取打卡地中的热门打卡地
export function hotCity(){
  return request({
    url: '/Site/HotCity',
    method:'get'
  })
}

//获取近一个月内的打卡高分地
//逻辑：在近半个月内被发布相关文章数量最多且评分最高的前两个景点
export function hotSite(){
  return request({
    url:'/Site/HotSite',
    method:'get'
  })
}

export function localhotSite(Addr){
  return request({
    url:`/Site/LocalHotSite?localCity=${Addr}`,
    method:'get'
  })
}

// export function SiteInfo(id){
//   return request({
//     url:`/Site/SiteInfo?SiteId=${id}`,
//     method:'get'
//   })
// }

export function SiteById(id){
  return request({
    url: `/Site/getSiteByNid/${id}`,
    method: 'get'
  })
}

export function selectSitesByIds(idList){
  const data={
    idList:idList
  }
  return request({
    url: `/Site/SitesByIds`,
    method: 'post',
    data
  })
}

export function getSiteList(queryInfo)
{
  return request(
    {
      url:`/Site/SiteListPart`,
      method:'post',
      data:queryInfo,
    }
  )
}

export function addSite(registerForm) {
  return request({
    url: '/Site/addSite',
    method: 'post',
    data: registerForm
  })
}

export function deleteSite(SiteId) {
  return request({
    url: `/Site/deleteSiteByNid/${SiteId}`,
    method: 'post',
    
  })
}
export function deleteSites(multipleSelection) {
  return request({
    url: `/Site/deleteSiteByNids`,
    method: 'post',
    data: multipleSelection,
  })
}

export function updateSite(editForm)
{
  return request({
    url:'/Site/updateSite',
    method:'post',
    data:editForm,
  })
}

export function getSiteListBySearchWord(temp)
{
  return request(
    {
      url:`/Site/getSiteListBySearchWord`,
      method:'post',
      data:temp,
    })
}

export function getRelatedSiteListByKeyWord(msg) {
  return request({
    url: '/Site/getRelatedSiteListByKeyWord',
    method: 'post',
    data : msg
  })
}

export function getSiteNum(){
  return request({
    url:'/Site/getSiteNum',
    method: 'get'
  })
}

export function updateSiteUrl(url)
{
  return request({
    url:'/Site/updateSiteUrl',
    method:'post',
    data:url,
  })
}
export function SiteInfo(ids){
  return request({
    url:`/Site/SiteInfo?siteId=${ids.siteId}`,
    method:'post',
    data:ids
  })
}

export function AddUserSite(ids){
  return request({
    url:`/Site/AddUserSite`,
    method:'post',
    data:ids
  })
}

export function DeleteUserSite(ids){
  return request({
    url:`/Site/DeleteUserSite`,
    method:'post',
    data:ids
  })
}

export function AddScore(temp){
  return request({
    url:`/Site/AddScore`,
    method:'post',
    data:temp
  })
}
