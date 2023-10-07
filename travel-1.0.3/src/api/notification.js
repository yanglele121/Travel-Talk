import request from '@/request'

export function getMyNotification(id) {
    return request({
      url: `/Notification/MyNotification/${id}`,
      method: 'get'
    })
  }

export function sendAuditOpinion(msg){
    return request({
        url: "/Notification/AuditOpinion",
        method: 'post',
        data:msg,
    })
  }
export function getMyUnreadCount(id){
  return request({
    url: `/Notification/MyUnreadCount/${id}`,
    method: 'get'
  })
}
export function allRead(id){
  return request({
    url: `/Notification/AllRead/${id}`,
    method: 'post'
  })
}

