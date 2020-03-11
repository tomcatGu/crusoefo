<template>
  <div class="app-container">
    <form-create v-model="$data.$f" :rule="rule" :option="option" @on-submit="onSubmit" />
  </div>
</template>

<script>
import FormCreate from '@form-create/element-ui'
import { getDeployedForm } from '@/api/task'

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
      resourceId: null,
      name: null,
      source: null
    }
  },

  mounted() {
  },
  created() {
    console.log(this.$route.params)
    this.$data.id = this.$route.params.id
    this.$data.formKey = this.$route.params.formKey
    // this.$data.resourceId = this.$route.params.resourceId
    getDeployedForm(this.$data.id).then(response => {
      this.$data.rule = response
      this.$data.value = this.$data.rule
    })
  },
  methods: {
    onSubmit(formData) {
      // TODO 提交表单
      console.log(JSON.stringify(formData))
    },
    onChange(data) {
      console.log(data)
      this.$data.rule = JSON.parse(data)
      // this.$data.rule = JSON.parse(this.$data.value)
      // this.formData.create(JSON.parse(this.rule))
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
