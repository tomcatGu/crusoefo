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
