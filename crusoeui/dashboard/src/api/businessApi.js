import request from '@/utils/request'

export function createRole(data) {
  return request({
    url: '/identity/role/create',
    method: 'post',
    data
  })
}

export function createUser(data) {
  return request({
    url: '/identity/user/create',
    method: 'post',
    data
  })
}

export function requestData(url, method, data) {
  console.log(url)
  return request({
    url: url,
    method: method,
    data
  })
}
