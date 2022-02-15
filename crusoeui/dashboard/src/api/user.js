import request from '@/utils/request'
import qs from 'qs'

export function login(data) {
  if (process.env.NODE_ENV === 'development') {
    return request({
      url: '/user/login',
      method: 'post',
      data
    })
  } else {
    return request({
      url: '/oauth/token?grant_type=password&client_id=client_1&client_secret=123456&scope=web&state=xyz' + '&username=' + data.username + '&password=' + data.password,
      method: 'get',
      data
    })
  }
}
export function login2(formdata) {
  const d = qs.stringify(formdata)
  // console.log(d)
  return request({
    headers: {
      'content-type': 'application/x-www-form-urlencoded;' // 设置完以后 传入的params对象就会时候用formdata传参的方式
    },
    url: '/login',
    method: 'post',
    data: d
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
