import request from '@/utils/request'

export function getTasks() {
    return request({
        url: 'http://localhost:6601/rest/task',
        method: 'get'
    })
}