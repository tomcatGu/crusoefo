import request from '@/utils/request'

export function getRepository() {
    return request({
        url: 'http://localhost:6601/rest/deployment',
        method: 'get'
        //auth: { username: 'kermit', password: 'superSecret' }
    })
}