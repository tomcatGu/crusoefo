import request from '@/utils/request'

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
export function login2(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
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
