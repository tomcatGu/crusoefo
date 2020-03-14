<template>
  <div class="app-container">
    <form-create v-model="$data.$f" :rule="rule" :option="option" @on-submit="onSubmit" />
  </div>
</template>

<script>
import FormCreate from '@form-create/element-ui'
import { getDeployedForm, submitTaskForm, getTaskVariables } from '@/api/task'
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
    const that = this
    this.id = this.$route.params.id
    this.formKey = this.$route.params.formKey
    // this.$data.resourceId = this.$route.params.resourceId
    getDeployedForm(this.id).then(response => {
      this.rule = response
      getTaskVariables(this.id).then(response => {
        // console.log(response)
        // console.log(that.$data.$f)
        for (const v in response) {
          const rule = this.$data.$f.getRule(v)
          switch (rule.type) {
            case 'DatePicker': {
              if (rule.props.type === 'datetimerange') {
                that.$data.$f.setValue(v, this.evil(response[v].value))
              } else {
                that.$data.$f.setValue(v, response[v].value)
              }
              break
            }
            default:
              that.$data.$f.setValue(v, response[v].value)
          }
        }
      })
    })
  },
  created() {

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
    },
    evil(fn) {
      // 一个变量指向Function，防止有些前端编译工具报错
      const Fn = Function
      return new Fn('return ' + fn)()
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
