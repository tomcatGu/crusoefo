import request from '@/utils/request'

export function getOrder() {
  return request({
    url: 'http://localhost:6601/order/message',
    method: 'get'
  })
}
