import request from '@/utils/request'

export function getRepositories(data) {
  return request({
    url: '/engine-rest/deployment',
    method: 'get',
    params: data
  })
}
export function getRepository(id) {
  return request({
    url: '/engine-rest/deployment/' + id,
    method: 'get'
  })
}
export function getResources(id) {
  return request({
    url: '/engine-rest/deployment/' + id + '/resources',
    method: 'get'
  })
}
export function getResourceData(id, resourceId) {
  return request({
    responseEncoding: 'utf8',
    // responseType: 'blob',
    url: '/engine-rest/deployment/' + id + '/resources/' + resourceId + '/data',
    method: 'get'
  })
}
export function getRepositoryCount(data) {
  return request({
    url: '/engine-rest/deployment/count',
    method: 'get',
    params: data
  })
}
export function createDeployment(formData) {
  return request({
    headers: {
      'Content-Type': 'multipart/form-data;charset=utf-8'
    },
    url: '/engine-rest/deployment/create',
    method: 'post',
    data: formData

  })
}
export function redeploy(id, formData) {
  return request({
    headers: {
      'Content-Type': 'multipart/form-data;charset=utf-8'
    },
    url: '/engine-rest/deployment/' + id + '/redeploy',
    method: 'post',
    data: formData

  })
}

