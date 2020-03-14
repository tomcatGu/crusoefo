import request from '@/utils/request'

export function getOrder() {
  return request({
    url: '/order/message',
    method: 'get'
  })
}
