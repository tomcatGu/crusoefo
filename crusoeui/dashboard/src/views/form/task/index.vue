<template>
  <div class="app-container">
    <form-create v-model="$data.$f" :rule="rule" :option="option" @on-submit="onSubmit" />
  </div>
</template>

<script>
import FormCreate from '@form-create/element-ui'
import { getDeployedForm, submitTaskForm } from '@/api/task'
import { Message } from 'element-ui'
import { formatTime } from '@/utils'

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
        resetBtn: false
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
    this.id = this.$route.params.id
    this.formKey = this.$route.params.formKey
    // this.$data.resourceId = this.$route.params.resourceId
    getDeployedForm(this.$data.id).then(response => {
      this.rule = response
    })
  },
  methods: {
    onSubmit(formData) {
      // TODO 提交表单
      // console.log(this.rule)
      // console.log(JSON.stringify(formData))
      var variables = {}
      this.rule.forEach(r => {
        // console.log(r.field)
        // console.log(r.type)
        var v = {}
        switch (r.type) {
          case 'input': {
            v.type = 'String'
            v.value = formData[r.field]
            break
          }
          case 'InputNumber': {
            v.value = formData[r.field]
            v.type = 'long'
            break
          }
          case 'datePicker': {
            v.type = 'date'
            v.value = formatTime(new Date(formData[r.field]), '{y}-{m}-{d}T{h}:{i}:{s}.000+0800')
            break }
          case 'switch': {
            v.type = 'boolean'
            v.value = formData[r.field]
            break
          }
          default: {
            v.type = 'String'
            v.value = formData[r.field]
            break
          }
        }

        variables[r.field] = v
      })
      submitTaskForm(this.id, { 'variables': variables }).then(response => {
        Message({
          message: '提交表单成功。',
          type: 'success',
          duration: 2 * 1000
        })
      })
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
