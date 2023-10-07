import request from '@/request'

export function myFollow(userId) {
    return request({
      url: `/follow/myfollows/${userId}`,
      method: 'get'
    })
  }

  export function myFan(userId) {
    return request({
      url: `/follow/myfans/${userId}`,
      method: 'get'
    })
  }

export function addFollow(follow,token) {
  return request({
    headers: {'Authorization': token},
    url: '/follow/add',
    method: 'post',
    data: follow
  })
}

  export function deleteFollow(follow,token) {
    return request({
      headers: {'Authorization': token},
      url: '/follow/delete',
      method: 'post',
      data: follow
    })
  }

  export function followAndFanCount(userId) {
    return request({
      url: `/follow/count/${userId}`,
      method: 'get'
    })
  }