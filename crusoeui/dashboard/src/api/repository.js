import request from '@/utils/request'

export function getRepositories(data) {
  return request({
    url: '/rest/deployment',
    method: 'get',
    params: data
  })
}
export function getRepository(id) {
  return request({
    url: '/rest/deployment/' + id,
    method: 'get'
  })
}
export function getResources(id) {
  return request({
    url: '/rest/deployment/' + id + '/resources',
    method: 'get'
  })
}
export function getResourceData(id, resourceId) {
  return request({
    url: '/rest/deployment/' + id + '/resources/' + resourceId + '/data',
    method: 'get'
  })
}
export function getRepositoryCount(data) {
  return request({
    url: '/rest/deployment/count',
    method: 'get',
    params: data
  })
}
export function createDeployment(formData) {
  return request({
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    url: '/rest/deployment/create',
    method: 'post',
    data: formData

  })
}

