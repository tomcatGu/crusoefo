<template>
  <div class="app-container">
    <split-pane split="vertical">
      <template slot="paneL">
        <div>
          <MonacoEditor width="100%" height="600" :code="value" class="editor" language="typescript" @codeChange="onChange" />
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
  data() {
    return {
      // 表单实例对象
      fApi: {},
      model: {},
      value: '[\n' +
        '{ type: "input",\n' +
        'field: "goods_name",\n' +
        'title: "商品名称",\n' +
         ' on: {\n' +
           ' change: (data) => {\n' +
             ' console.log(`change!![${data}]`)\n' +
            '}\n' +
         ' }\n' +
        '},\n' +
        '{\n' +
          'type: "datePicker",\n' +
         ' field: "created_at",\n' +
          'title: "创建时间"\n' +
       ' },\n' +
       ' {\n' +
         ' type: "ElButton",\n' +
          'field: "btn1",\n' +
          'children: ["test"]\n' +
       ' }\n' +

      ']',
      // 表单生成规则
      rule: [
      ],
      option: {
        resetBtn: false,
        submitBtn: false
      }
    }
  },
  mounted() {
    // this.model = this.$f.model();
    FormCreate.fApi = this.fApi
    FormCreate.businessApi = businessApi
    //console.log(formCreate.businessApi)
    // this.value = obj2String(this.$data.rule)
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
      console.log(this.value)
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
  width: 600px;
  height: 800px;
}
</style>
