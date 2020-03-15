import request from '@/utils/request'

export function getActivityInstances(data) {
  return request({
    url: '/rest/history/activity-instance',
    method: 'get',
    params: data
  })
}
