import request from '@/utils/request'

export function create(data) {
  return request({
    url: '/identity/role/create',
    method: 'post',
    data
  })
}
