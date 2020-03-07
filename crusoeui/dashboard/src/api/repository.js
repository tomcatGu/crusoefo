import request from '@/utils/request'

export function getRepository(data) {
  return request({
    url: 'http://localhost:6601/rest/deployment',
    method: 'get',
    params: data
  })
}
export function getRepositoryCount(data) {
  return request({
    url: 'http://localhost:6601/rest/deployment/count',
    method: 'get',
    params: data
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
