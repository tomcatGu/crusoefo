import request from '@/utils/request'

export function getProcesses(data) {
  return request({
    url: '/engine-rest/process-definition',
    method: 'get',
    params: data
  })
}
export function getProcessesCount(data) {
  return request({
    url: '/engine-rest/process-definition/count',
    method: 'get',
    params: data
  })
}
export function getProcessXML(data) {
  console.log(data)
  return request({
    url: '/engine-rest/process-definition/' + data + '/xml',
    method: 'get'
  })
}
export function getStartFormKey(data) {
  console.log(data)
  return request({
    url: '/engine-rest/process-definition/' + data + '/startForm',
    method: 'get'
  })
}

export function getDeployedStartForm(data) {
  console.log(data)
  return request({
    url: '/engine-rest/process-definition/' + data + '/deployed-start-form',
    method: 'get'
  })
}
export function getDeployedStartFormByKey(data) {
  console.log(data)
  return request({
    url: '/engine-rest/process-definition/key/' + data + '/deployed-start-form',
    method: 'get'
  })
}
export function startProcess(id, data) {
  console.log(id)
  return request({
    headers: {
      'Content-Type': 'application/json'
    },
    url: '/engine-rest/process-definition/' + id + '/start',
    method: 'post',
    data
  })
}
export function startProcessByKey(key, data) {
  return request({
    headers: {
      'Content-Type': 'application/json'
    },
    url: '/engine-rest/process-definition/key/' + key + '/start',
    method: 'post',
    data
  })
}
export function submitStartForm(id, data) {
  return request({
    headers: {
      'Content-Type': 'application/json'
    },
    url: '/engine-rest/process-definition/' + id + '/submit-form',
    method: 'post',
    data
  })
}
export function submitStartFormByKey(key, data) {
  return request({
    headers: {
      'Content-Type': 'application/json'
    },
    url: '/engine-rest/process-definition/key/' + key + '/submit-form',
    method: 'post',
    data
  })
}
export function getProcessInstances(data) {
  return request({
    url: '/engine-rest/process-instance',
    method: 'get',
    params: data
  })
}
export function getProcessInstanceCount(data) {
  return request({
    url: '/engine-rest/process-instance/count',
    method: 'get',
    params: data
  })
}
export function getProcessInstance(data) {
  return request({
    url: '/engine-rest/process-instance/' + data,
    method: 'get'
  })
}
