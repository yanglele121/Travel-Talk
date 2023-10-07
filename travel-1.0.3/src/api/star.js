import request from '@/request'

export function myGood(userId) {
    return request({
      url: `/good/list/${userId}`,
      method: 'post'
    })
  }

export function addGood(good,token) {
    return request({
      headers: {'Authorization': token},
      url: '/good/add',
      method: 'post',
      data: good
    })
  }

  export function deleteGood(good,token) {
    return request({
      headers: {'Authorization': token},
      url: '/good/delete',
      method: 'post',
      data: good
    })
  }

  export function goodCount(articleId) {
    return request({
      url: `/good/count/${articleId}`,
      method: 'post'
    })
  }

  export function mystarCount(id) {
    return request({
      url: `/star/allCounts/${id}`,
      method: 'get'
    })
  }
