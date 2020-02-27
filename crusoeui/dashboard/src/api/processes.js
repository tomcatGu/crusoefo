import request from '@/utils/request'

export function getProcesses() {
  return request({
    url: 'http://localhost:6601/rest/process-definition',
    method: 'get'
  })
}
export function getProcessXML(data) {
  console.log(data)
  return request({
    url: 'http://localhost:6601/rest/process-definition/' + data + '/xml',
    method: 'get'
  })
}

export function startProcess(data) {
  console.log(data)
  return request({
    headers: {
      'Content-Type': 'application/json'
    },
    url: 'http://localhost:6601/rest/process-definition/' + data + '/start',
    method: 'post',
    data: '{}'
  })
}
