<template>
  <div class="app-container">
    <split-pane split="vertical" @resize="resize">
      <template slot="paneL">
        <div class="editor">
          <MonacoEditor
            ref="editor"
            :key="randomKey"
            :code="value"
            language="typescript"
            @codeChange="onChange"
          />
        </div>
      </template>
      <template slot="paneR">
        <div class="editor-container">
          <form-create v-model="fApi" :rule="rule" :option="option" @on-submit="onSubmit" />
        </div>
      </template>
    </split-pane>
  </div>
</template>

<script>
import FormCreate from '@form-create/element-ui'
import splitPane from 'vue-splitpane'
import { obj2String, evil } from '@/utils'

// require component

// import MonacoEditor from 'vue-monaco'
import MonacoEditor from 'vue-monaco-editor2'

// import MonacoEditor from 'monaco-editor-vue'

import { createDeployment } from '@/api/repository'
import * as businessApi from '@/api/businessApi'

export default {
  components: {
    formCreate: FormCreate.$form(),
    splitPane,
    MonacoEditor
  },

  props: ['formStr'],
  data() {
    return {
      // 表单实例对象
      fApi: {},
      model: {},
      value: '[\n' +
        '{ type: "input",\n' +
        'field: "goods_name",\n' +
        'title: "名称",\n' +
         ' on: {\n' +
           ' change: (data) => {\n' +
             ' console.log(`change!![${data}]`)\n' +
            '}\n' +
         ' }\n' +
        '}\n' +
      ']',
      randomKey: 123,
      // 表单生成规则
      rule: [
      ],
      option: {
        resetBtn: false,
        submitBtn: false
      }
    }
  },
  watch: {
    formStr(val) {
      this.value = this.formStr
      this.rule = evil(this.value)
    }

  },
  mounted() {
    // this.model = this.$f.model();
    FormCreate.fApi = this.fApi
    FormCreate.businessApi = businessApi
    window['requestData'] = businessApi.requestData
    // console.log(formCreate.businessApi)
    // this.value = obj2String(this.$data.rule)
    // console.log(this.formStr)
    if (this.formStr !== undefined) { this.value = this.formStr }
    // console.log(this.formStr)
    this.rule = evil(this.value)
    // console.log(this.value)
    // debugger
  },
  methods: {
    onSubmit(formData) {
      // TODO 提交表单
      console.log(obj2String(formData))
    },
    onChange(editor) {
      // console.log(this.value)
      // this.$emit('valueChanged', data)
      this.$data.rule = evil(editor.getValue())
      this.value = editor.getValue()

      // this.$data.rule = JSON.parse(this.$data.value)
      // this.formData.create(JSON.parse(this.rule))
    },
    deploy() {
      // this.download('xml', xml)
      const formData = new FormData()
      formData.append('deployment-name', 'test')
      formData.append('deployment-source', 'test form')
      formData.append('enable-duplicate-filtering', true)
      formData.append('data', new Blob([this.$data.value]), 'test.form')

      createDeployment(formData).then(response => {
        console.log(response)
      })
    },
    resize() {
      console.log(this.$refs.editor)
      this.$refs.editor.layout()
    },
    createRandomKey() {
      this.randomKey = Math.floor(Math.random() * (10.10000000000012313))
    },
    loadForm(str) {
      // this.$nextTick(() => {
      this.value = str
      this.rule = evil(this.value)
      // createRandomKey()
      // })
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
}
.editor {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: auto;
}
</style>
