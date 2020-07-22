import request from '@/utils/request'

export function getTasks(data) {
  return request({
    url: '/engine-rest/task',
    method: 'get',
    params: data
  })
}
export function getTaskVariables(data) {
  return request({
    url: '/engine-rest/task/' + data + '/variables',
    method: 'get'
  })
}
export function getDeployedForm(data) {
  return request({
    url: '/engine-rest/task/' + data +'/deployed-form',
    method: 'get'
  })
}
export function getTasksCount(data) {
  return request({
    url: '/engine-rest/task/count',
    method: 'get',
    params: data
  })
}
export function submitTaskForm(id, data) {
  return request({
    headers: {
      'Content-Type': 'application/json'
    },
    url: '/engine-rest/task/' + id + '/submit-form',
    method: 'post',
    data
  })
}
export function completeTask(data) {
  return request({
    url: '/engine-rest/task/' + data + '/complete',
    method: 'post'
  })
}
