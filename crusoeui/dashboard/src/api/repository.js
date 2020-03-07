import request from '@/utils/request'

export function getRepository() {
  return request({
    url: 'http://localhost:6601/rest/deployment',
    method: 'get'
  })
}

export function createDeployment(formData) {
  return request({
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    url: 'http://localhost:6601/rest/deployment/create',
    method: 'post',
    data: formData

  })
}
