import { getOrder } from '@/api/order'

const state = {
  message: ''
}

const mutations = {

}

const actions = {
  // remove token
  getOrder({ commit }) {
    return new Promise(resolve => {
      getOrder().then(response => {
        state.message = response.message
        console.log(response)
      })

      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

