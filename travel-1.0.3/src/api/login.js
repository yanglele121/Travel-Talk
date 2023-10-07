import request from '@/request'

export function login(userName, userPassword) {
  const data = {
    userName,
    userPassword
  }
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function logout(token) {
  return request({
    headers: {'Authorization': token},
    url: '/logout',
    method: 'get'
  })
}

export function getUserInfo(token) {
  return request({
    headers: {'Authorization': token},
    url: '/users/CurrentUser',
    method: 'get'
  })
}

export function register(user) {
  return request({
    url: '/register',
    method: 'post',
    data: user
  })
}

export function AdminLogin(username,password){
  const data = {
    username,
    password
  }
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}
export function AdminLogOut(token){
  return request({
    headers: {'Authorization': token},
    url: '/admin/logout',
    method: 'get'
  })
}

export function getCurAdmin(token){
  return request({
    headers: {'Authorization': token},
    url: '/admin/curAdmin',
    method: 'get'
  })
}