import request from '@/utils/request'

export function getActivityInstances(data) {
  return request({
    url: '/engine-rest/history/activity-instance',
    method: 'get',
    params: data
  })
}
export function getIncidents(data) {
  return request({
    url: '/engine-rest/history/incident',
    method: 'get',
    params: data
  })
}
