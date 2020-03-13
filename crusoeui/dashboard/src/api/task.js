import request from '@/utils/request'

export function getTasks(data) {
  return request({
    url: 'http://localhost:6601/rest/task',
    method: 'get',
    params: data
  })
}
export function getDeployedForm(data) {
  return request({
    url: 'http://localhost:6601/rest/task/' + data +'/deployed-form',
    method: 'get'
  })
}
export function getTasksCount(data) {
  return request({
    url: 'http://localhost:6601/rest/task/count',
    method: 'get',
    params: data
  })
}
export function submitTaskForm(id, data) {
  return request({
    headers: {
      'Content-Type': 'application/json'
    },
    url: 'http://localhost:6601/rest/task/' + id + '/submit-form',
    method: 'post',
    data
  })
}
