import request from '@/utils/request'

export function getProcesses() {
    return request({
        url: 'http://localhost:6601/rest/process-definition',
        method: 'get'
    })
}