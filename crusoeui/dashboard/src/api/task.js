import request from '@/utils/request'

export function getTasks(data) {
  return request({
    url: 'http://localhost:6601/rest/task',
    method: 'get',
    params: data
  })
}
export function getTasksCount(data) {
  return request({
    url: 'http://localhost:6601/rest/task/count',
    method: 'get',
    params: data
  })
}
