<template>
  <div class="app-container">
    <form-create v-model="$data.$f" :rule="rule" :option="option" @on-submit="onSubmit" />
  </div>
</template>

<script>
import FormCreate from '@form-create/element-ui'
import { getDeployedStartForm } from '@/api/processes'

export default {
  components: {
    formCreate: FormCreate.$form()
  },
  data() {
    return {
      // 表单实例对象
      $f: {},
      model: {},
      // 表单生成规则
      rule: [],
      option: {
        resetBtn: false,
        submitBtn: false
      },
      id: null,
      formKey: null
    }
  },

  mounted() {
  },
  created() {
    console.log(this.$route.params)
    this.id = this.$route.params.id
    this.formKey = this.$route.params.formKey
    // this.$data.resourceId = this.$route.params.resourceId
    getDeployedStartForm(this.$data.id).then(response => {
      this.rule = response
    })
  },
  methods: {
    onSubmit(formData) {
      // TODO 提交表单
      console.log(JSON.stringify(formData))
    }
  }
}
</script>
<style lang="scss">
.app-container {
  position: absolute;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
}
.editor-container {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow-y: scroll;
  white-space: nowrap;
}
</style>
