<template>
  <div class="app-container">
    <form-create
      v-model="fApi"
      :rule="rule"
      :option="option"
      @on-submit="onSubmit"
    />
  </div>
</template>

<script>
import FormCreate from '@form-create/element-ui'
import * as businessApi from '@/api/businessApi'
import { getDeployedStartForm, getDeployedStartFormByKey, submitStartForm, submitStartFormByKey } from '@/api/processes'

import { Message } from 'element-ui'
import { formatTime, obj2String, evil } from '@/utils'
import { getToken } from '@/utils/auth'
export default {
  components: {
    formCreate: FormCreate.$form()
  },
  data() {
    return {
      // 表单实例对象
      fApi: {},
      model: {},
      // 表单生成规则
      rule: [],
      option: {
        resetBtn: false,
        submitBtn: false
      },
      id: null,
      businessKey: null
    }
  },

  mounted() {
    FormCreate.fApi = this.fApi
    FormCreate.businessApi = businessApi
    FormCreate.onSubmit = this.onSubmit
  },
  created() {
    console.log(this.$route.params)
    this.id = this.$route.params.id
    this.key = this.$route.params.key

    // this.$data.resourceId = this.$route.params.resourceId
    if (this.id !== undefined) {
      getDeployedStartForm(this.id).then(response => {
        this.rule = evil(response)
      })
    }
    if (this.key !== undefined) {
      getDeployedStartFormByKey(this.key).then(response => {
        this.rule = evil(response)
      })
    }
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
      const v = {}
      v.type = 'String'
      v.value = getToken()
      variables['access_token'] = v
      if (this.id !== undefined) {
        submitStartForm(this.id, { 'variables': variables, 'businessKey': formData['businessKey'] || '未命名' }).then(response => {
          Message({
            message: '提交表单成功。',
            type: 'success',
            duration: 2 * 1000
          })
        })
      }
      if (this.key !== undefined) {
        submitStartFormByKey(this.key, { 'variables': variables, 'businessKey': formData['businessKey'] || '未命名' }).then(response => {
          Message({
            message: '提交表单成功。',
            type: 'success',
            duration: 2 * 1000
          })
        })
      }
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
