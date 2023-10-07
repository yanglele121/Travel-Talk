import request from '@/request'

export function allUser(page) {
  return request({
    url: '/users/alluser',
    method: 'post',
    data: page
  })
}

export function userInfo(id,isme) {
  return request({
    url: `/users/getUserByNid/${id}?isme=${isme}`,
    method: 'get'
  })
}

export function myArticle(authorId,pageparam) {
  return request({
    url: `/article/myarticles/${authorId}`,
    method: 'post',
    data:pageparam
  })
}

//获取用户打卡地
export function myfootprint(id){
  return request({
    url: `/users/footprint/${id}`,
    method: 'get'
  })
}



export function footprint(id,cityName){
  return request({
    url: `/users/footprint/${id}`,
    method: 'post',
    data:cityName
  })
}


export function updateUser(user) {
  return request({
    url: '/users/updateUser',
    method: 'post',
    data: user
  })
}

export function selectUser(user) {
  return request({
    url: '/users/select',
    method: 'post',
    data: user
  })
}

export function selectUserByName(userName){
  return request({
    url:`/users/UserByName?userName=${userName}`,
    method: 'get',
  })
}
export function selectUsersByNames(nameList){
  const data={
    nameList:nameList
  }
  return request({
    url: `/users/UsersByNames`,
    method: 'post',
    data
  })
}

// 陈浩
export function getUserList(queryInfo)
{
  return request(
    {
      url:`/users/getUserList`,
      method:'post',
      data:queryInfo,
    }
  )
}



export function addUser(registerForm) {
  return request({
    url: '/users/addUser',
    method: 'post',
    data: registerForm
  })
}

export function deleteUser(userId) {
  return request({
    url: `/users/deleteUserByNid/${userId}`,
    method: 'post',
    
  })
}
export function deleteUsers(multipleSelection) {
  return request({
    url: `/users/deleteUserByNids`,
    method: 'post',
    data: multipleSelection,
  })
}

export function getUserListBySearchWord(temp)
{
  return request(
    {
      url:`/users/getUserListBySearchWord`,
      method:'post',
      data:temp,
    }
  )
}

export function getRelatedUserListByKeyWord(msg) {
  return request({
    url: '/users/getRelatedUserListByKeyWord',
    method: 'post',
    data : msg
  })
}

export function getUserAddr(){
  return request({
    url:'/users/getUserAddr',
    method:'get'
  })
}

export function getUserToAddr(){
  return request({
    url:'/users/getUserToAddr',
    method:'get'
  })
}

export function getUserSexRate(){
  return request({
    url:'/users/getUserSexRate',
    method:"get"
  })
}

export function RecentLogin(){
  return request({
    url:'/users/RecentLogin',
    method:"get"
  })
}

export function getUserTravelDiary(id) {
  return request({
    url: `/users/getUserTravelDiary/${id}`,
    method: 'get'
  })
}

export function getPicAlbumListByUserId(userId,pageparam) {
  return request({
    url: `/picAlbum/getPicAlbumListByUserId/${userId}`,
    method: 'post',
    data:pageparam
  })
}
