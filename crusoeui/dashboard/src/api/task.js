import request from '@/utils/request'

export function getTasks(data) {
  return request({
    url: '/rest/task',
    method: 'get',
    params: data
  })
}
export function getTaskVariables(data) {
  return request({
    url: '/rest/task/' + data + '/variables',
    method: 'get'
  })
}
export function getDeployedForm(data) {
  return request({
    url: '/rest/task/' + data +'/deployed-form',
    method: 'get'
  })
}
export function getTasksCount(data) {
  return request({
    url: '/rest/task/count',
    method: 'get',
    params: data
  })
}
export function submitTaskForm(id, data) {
  return request({
    headers: {
      'Content-Type': 'application/json'
    },
    url: '/rest/task/' + id + '/submit-form',
    method: 'post',
    data
  })
}
export function completeTask(data) {
  return request({
    url: '/rest/task/' + data + '/complete',
    method: 'post'
  })
}
