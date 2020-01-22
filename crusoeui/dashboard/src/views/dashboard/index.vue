<template>
  <div class="dashboard-container">
    <div class="dashboard-text">name: {{ name }}</div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getOrder } from '@/api/user'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  watch: {
    $route: {
      handler: function(route) {
        //this.redirect = route.query && route.query.redirect
        new Promise((resolve, reject) => {

          getOrder().then(response=>{

            console.log(response)
          })
        })
      },
      immediate: true
    }
  },
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
