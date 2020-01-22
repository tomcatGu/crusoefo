import request from '@/utils/request'

export function login(data) {
  return request({
    url: 'http://localhost:7000/oauth/token?grant_type=password&client_id=client_1&client_secret=123456&scope=web&state=xyz'+"&username="+data.username+"&password="+data.password,
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

export function getOrder(){
   return request({
     url: 'http://localhost:6601/order/message',
     method: 'get'

   })

}
