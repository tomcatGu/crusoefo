import request from '@/utils/request'

export function createRole(data) {
  return request({
    url: '/identity/role/create',
    method: 'post',
    data
  })
}
