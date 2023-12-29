<template>
  <div class="app-container">
    <splitpanes split="vertical" @resize="resize">
      <pane class="monaco-split-pane-item">

        <div ref="container" @change="onChange" style="height: 360px;width: 100%;" class="editor"></div>

      </pane>
      <pane>
        <div class="editor-container">

        </div>
        <formCreate :rule="rule" v-model="fApi" :option="option" />

      </pane>
    </splitpanes>
  </div>
</template>

<script language="ts">
import FormCreate from '@form-create/element-ui'


import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'
import { obj2String, evil } from '@/utils'

// require component

// import MonacoEditor from 'vue-monaco'
//import MonacoEditor from 'vue-monaco-editor2'
//import { install as VueMonacoEditorPlugin } from '@guolao/vue-monaco-editor'

import { defineComponent, onMounted, shallowRef, ref,reactive } from 'vue';
import * as monaco from 'monaco-editor';
import EditorWorker from 'monaco-editor/esm/vs/editor/editor.worker?worker';
import JsonWorker from 'monaco-editor/esm/vs/language/json/json.worker?worker';
import CssWorker from 'monaco-editor/esm/vs/language/css/css.worker?worker';
import HtmlWorker from 'monaco-editor/esm/vs/language/html/html.worker?worker';
import TsWorker from 'monaco-editor/esm/vs/language/typescript/ts.worker?worker';


// import MonacoEditor from 'monaco-editor-vue'

import { createDeployment } from '@/api/repository'
import * as businessApi from '@/api/businessApi'

export default defineComponent({
  name: 'Deployment',
  data() {
    return {
      value: '[\n' +
        '{ type: "input",\n' +
        'field: "goods_name",\n' +
        'title: "名称1",\n' +
        ' on: {\n' +
        ' change: (data) => {\n' +
        ' console.log(`change!![${data}]`)\n' +
        '}\n' +
        ' }\n' +
        '}\n' +
        ']'

    };
  },

  components: {
    //formCreate: FormCreate.$form(),
    formCreate: FormCreate.$form(),
    Splitpanes,
    Pane

  },
  setup(props, { emit }) {
    const Fc = formCreate.$form()
    self.MonacoEnvironment = {
      getWorker(_, label) {
        console.log(_, label);
        if (label === 'json') {
          return new JsonWorker();
        }
        if (label === 'css' || label === 'scss' || label === 'less') {
          return new CssWorker();
        }
        if (label === 'html' || label === 'handlebars' || label === 'razor') {
          return new HtmlWorker();
        }
        if (label === 'typescript' || label === 'javascript') {
          return new TsWorker();
        }
        return new EditorWorker();
      }
    };

    //monaco.editor.remeasureFonts();


    monaco.languages.typescript.typescriptDefaults.setEagerModelSync(true);

    let fApi = ref({})
    //let monacoEditor = ref({})

    let value= '[\n' +
        '{ type: "input",\n' +
        'field: "goods_name",\n' +
        'title: "名称1",\n' +
        ' on: {\n' +
        ' change: (data) => {\n' +
        ' console.log(`change!![${data}]`)\n' +
        '}\n' +
        ' }\n' +
        '}\n' +
        ']'


    
    let rule = ref(evil(value))
    let option = ref({
      resetBtn: true,
      onSubmit: (formData) => {
        console.log(formData)

        emit('submit', formData)


      }

    })
    return {
      //initEditor,
      fApi,
      value,
      rule,
      option
    }
  },
  props: ['formStr'],

  watch: {
    formStr(val) {
      //this.value = this.formStr
      let rule = ref(evil(this.value))
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
    //if (this.formStr !== undefined) { this.value = this.formStr }
    // console.log(this.formStr)
    //this.rule = evil(this.value)
    // console.log(this.value)
    // debugger
    /*
    this.monacoEditor = monaco.editor.create(this.$refs.container, {
      value: this.value,
      readOnly: false,
      language: 'json',
      theme: 'vs-dark'
    });
    */
    //this.initEditor();
    this.initMonaco();
    // this.monacoEditor.onContentChanged(() => {
    //     this.value = this.monacoEditor.getValue()
    //     this.rule = evil(this.value)
    //   });
    
  },
  methods: {
    onSubmit(formData) {
      // TODO 提交表单
      console.log(obj2String(formData))
    },
    onChange(event) {
      console.log(this.monacoEditor.getValue())
      let value = this.monacoEditor.getValue()
      this.$emit('valueChanged', value)
      this.rule = evil(value)
      this.value = value

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
    },
    initMonaco() {

      this.monacoEditor = monaco.editor.create(this.$refs.container, {
        value: this.value,
        readOnly: false,
        language: 'json',
        automaticLayout: true,
        theme: 'vs-dark',
        fontSize: 16,
      });


      //this.monacoEditor.setCursorPosition(1,2);
      monaco.editor.remeasureFonts();


    }

  }
});
</script>
<style lang="scss">
.app-container {
  position: absolute;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
}

.editor-container {
  position: flex;
  width: 100%;
  height: 100%;
  align-items: left;
}

.editor {
  display: flex;
  text-align: left;
  flex-direction: column;
  align-items: left;
}

.monaco-split-pane-item {
  display: flex;
  align-items: left;
}
</style>
